package ua.org.training.workshop.dao.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.dao.StatusDao;
import ua.org.training.workshop.dao.mapper.StatusMapper;
import ua.org.training.workshop.domain.Status;
import ua.org.training.workshop.enums.WorkshopError;
import ua.org.training.workshop.exception.WorkshopException;
import ua.org.training.workshop.utility.ApplicationConstants;
import ua.org.training.workshop.utility.Page;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCStatusDao implements StatusDao {

    static {
        new DOMConfigurator().doConfigure(ApplicationConstants.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }

    private final static String STATUS_FIND_NEXT_STATUSES_BY_CURRENT_ID_STATUS_QUERY =
            " select * from next_statuses ns " +
                    " inner join status s " +
                    " on ns.nnext_status = s.id " +
                    " where ns.nstatus = ? ";

    private static final String STATUS_FIND_BY_HISTORY_REQUEST_ID_QUERY =
            " select s.* from status s" +
                    " inner join history_request_list h on h.nstatus = s.id" +
                    " where h.id = ?";
    private static final String STATUS_FIND_BY_REQUEST_ID_QUERY =
            " select s.* from status s" +
                    " inner join request_list r on r.nstatus = s.id" +
                    " where r.id = ?";
    private final static String STATUS_FIND_BY_CODE_QUERY =
            " select * from status s where scode = ?";
    private final static String STATUS_FIND_BY_ID_QUERY =
            " select * from status s where id = ?";
    private final static String STATUS_FIND_ALL_QUERY =
            " select * from status s";

    private final static Logger LOGGER = Logger.getLogger(JDBCStatusDao.class);

    private Connection connection;

    public JDBCStatusDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Status> findByCode(String code) {
        Status status = null;
        try (PreparedStatement pst = connection.prepareStatement(
                STATUS_FIND_BY_CODE_QUERY)) {
            pst.setString(1, code);
            ResultSet rs = pst.executeQuery();
            StatusMapper statusMapper = new StatusMapper();
            if (rs.next()) {
                status = statusMapper
                        .extractFromResultSet(rs);
            }
        } catch (SQLException e) {
            LOGGER.debug("get status by code sql exception : " + e.getMessage());
        }
        close();
        return Optional.ofNullable(status);
    }

    @Override
    public Optional<Status> findByRequestId(Long requestId) {
        Status status = null;
        try (PreparedStatement pst = connection.prepareStatement(
                STATUS_FIND_BY_REQUEST_ID_QUERY)) {
            pst.setLong(1, requestId);
            ResultSet rs = pst.executeQuery();
            StatusMapper statusMapper = new StatusMapper();
            if (rs.next()) {
                status = statusMapper
                        .extractFromResultSet(rs);
            }
        } catch (SQLException e) {
            LOGGER.debug("get status by id sql exception : " + e.getMessage());
        }
        close();
        return Optional.ofNullable(status);
    }

    @Override
    public Optional<Status> findByHistoryRequestId(Long historyRequestId) {
        Status status = null;
        try (PreparedStatement pst = connection.prepareStatement(
                STATUS_FIND_BY_HISTORY_REQUEST_ID_QUERY)) {
            pst.setLong(1, historyRequestId);
            ResultSet rs = pst.executeQuery();
            StatusMapper statusMapper = new StatusMapper();
            if (rs.next()) {
                status = statusMapper
                        .extractFromResultSet(rs);
            }
        } catch (SQLException e) {
            LOGGER.debug("get status by id sql exception : " + e.getMessage());
        }
        close();
        return Optional.ofNullable(status);
    }

    public Optional<List<Status>> findNextStatusesForCurrentStatusById(Long id) {
        List<Status> statuses = new ArrayList<>();
        try (PreparedStatement pst = connection.prepareStatement(
                STATUS_FIND_NEXT_STATUSES_BY_CURRENT_ID_STATUS_QUERY)) {
            pst.setLong(1, id);
            ResultSet rs = pst.executeQuery();
            StatusMapper statusMapper = new StatusMapper();
            while (rs.next()) {
                statuses.add(statusMapper
                        .extractFromResultSet(rs));
            }
        } catch (SQLException e) {
            LOGGER.debug("find next statuses for status by id" + e.getMessage());
        }
        close();
        return Optional.of(statuses);
    }

    @Override
    public Long create(Status entity) {
        return 0L;
    }

    @Override
    public Optional<Status> findById(Long id) {
        Status status = null;
        try (PreparedStatement pst = connection.prepareStatement(
                STATUS_FIND_BY_ID_QUERY)) {
            pst.setLong(1, id);
            ResultSet rs = pst.executeQuery();
            StatusMapper statusMapper = new StatusMapper();
            if (rs.next()) {
                status = statusMapper
                        .extractFromResultSet(rs);
            }
        } catch (SQLException e) {
            LOGGER.debug("get status by id sql exception : " + e.getMessage());
        }
        close();
        return Optional.ofNullable(status);
    }

    @Override
    public Page<Status> getPage(Page<Status> page) {
        return null;
    }

    @Override
    public void update(Status entity) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void close() throws WorkshopException {
        try {
            connection.close();
        } catch (SQLException e) {
            LOGGER.error("cannot close connection : " + e.getMessage());
            throw new WorkshopException(WorkshopError.DATABASE_CONNECTION_ERROR);
        }
    }
}
