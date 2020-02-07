package ua.org.training.workshop.dao.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.dao.AccountDao;
import ua.org.training.workshop.dao.mapper.AccountMapper;
import ua.org.training.workshop.dao.mapper.RoleMapper;
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
    private final static String ACCOUNT_FIND_ROLES_BY_USER_ID_QUERY =
            " select * from user_role ur" +
                    " inner join role r " +
                    " on ur.nrole = r.id" +
                    " where ur.nuser = ? ";
    private final static String ACCOUNT_FIND_BY_ID_QUERY =
            " select * from user_list u where u.id = ?";
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
    public Long create(Account account) throws SQLException {
        Long newId;
        LOGGER.info("Start transaction! --------------------------------> ");
        connection.setAutoCommit(false);
        try {
            newId = insertAccount(account);
            addRoles(account, newId);
            connection.commit();
        } catch (SQLException e) {
            LOGGER.error("SQL exception : " + e.getMessage());
            LOGGER.info("Transaction was rollback! <--------------------------------");
            connection.rollback();
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

    private void deleteAllAccountRoles(Long nuser) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(
                             ACCOUNT_DELETE_ROLES_BY_USER_ID_QUERY)) {
            preparedStatement.setLong(1, nuser);
            preparedStatement.execute();
        } catch (SQLException e) {
            LOGGER.error("delete roles of user " + nuser + e.getMessage());
        }
        LOGGER.info("users roles were deleted " + nuser);
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

    private Long insertAccount(Account account) throws SQLException {
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
            callableStatement.setString("spassword", account.getPassword());
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
    public Optional<Account> findById(Long id) {
        AccountMapper accountMapper = new AccountMapper();
        Account account = null;

        try (PreparedStatement pst =
                     connection.prepareStatement(
                             ACCOUNT_FIND_BY_ID_QUERY)) {
            pst.setLong(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                account = accountMapper.extractFromResultSet(rs,
                        ApplicationConstants.ACCOUNT_QUERY_DEFAULT_PREFIX);
            }

        } catch (SQLException e) {
            LOGGER.debug("get account by " + id + " sql exception : " + e.getMessage());
        }

        if (account != null) account.setRoles(findRolesById(account.getId()));
        close();
        return Optional.ofNullable(account);
    }

    private List<Role> findRolesById(Long id) {
        Map<Long, Role> roles = new HashMap<>();
        try (PreparedStatement pst = connection.prepareStatement(
                ACCOUNT_FIND_ROLES_BY_USER_ID_QUERY)) {
            pst.setLong(1, id);
            ResultSet rs = pst.executeQuery();
            RoleMapper roleMapper = new RoleMapper();
            while (rs.next()) {
                Role role = roleMapper
                        .extractFromResultSet(rs,
                                ApplicationConstants.ROLE_QUERY_DEFAULT_PREFIX);
                roles.put(role.getId(), role);
            }
        } catch (SQLException e) {
            LOGGER.error("find roles for user by id" + e.getMessage());
        }
        return new ArrayList<>(roles.values());
    }

    @Override
    public Page getPage(Page page) {
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
                Account account = accountMapper
                        .extractFromResultSet(rs,
                                ApplicationConstants.ACCOUNT_QUERY_DEFAULT_PREFIX);
                accounts.add(account);
            }
        } catch (SQLException e) {
            LOGGER.error("find all SQL exception " + e.getMessage());
        }
        close();
        page.setContent(Optional.of(accounts));
        return page;
    }

    @Override
    public void delete(Long id) {

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
    public void create(Account account, String password) {

    }

    @Override
    public void update(Account account, String password) {

    }

    @Override
    public Optional<Account> findByUsername(String username) {
        return findByUniqueAttribute(username,
                ACCOUNT_FIND_BY_USERNAME_QUERY);
    }

    @Override
    public Optional<Account> findByPhone(String phone) {
        return findByUniqueAttribute(phone,
                ACCOUNT_FIND_BY_PHONE_QUERY);
    }

    @Override
    public Optional<Account> findByEmail(String email) {
        return findByUniqueAttribute(email,
                ACCOUNT_FIND_BY_EMAIL_QUERY);
    }

    private Optional<Account> findByUniqueAttribute(String uniqueAttribute,
                                                    String query) {
        Account account = null;
        try (PreparedStatement pst =
                     connection.prepareStatement(query)) {
            pst.setString(1, uniqueAttribute);
            ResultSet rs = pst.executeQuery();
            AccountMapper accountMapper = new AccountMapper();
            while (rs.next()) {
                account = accountMapper
                        .extractFromResultSet(rs,
                                ApplicationConstants.ACCOUNT_QUERY_DEFAULT_PREFIX);
            }
            if (account != null) account.setRoles(findRolesById(account.getId()));
        } catch (SQLException e) {
            LOGGER.error("get account by " + uniqueAttribute + " sql exception : " + e.getMessage());
        }
        close();
        return Optional.ofNullable(account);
    }
}
