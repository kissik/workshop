package ua.org.training.workshop.dao.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.dao.StatusDao;
import ua.org.training.workshop.dao.mapper.StatusMapper;
import ua.org.training.workshop.domain.Status;
import ua.org.training.workshop.exception.WorkshopErrors;
import ua.org.training.workshop.exception.WorkshopException;
import ua.org.training.workshop.utilities.Pageable;
import ua.org.training.workshop.utilities.UtilitiesClass;

import java.sql.*;
import java.util.*;

public class JDBCStatusDao implements StatusDao {

    static {
        new DOMConfigurator().doConfigure(UtilitiesClass.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }
    private static Logger logger = Logger.getLogger(JDBCStatusDao.class);
    private Connection connection;

    public JDBCStatusDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Status> findByCode(String code){
        Status status = null;
        try (PreparedStatement pst = connection.prepareStatement(MySQLQueries
                .STATUS_FIND_BY_CODE_QUERY)) {
            pst.setString(1, code);
            ResultSet rs = pst.executeQuery();
            StatusMapper statusMapper = new StatusMapper();
            while (rs.next()) {
                status = statusMapper
                        .extractFromResultSet(rs,
                                UtilitiesClass.STATUS_QUERY_DEFAULT_PREFIX);
                status.setNextStatuses(
                        findNextStatusesForCurrentStatusById(status.getId()));
            }
        } catch (SQLException e) {
            logger.debug("get status by code sql exception : " + e.getMessage());
        }
        close();
        return Optional.ofNullable(status);
    }

    private List<Status> findNextStatusesForCurrentStatusById(Long id){
        Map<Long, Status> statuses = new HashMap<>();
        try (PreparedStatement pst = connection.prepareStatement(MySQLQueries
                .STATUS_FIND_NEXT_STATUSES_BY_CURRENT_ID_STATUS_QUERY)) {
            pst.setLong(1, id);
            ResultSet rs = pst.executeQuery();
            StatusMapper statusMapper = new StatusMapper();
            while (rs.next()) {
                Status status = statusMapper
                        .extractFromResultSet(rs,
                                UtilitiesClass.STATUS_QUERY_DEFAULT_PREFIX);
                statuses.put(status.getId(), status);
            }
        } catch (SQLException e) {
            logger.debug("find next statuses for status by id" + e.getMessage());
        }
        return new ArrayList<>(statuses.values());
    }

    @Override
    public List<Status> getStatusList() {
        return null;
    }

    @Override
    public void create(Status entity) {

    }

    @Override
    public Optional<Status> findById(Long id) {
        Status status = null;
        try (PreparedStatement pst = connection.prepareStatement(MySQLQueries
                .STATUS_FIND_BY_ID_QUERY)) {
            pst.setLong(1, id);
            ResultSet rs = pst.executeQuery();
            StatusMapper statusMapper = new StatusMapper();
            while (rs.next()) {
                status = statusMapper
                        .extractFromResultSet(rs,
                                UtilitiesClass.STATUS_QUERY_DEFAULT_PREFIX);
                status.setNextStatuses(
                        findNextStatusesForCurrentStatusById(status.getId()));
            }
        } catch (SQLException e) {
            logger.debug("get status by id sql exception : " + e.getMessage());
        }
        close();
        return Optional.ofNullable(status);
    }

    @Override
    public Pageable getPage(Pageable page) {
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
            logger.error("cannot close connection : " + e.getMessage());
            throw new WorkshopException(WorkshopErrors.DATABASE_CONNECTION_ERROR);
        }
    }
}
