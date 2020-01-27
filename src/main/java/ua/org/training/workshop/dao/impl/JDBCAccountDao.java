package ua.org.training.workshop.dao.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import ua.org.training.workshop.dao.AccountDao;
import ua.org.training.workshop.dao.mapper.AccountMapper;
import ua.org.training.workshop.dao.mapper.RoleMapper;
import ua.org.training.workshop.domain.Account;
import ua.org.training.workshop.domain.Role;
import ua.org.training.workshop.utilities.Pageable;

import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class JDBCAccountDao implements AccountDao {
    static {
        new DOMConfigurator().doConfigure("src/log4j.xml", LogManager.getLoggerRepository());
    }
    private static Logger logger = Logger.getLogger(JDBCAccountDao.class);
    private static final String FIND_ALL_QUERY =
            " select * from user_list u";
    private static final String FIND_PAGE_QUERY =
            "CALL APP_PAGINATION_USER_LIST (?, ?, ?, ?, ?);";
    private static final String DELETE_ROLES_BY_USER_ID_QUERY =
            " delete from user_role where nuser = ? ";
    private static final String FIND_ROLES_BY_USER_ID_QUERY =
            " select * from user_role ur" +
                    " inner join role r " +
                    " on ur.nrole = r.id" +
                    " where ur.nuser = ? ";
    private static final String FIND_BY_ID_QUERY =
            " select * from user_list u where u.id = ?";
    private static final String FIND_BY_USERNAME_QUERY =
            " select * from user_list u where slogin = ?";
    private static final String FIND_BY_PHONE_QUERY =
            " select * from user_list u where sphone = ?";
    private static final String FIND_BY_EMAIL_QUERY =
            " select * from user_list u where semail = ?";
    private static final String INSERT_ACCOUNT_PREPARE_STATEMENT =
            "CALL APP_INSERT_USER_LIST (?,?,?,?,?,?,?,?,?, ?);";
    private static final String INSERT_ACCOUNT_ROLE_PREPARE_STATEMENT =
            "INSERT INTO `user_role`" +
            "(`nuser`,`nrole`) " +
            "VALUES (?,?)";
    private Connection connection;
    public JDBCAccountDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Account account) throws SQLException {
        Long newId = insertAccount(account);
        logger.debug("account was inserted! " + account.getFullName());
        addRoles(account, newId);
    }

    @Transactional
    @Override
    public void update(Account account) throws SQLException{
        deleteAllAccountRoles(account.getId());
        addRoles(account, account.getId());
    }

    private void addRoles(Account account, Long id) throws SQLException{
        for(Role r : account.getRoles())
            insertAccountRole(id, r.getId());
        logger.debug("account have new role(s)");
        connection.close();
    }

    private void deleteAllAccountRoles(Long nuser) throws SQLException{
        logger.debug("delete all roles of user " + nuser);
        try(PreparedStatement preparedStatement =
                    connection.prepareStatement(DELETE_ROLES_BY_USER_ID_QUERY)) {
            preparedStatement.setLong(1, nuser);
            preparedStatement.execute();
        }
        catch(SQLException e){
            logger.error("delete roles of user " + nuser + e.getMessage());
            throw e;
        }
        logger.debug("users roles were deleted " + nuser);
    }

    private void insertAccountRole(Long nuser, Long nrole) throws SQLException {
        logger.debug("create role " + nrole.toString() + " to user " + nuser.toString());
        try(PreparedStatement preparedStatement =
            connection.prepareStatement(INSERT_ACCOUNT_ROLE_PREPARE_STATEMENT)) {
            preparedStatement.setLong(1, nuser);
            preparedStatement.setLong(2, nrole);
            preparedStatement.execute();
        }
        catch(SQLException e){
            logger.error("add roles to user " + e.getMessage());
            throw e;
        }
        logger.debug("user has role: " + nrole.toString());
    }

    private Long insertAccount(Account account) throws SQLException{
        logger.debug("create new user, with username = " + account.getUsername() + " attempt");
        Long newId;
        try(CallableStatement callableStatement =
                connection.prepareCall(INSERT_ACCOUNT_PREPARE_STATEMENT)) {
            logger.debug("before callable statement");
            callableStatement.setString("slogin", account.getUsername());
            logger.debug("set string login");
            callableStatement.setString("sfirst_name", account.getFirstName());
            callableStatement.setString("slast_name", account.getLastName());
            callableStatement.setString("semail", account.getEmail());
            callableStatement.setString("sphone", account.getPhone());
            callableStatement.setString("spassword", account.getPassword());
            callableStatement.setBoolean("benabled", account.isEnabled());
            callableStatement.setString("sfirst_name_origin", account.getFirstNameOrigin());
            callableStatement.setString("slast_name_origin", account.getLastNameOrigin());
            callableStatement.registerOutParameter("nid", java.sql.Types.BIGINT);
            logger.debug("after prepared callable statement");

            callableStatement.executeUpdate();

            logger.debug("after execute callable statement");

            newId = callableStatement.getLong("nid");
        }catch (SQLException e){
            logger.error("create new user " + e.getMessage());
            throw e;
        }
        logger.debug("new user : " + account.getUsername() + " was successfully created");
        return newId;
    }

    @Override
    public Optional<Account> findById(Long id) {

        Account account = null;

        try (PreparedStatement pst =
                     connection.prepareStatement(FIND_BY_ID_QUERY)) {
            pst.setLong(1, id);
            ResultSet rs = pst.executeQuery();

            AccountMapper accountMapper = new AccountMapper();
            while (rs.next()) {
                account = accountMapper
                        .extractFromResultSet(rs);
            }
            if (account != null) account.setRoles(findRolesById(account.getId()));

        } catch (SQLException e) {
            logger.debug("get account by " + id + " sql exception : " + e.getMessage());
        }
        try {
            connection.close();
        }catch(SQLException e){
            logger.error("cannot close connection " + e.getMessage());
        }
        return Optional.ofNullable(account);
    }

    private List<Role> findRolesById(Long id) throws SQLException {
        Map<Long, Role> roles = new HashMap<>();

        try (PreparedStatement pst = connection.prepareStatement(FIND_ROLES_BY_USER_ID_QUERY)) {
            pst.setLong(1, id);
            ResultSet rs = pst.executeQuery();

                RoleMapper roleMapper = new RoleMapper();

                while (rs.next()) {
                    Role role = roleMapper
                            .extractFromResultSet(rs);
                    roles.put(role.getId(), role);
                }

        } catch (SQLException e) {
            logger.debug("find roles for user by id" + e.getMessage());
        }
        connection.close();
        return new ArrayList<>(roles.values());
    }

    @Override
    public Optional<List<Account>> findAll() {
        Map<Long, Account> accounts = new HashMap<>();

        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(FIND_ALL_QUERY);

            AccountMapper accountMapper = new AccountMapper();

            while (rs.next()) {
                Account account = accountMapper
                        .extractFromResultSet(rs);
                accounts.put(account.getId(),account);
            }
            connection.close();
        } catch (SQLException e) {
            logger.error("find all SQL exception " + e.getMessage());
        }

        return Optional.of(new ArrayList<>(accounts.values()));
    }

    @Override
    public String getPage(Pageable page) {

        String jsonString = "";
        ObjectMapper jsonMapper = new ObjectMapper();

        Map<String, Object> myMap = makeMap(page);

        try{
            jsonString = jsonMapper.writeValueAsString(myMap);

        }catch (JsonGenerationException e) {
            logger.error("JSON generation exception : " + e.getMessage());
        } catch (JsonMappingException e) {
            logger.error("JSON mapping exception : " + e.getMessage());
        } catch (IOException e) {
            logger.error("IO exception : " + e.getMessage());
        }

        return jsonString;
    }

    private Map<String, Object> makeMap(Pageable page) {
        Map<String, Object> myMap = new HashMap<>();

        myMap.put("content", getListPage(page, myMap).get());
        myMap.put("size", page.getSize());

        return myMap;
    }

    private Optional<List<Account>> getListPage(Pageable page, Map<String, Object> myMap){

        Map<Long, Account> accounts = new HashMap<>();

        try (CallableStatement callableStatement =
                     connection.prepareCall(FIND_PAGE_QUERY)) {
            callableStatement.setLong("nlimit", page.getSize());
            callableStatement.setLong("noffset", page.getOffset());
            callableStatement.setString("ssearch", page.getSearch());
            callableStatement.setString("ssorting", page.getSorting());
            callableStatement.registerOutParameter("ncount", java.sql.Types.BIGINT);
            logger.info("page : " + page.toString());
            logger.info("callable statement query : " + callableStatement.toString());

            ResultSet rs = callableStatement.executeQuery();
            myMap.put("totalElements", callableStatement.getLong("ncount"));
            AccountMapper accountMapper = new AccountMapper();

            while (rs.next()) {
                Account account = accountMapper
                        .extractFromResultSet(rs);
                accounts.put(account.getId(),account);
                logger.info("reading account : " + account.getFullName());
            }
            connection.close();
        } catch (SQLException e) {
            logger.error("find all SQL exception " + e.getMessage());
        }

        return Optional.of(new ArrayList<>(accounts.values()));
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void close()  {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
        return findByUniqueAttribute(username, FIND_BY_USERNAME_QUERY);
    }

    @Override
    public Optional<Account> findByPhone(String phone) {
        return findByUniqueAttribute(phone, FIND_BY_PHONE_QUERY);
    }

    @Override
    public Optional<Account> findByEmail(String email) {
        return findByUniqueAttribute(email, FIND_BY_EMAIL_QUERY);
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
                        .extractFromResultSet(rs);
            }
            if (account != null) account.setRoles(findRolesById(account.getId()));

        } catch (SQLException e) {
            logger.debug("get account by " + uniqueAttribute + " sql exception : " + e.getMessage());
        }
        try {
            connection.close();
        }catch(SQLException e){
            logger.error("cannot close connection " + e.getMessage());
        }
        return Optional.ofNullable(account);
    }

    @Override
    public List<Account> getAccountList() {
        return null;
    }
}
