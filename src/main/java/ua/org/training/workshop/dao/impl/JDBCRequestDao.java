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
import ua.org.training.workshop.exception.WorkshopErrors;
import ua.org.training.workshop.exception.WorkshopException;
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
    public Long create(Request request) throws WorkshopException {
        Long newId;
        try {
            newId = insertRequest(request);
            logger.debug("request (id = "
                    + newId
                    + ") was created: " + request.getTitle());
        }catch (SQLException e){
            logger.error("SQL exception : " + e.getMessage());
            throw new WorkshopException(WorkshopErrors.REQUEST_CREATE_NEW_ERROR);
        }
        return newId;
    }

    @Override
    public void update(Request request) throws SQLException{
        updateStatus(request);
    }

    private void updateStatus(Request request) {
    }

    private Long insertRequest(Request request) throws SQLException{
        Long newId;
        try(CallableStatement callableStatement =
                connection.prepareCall(MySQLQueries
                        .REQUEST_INSERT_PREPARE_STATEMENT)) {
            callableStatement.setString("stitle", request.getTitle());
            callableStatement.setString("sdescription", request.getDescription());
            callableStatement.setLong("nauthor", request.getAuthor().getId());
            callableStatement.setLong("nuser", request.getUser().getId());
            callableStatement.setLong("nstatus", request.getStatus().getId());
            callableStatement.setString("slang", request.getLanguage());
            callableStatement.setBoolean("bclosed", request.isClosed());
            callableStatement.registerOutParameter("nid", Types.BIGINT);
            callableStatement.executeUpdate();
            newId = callableStatement.getLong("nid");
        }catch (SQLException e){
            logger.error("create new request " + e.getMessage());
            throw e;
        }
        close();
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
        close();
        return Optional.ofNullable(request);
    }

    private Request loadRequest(ResultSet rs) throws SQLException {
        RequestMapper requestMapper = new RequestMapper();
        AccountMapper accountMapper = new AccountMapper();
        StatusMapper statusMapper = new StatusMapper();
        Request request = requestMapper.extractFromResultSet(rs,
                UtilitiesClass.REQUEST_QUERY_DEFAULT_PREFIX);

        request.setAuthor(
                accountMapper.extractFromResultSet(rs,
                        UtilitiesClass.REQUEST_AUTHOR_QUERY_DEFAULT_PREFIX));
        request.setUser(
                accountMapper.extractFromResultSet(rs,
                        UtilitiesClass.REQUEST_USER_QUERY_DEFAULT_PREFIX));
        request.setStatus(
                statusMapper.extractFromResultSet(rs,
                        UtilitiesClass.STATUS_QUERY_DEFAULT_PREFIX));

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
            logger.error("cannot close connection" + e.getMessage());
            throw new WorkshopException(WorkshopErrors.DATABASE_CONNECTION_ERROR);
        }
    }

    public Pageable getPage(Pageable page){
        List<Request> requests = new ArrayList<>();
        try (CallableStatement callableStatement =
                     connection.prepareCall(MySQLQueries
                             .REQUEST_FIND_PAGE_CALLABLE_STATEMENT)) {
            callableStatement.setLong("nlimit", page.getSize());
            callableStatement.setLong("noffset", page.getOffset());
            callableStatement.setString("ssearch", page.getSearch());
            callableStatement.setString("ssorting", page.getSorting());
            callableStatement.registerOutParameter("ncount", Types.BIGINT);
            ResultSet rs = callableStatement.executeQuery();
            page.setTotalElements(callableStatement.getLong("ncount"));
            while (rs.next()) {
                Request request = loadRequest(rs);
                requests.add(request);
            }
        } catch (SQLException e) {
            logger.error("find all SQL exception " + e.getMessage());
        }
        close();
        page.setContent(Optional.of(requests));
        return page;
    }

    @Override
    public Pageable getPageByAuthor(Pageable page, Account author) {
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
            ResultSet rs = callableStatement.executeQuery();
            page.setTotalElements(callableStatement.getLong("ncount"));
            while (rs.next()) {
                Request request = loadRequest(rs);
                requests.add(request);
            }
        } catch (SQLException e) {
            logger.error("find all SQL exception " + e.getMessage());
        }
        close();
        page.setContent(Optional.of(requests));
        return page;
    }

    @Override
    public Pageable getPageByLanguageAndAuthor(Pageable page, String language, Account author) {
        List<Request> requests = new ArrayList<>();
        try (CallableStatement callableStatement =
                     connection.prepareCall(MySQLQueries
                             .REQUEST_FIND_PAGE_BY_LANGUAGE_AND_AUTHOR_CALLABLE_STATEMENT)) {
            callableStatement.setLong("nlimit", page.getSize());
            callableStatement.setLong("noffset", page.getOffset());
            callableStatement.setLong("nauthor", author.getId());
            callableStatement.setString("slang", language);
            callableStatement.setString("ssearch", page.getSearch());
            callableStatement.setString("ssorting", page.getSorting());
            callableStatement.registerOutParameter("ncount", Types.BIGINT);
            ResultSet rs = callableStatement.executeQuery();
            page.setTotalElements(callableStatement.getLong("ncount"));
            while (rs.next()) {
                Request request = loadRequest(rs);
                requests.add(request);
            }
        } catch (SQLException e) {
            logger.error("find all SQL exception " + e.getMessage());
        }
        close();
        page.setContent(Optional.of(requests));
        return page;
    }

    @Override
    public Pageable getPageByLanguageAndStatus(
            Pageable page,
            String language,
            Status status) {
        List<Request> requests = new ArrayList<>();
        try (CallableStatement callableStatement =
                     connection.prepareCall(MySQLQueries
                             .REQUEST_FIND_PAGE_BY_LANGUAGE_AND_STATUS_CALLABLE_STATEMENT)) {
            callableStatement.setLong("nlimit", page.getSize());
            callableStatement.setLong("noffset", page.getOffset());
            callableStatement.setLong("nstatus", status.getId());
            callableStatement.setString("slang", language);
            callableStatement.setString("ssearch", page.getSearch());
            callableStatement.setString("ssorting", page.getSorting());
            callableStatement.registerOutParameter("ncount", Types.BIGINT);
            ResultSet rs = callableStatement.executeQuery();
            page.setTotalElements(callableStatement.getLong("ncount"));
            while (rs.next()) {
                Request request = loadRequest(rs);
                requests.add(request);
            }
        } catch (SQLException e) {
            logger.error("find all SQL exception " + e.getMessage());
        }
        close();
        page.setContent(Optional.of(requests));
        return page;
    }
}
