package ua.org.training.workshop.dao.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.dao.HistoryRequestDao;
import ua.org.training.workshop.dao.mapper.HistoryRequestMapper;
import ua.org.training.workshop.domain.HistoryRequest;
import ua.org.training.workshop.enums.WorkshopError;
import ua.org.training.workshop.exception.WorkshopException;
import ua.org.training.workshop.utility.ApplicationConstants;
import ua.org.training.workshop.utility.Page;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCHistoryRequestDao implements HistoryRequestDao {

    static {
        new DOMConfigurator().doConfigure(ApplicationConstants.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }

    private final static String HISTORY_REQUEST_FIND_PAGE_BY_LANGUAGE_AND_AUTHOR_CALLABLE_STATEMENT =
            "CALL APP_PAGINATION_HISTORY_REQUEST_LIST_BY_LANGUAGE_AND_AUTHOR (?, ?, ?, ?, ?, ?, ?);";
    private final static String HISTORY_REQUEST_FIND_PAGE_CALLABLE_STATEMENT =
            "CALL APP_PAGINATION_HISTORY_REQUEST_LIST (?, ?, ?, ?, ?);";
    private final static String HISTORY_REQUEST_FIND_BY_ID_QUERY =
            " select * from history_request_list h " +
                    " where h.id = ?";
    private final static String HISTORY_REQUEST_UPDATE_CALLABLE_STATEMENT =
            "CALL APP_UPDATE_HISTORY_REQUEST (?, ?, ?);";
    ;
    private final static Logger LOGGER = Logger.getLogger(JDBCHistoryRequestDao.class);

    private Connection connection;

    public JDBCHistoryRequestDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Long create(HistoryRequest historyRequest) throws WorkshopException {
        return 0L;
    }

    @Override
    public void update(HistoryRequest historyRequest) throws SQLException {
        try (CallableStatement callableStatement =
                     connection.prepareCall(
                             HISTORY_REQUEST_UPDATE_CALLABLE_STATEMENT)) {
            callableStatement.setString("sreview", historyRequest.getReview());
            callableStatement.setBigDecimal("nrating", historyRequest.getRating());
            callableStatement.setLong("nid", historyRequest.getId());
            callableStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("update history request " + e.getMessage());
            throw e;
        }
        LOGGER.debug("History request " + historyRequest.getTitle() + " was successfully updated!");
    }

    private void updateStatus(HistoryRequest historyRequest) {
    }

    private Long insertRequest(HistoryRequest historyRequest) throws SQLException {
        return 0L;
    }

    @Override
    public Optional<HistoryRequest> findById(Long id) {
        HistoryRequest historyRequest = null;
        try (PreparedStatement pst =
                     connection.prepareStatement(
                             HISTORY_REQUEST_FIND_BY_ID_QUERY)) {
            pst.setLong(1, id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                historyRequest = loadHistoryRequest(rs);
            }
        } catch (SQLException e) {
            LOGGER.debug("get request by " + id + " sql exception : " + e.getMessage());
        }
        close();
        return Optional.ofNullable(historyRequest);
    }

    private HistoryRequest loadHistoryRequest(ResultSet rs) throws SQLException {
        HistoryRequestMapper historyRequestMapper = new HistoryRequestMapper();
        return historyRequestMapper.extractFromResultSet(rs);
    }

    @Override
    public void delete(Long id) {
        throw new UnsupportedOperationException("It is strictly prohibited deleting history requests!");
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

    public Page<HistoryRequest> getPage(Page<HistoryRequest> page) {
        List<HistoryRequest> historyRequests = new ArrayList<>();
        try (CallableStatement callableStatement =
                     connection.prepareCall(
                             HISTORY_REQUEST_FIND_PAGE_CALLABLE_STATEMENT)) {
            callableStatement.setLong("nlimit", page.getSize());
            callableStatement.setLong("noffset", page.getOffset());
            callableStatement.setString("ssearch", page.getSearch());
            callableStatement.setString("ssorting", page.getSorting());
            callableStatement.registerOutParameter("ncount", Types.BIGINT);
            ResultSet rs = callableStatement.executeQuery();
            page.setTotalElements(callableStatement.getLong("ncount"));
            while (rs.next()) {
                HistoryRequest historyRequest = loadHistoryRequest(rs);
                historyRequests.add(historyRequest);
            }
        } catch (SQLException e) {
            LOGGER.error("find all SQL exception " + e.getMessage());
        }
        close();
        page.setContent(Optional.of(historyRequests));
        return page;
    }

    @Override
    public Page<HistoryRequest> getPageByLanguageAndAuthor(Page<HistoryRequest> page, String language, Long authorId) {
        List<HistoryRequest> historyRequests = new ArrayList<>();
        try (CallableStatement callableStatement =
                     connection.prepareCall(
                             HISTORY_REQUEST_FIND_PAGE_BY_LANGUAGE_AND_AUTHOR_CALLABLE_STATEMENT)) {
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
                HistoryRequest historyRequest = loadHistoryRequest(rs);
                historyRequests.add(historyRequest);
            }
        } catch (SQLException e) {
            LOGGER.error("find all SQL exception " + e.getMessage());
        }
        close();
        page.setContent(Optional.of(historyRequests));
        return page;
    }
}
