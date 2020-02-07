package ua.org.training.workshop.dao.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.dao.RoleDao;
import ua.org.training.workshop.dao.mapper.RoleMapper;
import ua.org.training.workshop.domain.Role;
import ua.org.training.workshop.enums.WorkshopError;
import ua.org.training.workshop.exception.WorkshopException;
import ua.org.training.workshop.utility.ApplicationConstants;
import ua.org.training.workshop.utility.Page;

import java.sql.*;
import java.util.*;

public class JDBCRoleDao implements RoleDao {

    static {
        new DOMConfigurator().doConfigure(ApplicationConstants.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }

    private final static Logger LOGGER = Logger.getLogger(JDBCRoleDao.class);
    private final static String ROLE_FIND_BY_CODE_QUERY =
            " select * from role r where scode = ?";
    private final static String ROLE_FIND_ALL_QUERY =
            " select * from role r";

    private Connection connection;

    public JDBCRoleDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Role> findByCode(String code) {
        try (PreparedStatement pst = connection.prepareStatement(
                ROLE_FIND_BY_CODE_QUERY)) {
            pst.setString(1, code);
            ResultSet rs = pst.executeQuery();
            RoleMapper roleMapper = new RoleMapper();
            Role role = null;
            while (rs.next()) {
                role = roleMapper
                        .extractFromResultSet(rs, ApplicationConstants.ROLE_QUERY_DEFAULT_PREFIX);
            }
            return Optional.ofNullable(role);
        } catch (SQLException e) {
            LOGGER.debug("get account by username sql exception : " + e.getMessage());
        }
        close();
        return Optional.empty();
    }

    @Override
    public Long create(Role entity) {
        return 0L;
    }

    @Override
    public Optional<Role> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Page getPage(Page page) {
        return null;
    }

    @Override
    public Optional<List<Role>> findAll() {
        Map<Long, Role> roles = new HashMap<>();
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(
                    ROLE_FIND_ALL_QUERY);
            RoleMapper roleMapper = new RoleMapper();
            while (rs.next()) {
                Role role = roleMapper
                        .extractFromResultSet(rs, ApplicationConstants.ROLE_QUERY_DEFAULT_PREFIX);
                roles.put(role.getId(), role);
            }
        } catch (SQLException e) {
            LOGGER.error("find all roles error : " + e.getMessage());
        }
        close();
        return Optional.of(new ArrayList<>(roles.values()));
    }

    @Override
    public void update(Role entity) {

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
