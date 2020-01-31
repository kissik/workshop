package ua.org.training.workshop.dao.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.dao.AccountDao;
import ua.org.training.workshop.dao.mapper.AccountMapper;
import ua.org.training.workshop.dao.mapper.RoleMapper;
import ua.org.training.workshop.domain.Account;
import ua.org.training.workshop.domain.Role;
import ua.org.training.workshop.exception.WorkshopErrors;
import ua.org.training.workshop.exception.WorkshopException;
import ua.org.training.workshop.utilities.Pageable;
import ua.org.training.workshop.utilities.UtilitiesClass;

import java.sql.*;
import java.util.*;

public class JDBCAccountDao implements AccountDao {
    static {
        new DOMConfigurator().doConfigure(UtilitiesClass.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }
    private static Logger logger = Logger.getLogger(JDBCAccountDao.class);

    private Connection connection;
    public JDBCAccountDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Account account) throws SQLException{
        logger.info("Start transaction! --------------------------------> ");
        connection.setAutoCommit(false);
        try {
            Long newId = insertAccount(account);
            addRoles(account, newId);
            connection.commit();
        }catch (SQLException e) {
            logger.error("SQL exception : " + e.getMessage());
            logger.info("Transaction was rollback! <--------------------------------");
            connection.rollback();
            throw new WorkshopException(WorkshopErrors.ACCOUNT_CREATE_NEW_ERROR);
        }
        logger.info("Transaction was successfully committed! <--------------------------------");
        close();
    }

    @Override
    public void update(Account account) throws SQLException{
        logger.info("Start transaction! --------------------------------> ");
        connection.setAutoCommit(false);
        try{
            deleteAllAccountRoles(account.getId());
            addRoles(account, account.getId());
            connection.commit();
        }catch (SQLException e) {
            logger.error("SQL exception : " + e.getMessage());
            logger.info("Transaction was rollback! <--------------------------------");
            connection.rollback();
            throw new WorkshopException(WorkshopErrors.ACCOUNT_UPDATE_ERROR);
        }
        logger.info("Transaction was successfully committed! <--------------------------------");
        close();
    }

    private void addRoles(Account account, Long id) throws SQLException {
        for(Role r : account.getRoles())
            insertAccountRole(id, r.getId());
        logger.info("account have new role(s)");
    }

    private void deleteAllAccountRoles(Long nuser){
        try(PreparedStatement preparedStatement =
                    connection.prepareStatement(MySQLQueries
                            .ACCOUNT_DELETE_ROLES_BY_USER_ID_QUERY)) {
            preparedStatement.setLong(1, nuser);
            preparedStatement.execute();
        }
        catch(SQLException e){
            logger.error("delete roles of user " + nuser + e.getMessage());
        }
        logger.info("users roles were deleted " + nuser);
    }

    private void insertAccountRole(Long nuser, Long nrole) throws SQLException{
        try(PreparedStatement preparedStatement =
            connection.prepareStatement(MySQLQueries
                    .ACCOUNT_INSERT_ROLE_PREPARE_STATEMENT)) {
            preparedStatement.setLong(1, nuser);
            preparedStatement.setLong(2, nrole);
            preparedStatement.execute();
        }
        catch(SQLException e){
            logger.error("add roles to user " + e.getMessage());
            throw e;
        }
        logger.info("user has role: " + nrole.toString());
    }

    private Long insertAccount(Account account) throws SQLException{
        logger.info("create new user, with username = " + account.getUsername() + " attempt");
        Long newId;
        try(CallableStatement callableStatement =
                connection.prepareCall(MySQLQueries
                        .ACCOUNT_INSERT_CALLABLE_STATEMENT)) {
            callableStatement.setString("slogin", account.getUsername());
            callableStatement.setString("sfirst_name", account.getFirstName());
            callableStatement.setString("slast_name", account.getLastName());
            callableStatement.setString("semail", account.getEmail());
            callableStatement.setString("sphone", account.getPhone());
            callableStatement.setString("spassword", account.getPassword());
            callableStatement.setBoolean("benabled", account.isEnabled());
            callableStatement.setString("sfirst_name_origin", account.getFirstNameOrigin());
            callableStatement.setString("slast_name_origin", account.getLastNameOrigin());
            callableStatement.registerOutParameter("nid", java.sql.Types.BIGINT);
            callableStatement.executeUpdate();
            newId = callableStatement.getLong("nid");
        }catch (SQLException e){
            logger.error("create new user " + e.getMessage());
            throw e;
        }
        logger.info("new user : " + account.getUsername() + " was successfully created");
        return newId;
    }

    @Override
    public Optional<Account> findById(Long id) {
        AccountMapper accountMapper = new AccountMapper();
        Account account = null;

        try (PreparedStatement pst =
                     connection.prepareStatement(
                             MySQLQueries.ACCOUNT_FIND_BY_ID_QUERY)) {
            pst.setLong(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                account = accountMapper.extractFromResultSet(rs,
                        UtilitiesClass.ACCOUNT_QUERY_DEFAULT_PREFIX);
            }

        } catch (SQLException e) {
            logger.debug("get account by " + id + " sql exception : " + e.getMessage());
        }

        if (account != null) account.setRoles(findRolesById(account.getId()));
        close();
        return Optional.ofNullable(account);
    }

    private List<Role> findRolesById(Long id) {
        Map<Long, Role> roles = new HashMap<>();
        try (PreparedStatement pst = connection.prepareStatement(MySQLQueries
                .ACCOUNT_FIND_ROLES_BY_USER_ID_QUERY)) {
            pst.setLong(1, id);
            ResultSet rs = pst.executeQuery();
            RoleMapper roleMapper = new RoleMapper();
            while (rs.next()) {
                Role role = roleMapper
                            .extractFromResultSet(rs,
                                    UtilitiesClass.ROLE_QUERY_DEFAULT_PREFIX);
                roles.put(role.getId(), role);
            }
        } catch (SQLException e) {
            logger.error("find roles for user by id" + e.getMessage());
        }
        return new ArrayList<>(roles.values());
    }

    @Override
    public Pageable getPage(Pageable page){
        List<Account> accounts = new ArrayList<>();
        try (CallableStatement callableStatement =
                     connection.prepareCall(MySQLQueries.ACCOUNT_FIND_PAGE_CALLABLE_STATEMENT)) {
            callableStatement.setLong("nlimit", page.getSize());
            callableStatement.setLong("noffset", page.getOffset());
            callableStatement.setString("ssearch", page.getSearch());
            callableStatement.setString("ssorting", page.getSorting());
            callableStatement.registerOutParameter("ncount", java.sql.Types.BIGINT);
            ResultSet rs = callableStatement.executeQuery();
            page.setTotalElements(callableStatement.getLong("ncount"));
            AccountMapper accountMapper = new AccountMapper();
            while (rs.next()) {
                Account account = accountMapper
                        .extractFromResultSet(rs,
                                UtilitiesClass.ACCOUNT_QUERY_DEFAULT_PREFIX);
                accounts.add(account);
            }
        } catch (SQLException e) {
            logger.error("find all SQL exception " + e.getMessage());
        }
        close();
        page.setContent(Optional.of(accounts));
        return page;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void close()  {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("cannot close connection " + e.getMessage());
            throw new WorkshopException(WorkshopErrors.DATABASE_CONNECTION_ERROR);
        }
    }

    @Override
    public void create(Account account, String password) {

    }

    @Override
    public void update(Account account, String password) {

    }

    @Override
    public Optional<Account> findByUsername(String username){
        return findByUniqueAttribute(username, MySQLQueries
                .ACCOUNT_FIND_BY_USERNAME_QUERY);
    }

    @Override
    public Optional<Account> findByPhone(String phone) {
        return findByUniqueAttribute(phone, MySQLQueries
                .ACCOUNT_FIND_BY_PHONE_QUERY);
    }

    @Override
    public Optional<Account> findByEmail(String email) {
        return findByUniqueAttribute(email, MySQLQueries
                .ACCOUNT_FIND_BY_EMAIL_QUERY);
    }

    private Optional<Account> findByUniqueAttribute(String uniqueAttribute,
                                                    String query){
        Account account = null;
        try (PreparedStatement pst =
                     connection.prepareStatement(query)) {
            pst.setString(1, uniqueAttribute);
            ResultSet rs = pst.executeQuery();
            AccountMapper accountMapper = new AccountMapper();
            while (rs.next()) {
                account = accountMapper
                        .extractFromResultSet(rs,
                                UtilitiesClass.ACCOUNT_QUERY_DEFAULT_PREFIX);
            }
            if (account != null) account.setRoles(findRolesById(account.getId()));
        } catch (SQLException e) {
            logger.error("get account by " + uniqueAttribute + " sql exception : " + e.getMessage());
        }
        close();
        return Optional.ofNullable(account);
    }
}
