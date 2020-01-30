package ua.org.training.workshop.dao.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.dao.RequestDao;
import ua.org.training.workshop.dao.mapper.AccountMapper;
import ua.org.training.workshop.dao.mapper.RequestMapper;
import ua.org.training.workshop.dao.mapper.StatusMapper;
import ua.org.training.workshop.domain.Account;
import ua.org.training.workshop.domain.Request;
import ua.org.training.workshop.domain.Status;
import ua.org.training.workshop.utilities.Pageable;
import ua.org.training.workshop.utilities.UtilitiesClass;

import java.sql.*;
import java.util.*;

public class JDBCRequestDao implements RequestDao {
    static {
        new DOMConfigurator().doConfigure(UtilitiesClass.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }
    private static Logger logger = Logger.getLogger(JDBCRequestDao.class);

    private Connection connection;
    public JDBCRequestDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Request request) throws SQLException {
        insertRequest(request);
        logger.debug("request was created! " + request.getTitle());
    }

    @Override
    public void update(Request request) throws SQLException{
        updateStatus(request);
    }

    private void updateStatus(Request request) {
    }

    private Long insertRequest(Request request) throws SQLException{
        logger.debug("create new request, with title = " + request.getTitle() + " attempt");
        Long newId;
        try(CallableStatement callableStatement =
                connection.prepareCall(MySQLQueries
                        .REQUEST_INSERT_PREPARE_STATEMENT)) {
            logger.debug("before callable statement");
            callableStatement.setString("stitle", request.getTitle());
            callableStatement.setString("sdescription", request.getDescription());
            callableStatement.setLong("nauthor", request.getAuthor().getId());
            callableStatement.setLong("nuser", request.getUser().getId());
            callableStatement.setLong("nstatus", request.getStatus().getId());
            callableStatement.setString("slang", request.getLanguage());
            callableStatement.setBoolean("bclosed", request.isClosed());
            callableStatement.registerOutParameter("nid", Types.BIGINT);
            logger.debug("after prepared callable statement");

            callableStatement.executeUpdate();

            logger.debug("after execute callable statement");

            newId = callableStatement.getLong("nid");
            connection.close();
        }catch (SQLException e){
            logger.error("create new request " + e.getMessage());
            throw e;
        }
        logger.debug("new request : " + request.getTitle() + " was successfully created");
        return newId;
    }

    @Override
    public Optional<Request> findById(Long id) {

        Request request = null;

        try (PreparedStatement pst =
                     connection.prepareStatement(MySQLQueries
                             .REQUEST_FIND_BY_ID_QUERY)) {
            pst.setLong(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                request = loadRequest(rs);
            }
        } catch (SQLException e) {
            logger.debug("get request by " + id + " sql exception : " + e.getMessage());
        }
        try {
            connection.close();
        }catch(SQLException e){
            logger.error("cannot close connection " + e.getMessage());
        }
        return Optional.ofNullable(request);
    }

    private Request loadRequest(ResultSet rs) throws SQLException {
        JDBCGenericDao<Account> jdbcAccount = new JDBCGenericDao<>();
        JDBCGenericDao<Status> jdbcStatus = new JDBCGenericDao<>();
        RequestMapper requestMapper = new RequestMapper();
        AccountMapper accountMapper = new AccountMapper();
        StatusMapper statusMapper = new StatusMapper();
        Account account;
        Status status;
        Request request = requestMapper.extractFromResultSet(rs);

        account = jdbcAccount.findById(
                rs.getLong("nauthor"),
                accountMapper,
                MySQLQueries.ACCOUNT_FIND_BY_ID_QUERY,
                connection);
        request.setAuthor(account);
        account = jdbcAccount.findById(
                rs.getLong("nuser"),
                accountMapper,
                MySQLQueries.ACCOUNT_FIND_BY_ID_QUERY,
                connection);
        request.setUser(account);
        status = jdbcStatus.findById(
                rs.getLong("nstatus"),
                statusMapper,
                MySQLQueries.STATUS_FIND_BY_ID_QUERY,
                connection);
        request.setStatus(status);

        return request;
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

    public Optional<List<Request>> getPage(Pageable page, Map<String, Object> myMap){

        List<Request> requests = new ArrayList<>();

        try (CallableStatement callableStatement =
                     connection.prepareCall(MySQLQueries
                             .REQUEST_FIND_PAGE_CALLABLE_STATEMENT)) {
            callableStatement.setLong("nlimit", page.getSize());
            callableStatement.setLong("noffset", page.getOffset());
            callableStatement.setString("ssearch", page.getSearch());
            callableStatement.setString("ssorting", page.getSorting());
            callableStatement.registerOutParameter("ncount", Types.BIGINT);
            logger.info("page : " + page.toString());
            logger.info("callable statement query : " + callableStatement.toString());

            ResultSet rs = callableStatement.executeQuery();
            myMap.put("totalElements", callableStatement.getLong("ncount"));

            while (rs.next()) {
                Request request = loadRequest(rs);
                requests.add(request);
                logger.info("reading request : " + request.getTitle());
            }
            connection.close();
        } catch (SQLException e) {
            logger.error("find all SQL exception " + e.getMessage());
        }

        return Optional.of(requests);
    }

    @Override
    public Optional<List<Request>> getPageByAuthor(Pageable page, Account author, Map<String, Object> mapJson) {

        List<Request> requests = new ArrayList<>();

        try (CallableStatement callableStatement =
                     connection.prepareCall(MySQLQueries
                             .REQUEST_FIND_PAGE_BY_AUTHOR_CALLABLE_STATEMENT)) {
            callableStatement.setLong("nlimit", page.getSize());
            callableStatement.setLong("noffset", page.getOffset());
            callableStatement.setLong("nauthor", author.getId());
            callableStatement.setString("ssearch", page.getSearch());
            callableStatement.setString("ssorting", page.getSorting());
            callableStatement.registerOutParameter("ncount", Types.BIGINT);
            logger.info("page : " + page.toString());
            logger.info("callable statement query : " + callableStatement.toString());

            ResultSet rs = callableStatement.executeQuery();
            mapJson.put("totalElements", callableStatement.getLong("ncount"));

            while (rs.next()) {
                Request request = loadRequest(rs);
                requests.add(request);
                logger.info("reading request : " + request.getTitle());
            }
            connection.close();
        } catch (SQLException e) {
            logger.error("find all SQL exception " + e.getMessage());
        }

        return Optional.of(requests);
    }
}
