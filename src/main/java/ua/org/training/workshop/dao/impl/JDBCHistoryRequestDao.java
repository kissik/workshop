package ua.org.training.workshop.dao.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.dao.HistoryRequestDao;
import ua.org.training.workshop.dao.mapper.AccountMapper;
import ua.org.training.workshop.dao.mapper.HistoryRequestMapper;
import ua.org.training.workshop.dao.mapper.StatusMapper;
import ua.org.training.workshop.domain.Account;
import ua.org.training.workshop.domain.HistoryRequest;
import ua.org.training.workshop.exception.WorkshopErrors;
import ua.org.training.workshop.exception.WorkshopException;
import ua.org.training.workshop.utilities.Pageable;
import ua.org.training.workshop.utilities.UtilitiesClass;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCHistoryRequestDao implements HistoryRequestDao {
    static {
        new DOMConfigurator().doConfigure(UtilitiesClass.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }
    private static Logger logger = Logger.getLogger(JDBCHistoryRequestDao.class);

    private Connection connection;
    public JDBCHistoryRequestDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Long create(HistoryRequest historyRequest) throws WorkshopException {
        return 0L;
    }

    @Override
    public void update(HistoryRequest historyRequest) throws SQLException{
    }

    private void updateStatus(HistoryRequest historyRequest) {
    }

    private Long insertRequest(HistoryRequest historyRequest) throws SQLException{
        return 0L;
    }

    @Override
    public Optional<HistoryRequest> findById(Long id) {
        HistoryRequest historyRequest = null;
        try (PreparedStatement pst =
                     connection.prepareStatement(MySQLQueries
                             .HISTORY_REQUEST_FIND_BY_ID_QUERY)) {
            pst.setLong(1, id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                historyRequest = loadHistoryRequest(rs);
            }
        } catch (SQLException e) {
            logger.debug("get request by " + id + " sql exception : " + e.getMessage());
        }
        close();
        return Optional.ofNullable(historyRequest);
    }

    private HistoryRequest loadHistoryRequest(ResultSet rs) throws SQLException {
        HistoryRequestMapper historyRequestMapper = new HistoryRequestMapper();
        AccountMapper accountMapper = new AccountMapper();
        StatusMapper statusMapper = new StatusMapper();
        HistoryRequest historyRequest = historyRequestMapper.extractFromResultSet(rs,
                UtilitiesClass.HISTORY_REQUEST_QUERY_DEFAULT_PREFIX);

        historyRequest.setAuthor(
                accountMapper.extractFromResultSet(rs,
                        UtilitiesClass.HISTORY_REQUEST_AUTHOR_QUERY_DEFAULT_PREFIX));
        historyRequest.setUser(
                accountMapper.extractFromResultSet(rs,
                        UtilitiesClass.HISTORY_REQUEST_USER_QUERY_DEFAULT_PREFIX));
        historyRequest.setStatus(
                statusMapper.extractFromResultSet(rs,
                        UtilitiesClass.STATUS_QUERY_DEFAULT_PREFIX));

        return historyRequest;
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
        List<HistoryRequest> historyRequests = new ArrayList<>();
        try (CallableStatement callableStatement =
                     connection.prepareCall(MySQLQueries
                             .HISTORY_REQUEST_FIND_PAGE_CALLABLE_STATEMENT)) {
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
            logger.error("find all SQL exception " + e.getMessage());
        }
        close();
        page.setContent(Optional.of(historyRequests));
        return page;
    }

    @Override
    public Pageable getPageByLanguageAndAuthor(Pageable page, String language, Account author) {
        List<HistoryRequest> historyRequests = new ArrayList<>();
        try (CallableStatement callableStatement =
                     connection.prepareCall(MySQLQueries
                             .HISTORY_REQUEST_FIND_PAGE_BY_LANGUAGE_AND_AUTHOR_CALLABLE_STATEMENT)) {
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
                HistoryRequest historyRequest = loadHistoryRequest(rs);
                historyRequests.add(historyRequest);
            }
        } catch (SQLException e) {
            logger.error("find all SQL exception " + e.getMessage());
        }
        close();
        page.setContent(Optional.of(historyRequests));
        return page;
    }
}
