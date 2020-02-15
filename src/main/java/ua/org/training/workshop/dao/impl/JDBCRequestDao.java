package ua.org.training.workshop.dao.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.dao.RequestDao;
import ua.org.training.workshop.dao.mapper.RequestMapper;
import ua.org.training.workshop.domain.Request;
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
                    " where r.id = ?";
    private final static String DELETE_REQUEST_BY_ID_QUERY =
            " delete from request_list " +
                    " where id = ?";
    private final static String REQUEST_INSERT_CALLABLE_STATEMENT =
            "CALL APP_INSERT_REQUEST_LIST (?,?,?,?,?,?,?,?);";
    private final static String REQUEST_UPDATE_CALLABLE_STATEMENT =
            "CALL APP_UPDATE_REQUEST (?, ?, ?, ?, ?, ?);";
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
        LOGGER.info("Start transaction! --------------------------------> ");
        connection.setAutoCommit(false);
        try {
            updateRequest(request);
            if (request.isClosed())
                deleteRequest(request.getId());
            connection.commit();
        } catch (SQLException e) {
            LOGGER.error("update request error : " + e.getMessage());
            LOGGER.info("Transaction was rollback! <--------------------------------");
            connection.rollback();
            close();
            throw new WorkshopException(WorkshopError.REQUEST_UPDATE_ERROR);
        }
        LOGGER.info("Transaction was successfully committed! <--------------------------------");
        close();
    }

    private void updateRequest(Request request) throws SQLException {
        try (CallableStatement callableStatement =
                     connection.prepareCall(
                             REQUEST_UPDATE_CALLABLE_STATEMENT)) {
            callableStatement.setLong("nuser", request.getUser().getId());
            callableStatement.setLong("nstatus", request.getStatus().getId());
            callableStatement.setString("scause", request.getCause());
            callableStatement.setBigDecimal("nprice", request.getPrice());
            callableStatement.setLong("nid", request.getId());
            callableStatement.setBoolean("bclosed", request.isClosed());
            callableStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("update request " + e.getMessage());
            throw e;
        }
        LOGGER.debug("Request " + request.getTitle() + " was successfully updated!");
    }

    private Long insertRequest(Request request) throws SQLException {
        Long newId;
        try (CallableStatement callableStatement =
                     connection.prepareCall(
                             REQUEST_INSERT_CALLABLE_STATEMENT)) {
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
        LOGGER.info("try to find request by id");
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
        return requestMapper.extractFromResultSet(rs);
    }

    private void deleteRequest(Long id) {
        LOGGER.info("try to delete request by id");
        try (PreparedStatement pst =
                     connection.prepareStatement(
                             DELETE_REQUEST_BY_ID_QUERY)) {
            pst.setLong(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            LOGGER.debug("delete request by " + id + " sql exception : " + e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        deleteRequest(id);
        close();
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

    public Page<Request> getPage(Page<Request> page) {
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
    public Page<Request> getPageByAuthor(Page<Request> page, Long authorId) {
        List<Request> requests = new ArrayList<>();
        try (CallableStatement callableStatement =
                     connection.prepareCall(
                             REQUEST_FIND_PAGE_BY_AUTHOR_CALLABLE_STATEMENT)) {
            callableStatement.setLong("nlimit", page.getSize());
            callableStatement.setLong("noffset", page.getOffset());
            callableStatement.setLong("nauthor", authorId);
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
    public Page<Request> getPageByLanguageAndAuthor(Page<Request> page, String language, Long authorId) {
        List<Request> requests = new ArrayList<>();
        try (CallableStatement callableStatement =
                     connection.prepareCall(
                             REQUEST_FIND_PAGE_BY_LANGUAGE_AND_AUTHOR_CALLABLE_STATEMENT)) {
            callableStatement.setLong("nlimit", page.getSize());
            callableStatement.setLong("noffset", page.getOffset());
            callableStatement.setLong("nauthor", authorId);
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
    public Page<Request> getPageByLanguageAndStatus(
            Page<Request> page,
            String language,
            Long statusId) {
        List<Request> requests = new ArrayList<>();
        try (CallableStatement callableStatement =
                     connection.prepareCall(
                             REQUEST_FIND_PAGE_BY_LANGUAGE_AND_STATUS_CALLABLE_STATEMENT)) {
            callableStatement.setLong("nlimit", page.getSize());
            callableStatement.setLong("noffset", page.getOffset());
            callableStatement.setLong("nstatus", statusId);
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
