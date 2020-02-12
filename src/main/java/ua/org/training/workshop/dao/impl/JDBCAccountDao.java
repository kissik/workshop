package ua.org.training.workshop.dao.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.dao.AccountDao;
import ua.org.training.workshop.dao.mapper.AccountMapper;
import ua.org.training.workshop.domain.Account;
import ua.org.training.workshop.domain.Role;
import ua.org.training.workshop.enums.WorkshopError;
import ua.org.training.workshop.exception.WorkshopException;
import ua.org.training.workshop.utility.ApplicationConstants;
import ua.org.training.workshop.utility.Page;

import java.sql.*;
import java.util.*;

public class JDBCAccountDao implements AccountDao {

    static {
        new DOMConfigurator().doConfigure(ApplicationConstants.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }

    private final static String ACCOUNT_FIND_PAGE_CALLABLE_STATEMENT =
            "CALL APP_PAGINATION_USER_LIST (?, ?, ?, ?, ?);";
    private final static String ACCOUNT_DELETE_ROLES_BY_USER_ID_QUERY =
            " delete from user_role where nuser = ? ";
    private final static String ACCOUNT_DELETE_BY_ID_QUERY =
            " delete from user_list where id = ? ";
    private final static String AUTHOR_FIND_BY_HISTORY_REQUEST_ID_QUERY =
            " select u.* from user_list u " +
                    " inner join history_request_list h on u.id = h.nauthor" +
                    " where h.id = ?";
    private final static String USER_FIND_BY_HISTORY_REQUEST_ID_QUERY =
            " select u.* from user_list u " +
                    " inner join history_request_list h on u.id = h.nuser" +
                    " where h.id = ?";
    private final static String ACCOUNT_FIND_BY_ID_QUERY =
            " select * from user_list u where u.id = ?";
    private final static String AUTHOR_FIND_BY_REQUEST_ID_QUERY =
            " select u.* from user_list u " +
                    " inner join request_list r on u.id = r.nauthor" +
                    " where r.id = ?";
    private final static String USER_FIND_BY_REQUEST_ID_QUERY =
            " select u.* from user_list u " +
                    " inner join request_list r on u.id = r.nuser" +
                    " where r.id = ?";
    private final static String ACCOUNT_FIND_BY_USERNAME_QUERY =
            " select * from user_list u where slogin = ?";
    private final static String ACCOUNT_FIND_BY_PHONE_QUERY =
            " select * from user_list u where sphone = ?";
    private final static String ACCOUNT_FIND_BY_EMAIL_QUERY =
            " select * from user_list u where semail = ?";
    private final static String ACCOUNT_INSERT_CALLABLE_STATEMENT =
            "CALL APP_INSERT_USER_LIST (?,?,?,?,?,?,?,?,?, ?);";
    private final static String ACCOUNT_INSERT_ROLE_PREPARE_STATEMENT =
            "INSERT INTO `user_role`" +
                    "(`nuser`,`nrole`) " +
                    "VALUES (?,?)";

    private final static Logger LOGGER = Logger.getLogger(JDBCAccountDao.class);

    private Connection connection;

    public JDBCAccountDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Long create(Account account, String password) throws SQLException {
        Long newId;
        LOGGER.info("Start transaction! --------------------------------> ");
        connection.setAutoCommit(false);
        try {
            newId = insertAccount(account, password);
            addRoles(account, newId);
            connection.commit();
        } catch (SQLException e) {
            LOGGER.error("SQL exception : " + e.getMessage());
            LOGGER.info("Transaction was rollback! <--------------------------------");
            connection.rollback();
            close();
            throw new WorkshopException(WorkshopError.ACCOUNT_CREATE_NEW_ERROR);
        }
        LOGGER.info("Transaction was successfully committed! <--------------------------------");
        close();
        return newId;
    }

    @Override
    public void update(Account account) throws SQLException {
        LOGGER.info("Start transaction! --------------------------------> ");
        connection.setAutoCommit(false);
        try {
            deleteAllAccountRoles(account.getId());
            addRoles(account, account.getId());
            connection.commit();
        } catch (SQLException e) {
            LOGGER.error("SQL exception : " + e.getMessage());
            LOGGER.info("Transaction was rollback! <--------------------------------");
            connection.rollback();
            throw new WorkshopException(WorkshopError.ACCOUNT_UPDATE_ERROR);
        }
        LOGGER.info("Transaction was successfully committed! <--------------------------------");
        close();
    }

    private void addRoles(Account account, Long id) throws SQLException {
        for (Role r : account.getRoles())
            insertAccountRole(id, r.getId());
        LOGGER.info("account have new role(s)");
    }

    private void deleteAllAccountRoles(Long nuser) throws SQLException {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(
                             ACCOUNT_DELETE_ROLES_BY_USER_ID_QUERY)) {
            preparedStatement.setLong(1, nuser);
            preparedStatement.execute();
        } catch (SQLException e) {
            LOGGER.error("delete roles of account : " + nuser + e.getMessage());
        }
        LOGGER.info("account roles were deleted " + nuser);
    }

    @Override
    public void delete(Long id) throws SQLException {
        LOGGER.info("Start transaction! --------------------------------> ");
        connection.setAutoCommit(false);
        try{
            deleteAllAccountRoles(id);
            deleteAccount(id);
            connection.commit();
        }catch (SQLException e){
            LOGGER.error("SQL exception : " + e.getMessage());
            LOGGER.info("Transaction was rollback! <--------------------------------");
            connection.rollback();
            throw new WorkshopException(WorkshopError.ACCOUNT_DELETE_ERROR);
        }
        LOGGER.info("Transaction was successfully committed! <--------------------------------");
        close();
    }

    private void deleteAccount(Long id) throws SQLException{
        PreparedStatement preparedStatement =
                     connection.prepareStatement(
                             ACCOUNT_DELETE_BY_ID_QUERY);
        preparedStatement.setLong(1, id);
        preparedStatement.execute();
    }

    private void insertAccountRole(Long nuser, Long nrole) throws SQLException {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(
                             ACCOUNT_INSERT_ROLE_PREPARE_STATEMENT)) {
            preparedStatement.setLong(1, nuser);
            preparedStatement.setLong(2, nrole);
            preparedStatement.execute();
        } catch (SQLException e) {
            LOGGER.error("add roles to user " + e.getMessage());
            throw e;
        }
        LOGGER.info("user has role: " + nrole.toString());
    }

    private Long insertAccount(Account account, String password) throws SQLException {
        LOGGER.info("create new user, with username = " + account.getUsername() + " attempt");
        Long newId;
        try (CallableStatement callableStatement =
                     connection.prepareCall(
                             ACCOUNT_INSERT_CALLABLE_STATEMENT)) {
            callableStatement.setString("slogin", account.getUsername());
            callableStatement.setString("sfirst_name", account.getFirstName());
            callableStatement.setString("slast_name", account.getLastName());
            callableStatement.setString("semail", account.getEmail());
            callableStatement.setString("sphone", account.getPhone());
            callableStatement.setString("spassword", password);
            callableStatement.setBoolean("benabled", account.isEnabled());
            callableStatement.setString("sfirst_name_origin", account.getFirstNameOrigin());
            callableStatement.setString("slast_name_origin", account.getLastNameOrigin());
            callableStatement.registerOutParameter("nid", java.sql.Types.BIGINT);
            callableStatement.executeUpdate();
            newId = callableStatement.getLong("nid");
        } catch (SQLException e) {
            LOGGER.error("create new user " + e.getMessage());
            throw e;
        }
        LOGGER.info("new user : " + account.getUsername() + " was successfully created");
        return newId;
    }

    @Override
    public Optional<Account> findAuthorByRequestId(Long id) {
        return findByUniqueLongAttribute(id, AUTHOR_FIND_BY_REQUEST_ID_QUERY);
    }

    @Override
    public Optional<Account> findUserByRequestId(Long id) {
        return findByUniqueLongAttribute(id, USER_FIND_BY_REQUEST_ID_QUERY);
    }

    @Override
    public Optional<Account> findAuthorByHistoryRequestId(Long id) {
        return findByUniqueLongAttribute(id, AUTHOR_FIND_BY_HISTORY_REQUEST_ID_QUERY);
    }

    @Override
    public Optional<Account> findUserByHistoryRequestId(Long id) {
        return findByUniqueLongAttribute(id, USER_FIND_BY_HISTORY_REQUEST_ID_QUERY);
    }

    @Override
    public Optional<Account> findById(Long id) {
        return findByUniqueLongAttribute(id, ACCOUNT_FIND_BY_ID_QUERY);
    }

    private Optional<Account> findByUniqueLongAttribute(Long id, String query){

        AccountMapper accountMapper = new AccountMapper();
        Account account = null;

        try (PreparedStatement pst =
                     connection.prepareStatement(
                             query)) {
            pst.setLong(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                account = accountMapper.extractFromResultSet(rs);
            }

        } catch (SQLException e) {
            LOGGER.debug("get account by " + id + " sql exception : " + e.getMessage());
        }
        close();
        return Optional.ofNullable(account);
    }

    @Override
    public Page<Account> getPage(Page<Account> page) {
        List<Account> accounts = new ArrayList<>();
        try (CallableStatement callableStatement =
                     connection.prepareCall(ACCOUNT_FIND_PAGE_CALLABLE_STATEMENT)) {
            callableStatement.setLong("nlimit", page.getSize());
            callableStatement.setLong("noffset", page.getOffset());
            callableStatement.setString("ssearch", page.getSearch());
            callableStatement.setString("ssorting", page.getSorting());
            callableStatement.registerOutParameter("ncount", java.sql.Types.BIGINT);
            ResultSet rs = callableStatement.executeQuery();
            page.setTotalElements(callableStatement.getLong("ncount"));
            AccountMapper accountMapper = new AccountMapper();
            while (rs.next()) {
                accounts.add(accountMapper
                        .extractFromResultSet(rs));
            }
        } catch (SQLException e) {
            LOGGER.error("find all SQL exception " + e.getMessage());
        }
        close();
        page.setContent(Optional.of(accounts));
        return page;
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            LOGGER.error("cannot close connection " + e.getMessage());
            throw new WorkshopException(WorkshopError.DATABASE_CONNECTION_ERROR);
        }
    }

    @Override
    public Long create(Account account) {
        return ApplicationConstants.APP_DEFAULT_ID;
    }

    @Override
    public void update(Account account, String password) {

    }

    @Override
    public Optional<Account> findByUsername(String username) {
        return findByUniqueStringAttribute(username,
                ACCOUNT_FIND_BY_USERNAME_QUERY);
    }

    @Override
    public Optional<Account> findByPhone(String phone) {
        return findByUniqueStringAttribute(phone,
                ACCOUNT_FIND_BY_PHONE_QUERY);
    }

    @Override
    public Optional<Account> findByEmail(String email) {
        return findByUniqueStringAttribute(email,
                ACCOUNT_FIND_BY_EMAIL_QUERY);
    }

    private Optional<Account> findByUniqueStringAttribute(String uniqueAttribute,
                                                          String query) {
        Account account = null;
        try (PreparedStatement pst =
                     connection.prepareStatement(query)) {
            pst.setString(1, uniqueAttribute);
            ResultSet rs = pst.executeQuery();
            AccountMapper accountMapper = new AccountMapper();
            if (rs.next()) {
                account = accountMapper
                        .extractFromResultSet(rs);
            }
        } catch (SQLException e) {
            LOGGER.error("get account by " + uniqueAttribute + " sql exception : " + e.getMessage());
        }
        close();
        return Optional.ofNullable(account);
    }
}
