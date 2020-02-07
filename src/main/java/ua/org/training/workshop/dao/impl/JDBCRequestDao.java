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
import ua.org.training.workshop.enums.WorkshopError;
import ua.org.training.workshop.exception.WorkshopException;
import ua.org.training.workshop.utility.ApplicationConstants;
import ua.org.training.workshop.utility.Page;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCRequestDao implements RequestDao {

    static {
        new DOMConfigurator().doConfigure(ApplicationConstants.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }

    private final static String REQUEST_FIND_PAGE_CALLABLE_STATEMENT =
            "CALL APP_PAGINATION_REQUEST_LIST (?, ?, ?, ?, ?);";
    private final static String REQUEST_FIND_PAGE_BY_AUTHOR_CALLABLE_STATEMENT =
            "CALL APP_PAGINATION_REQUEST_LIST_BY_AUTHOR (?, ?, ?, ?, ?, ?);";
    private final static String REQUEST_FIND_PAGE_BY_LANGUAGE_AND_AUTHOR_CALLABLE_STATEMENT =
            "CALL APP_PAGINATION_REQUEST_LIST_BY_LANGUAGE_AND_AUTHOR (?, ?, ?, ?, ?, ?, ?);";
    private final static String REQUEST_FIND_PAGE_BY_LANGUAGE_AND_STATUS_CALLABLE_STATEMENT =
            "CALL APP_PAGINATION_REQUEST_LIST_BY_LANGUAGE_AND_STATUS (?, ?, ?, ?, ?, ?, ?);";
    private final static String REQUEST_FIND_BY_ID_QUERY =
            " select * from request_list r " +
                    " inner join user_list a on r.nauthor = a.id " +
                    " inner join user_list u on r.nuser = u.id " +
                    " inner join status s on r.nstatus = s.id " +
                    " where r.id = ?";
    private final static String REQUEST_INSERT_PREPARE_STATEMENT =
            "CALL APP_INSERT_REQUEST_LIST (?,?,?,?,?,?,?,?);";
    private final static Logger LOGGER = Logger.getLogger(JDBCRequestDao.class);
    private Connection connection;

    public JDBCRequestDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Long create(Request request) throws WorkshopException {
        Long newId;
        try {
            newId = insertRequest(request);
            LOGGER.debug("request (id = "
                    + newId
                    + ") was created: " + request.getTitle());
        } catch (SQLException e) {
            LOGGER.error("SQL exception : " + e.getMessage());
            throw new WorkshopException(WorkshopError.REQUEST_CREATE_NEW_ERROR);
        }
        return newId;
    }

    @Override
    public void update(Request request) throws SQLException {
        updateStatus(request);
    }

    private void updateStatus(Request request) {
    }

    private Long insertRequest(Request request) throws SQLException {
        Long newId;
        try (CallableStatement callableStatement =
                     connection.prepareCall(
                             REQUEST_INSERT_PREPARE_STATEMENT)) {
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
        } catch (SQLException e) {
            LOGGER.error("create new request " + e.getMessage());
            throw e;
        }
        close();
        LOGGER.debug("new request : " + request.getTitle() + " was successfully created");
        return newId;
    }

    @Override
    public Optional<Request> findById(Long id) {
        Request request = null;
        try (PreparedStatement pst =
                     connection.prepareStatement(
                             REQUEST_FIND_BY_ID_QUERY)) {
            pst.setLong(1, id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                request = loadRequest(rs);
            }
        } catch (SQLException e) {
            LOGGER.debug("get request by " + id + " sql exception : " + e.getMessage());
        }
        close();
        return Optional.ofNullable(request);
    }

    private Request loadRequest(ResultSet rs) throws SQLException {
        RequestMapper requestMapper = new RequestMapper();
        AccountMapper accountMapper = new AccountMapper();
        StatusMapper statusMapper = new StatusMapper();
        Request request = requestMapper.extractFromResultSet(rs,
                ApplicationConstants.REQUEST_QUERY_DEFAULT_PREFIX);

        request.setAuthor(
                accountMapper.extractFromResultSet(rs,
                        ApplicationConstants.REQUEST_AUTHOR_QUERY_DEFAULT_PREFIX));
        request.setUser(
                accountMapper.extractFromResultSet(rs,
                        ApplicationConstants.REQUEST_USER_QUERY_DEFAULT_PREFIX));
        request.setStatus(
                statusMapper.extractFromResultSet(rs,
                        ApplicationConstants.STATUS_QUERY_DEFAULT_PREFIX));

        return request;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            LOGGER.error("cannot close connection" + e.getMessage());
            throw new WorkshopException(WorkshopError.DATABASE_CONNECTION_ERROR);
        }
    }

    public Page getPage(Page page) {
        List<Request> requests = new ArrayList<>();
        try (CallableStatement callableStatement =
                     connection.prepareCall(
                             REQUEST_FIND_PAGE_CALLABLE_STATEMENT)) {
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
            LOGGER.error("find all SQL exception " + e.getMessage());
        }
        close();
        page.setContent(Optional.of(requests));
        return page;
    }

    @Override
    public Page getPageByAuthor(Page page, Account author) {
        List<Request> requests = new ArrayList<>();
        try (CallableStatement callableStatement =
                     connection.prepareCall(
                             REQUEST_FIND_PAGE_BY_AUTHOR_CALLABLE_STATEMENT)) {
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
            LOGGER.error("find all SQL exception " + e.getMessage());
        }
        close();
        page.setContent(Optional.of(requests));
        return page;
    }

    @Override
    public Page getPageByLanguageAndAuthor(Page page, String language, Account author) {
        List<Request> requests = new ArrayList<>();
        try (CallableStatement callableStatement =
                     connection.prepareCall(
                             REQUEST_FIND_PAGE_BY_LANGUAGE_AND_AUTHOR_CALLABLE_STATEMENT)) {
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
            LOGGER.error("find all SQL exception " + e.getMessage());
        }
        close();
        page.setContent(Optional.of(requests));
        return page;
    }

    @Override
    public Page getPageByLanguageAndStatus(
            Page page,
            String language,
            Status status) {
        List<Request> requests = new ArrayList<>();
        try (CallableStatement callableStatement =
                     connection.prepareCall(
                             REQUEST_FIND_PAGE_BY_LANGUAGE_AND_STATUS_CALLABLE_STATEMENT)) {
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
            LOGGER.error("find all SQL exception " + e.getMessage());
        }
        close();
        page.setContent(Optional.of(requests));
        return page;
    }
}
