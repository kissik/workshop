package ua.org.training.workshop.dao.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.dao.RoleDao;
import ua.org.training.workshop.dao.mapper.RoleMapper;
import ua.org.training.workshop.domain.Role;
import ua.org.training.workshop.exception.WorkshopErrors;
import ua.org.training.workshop.exception.WorkshopException;
import ua.org.training.workshop.utilities.Pageable;
import ua.org.training.workshop.utilities.UtilitiesClass;

import java.sql.*;
import java.util.*;

public class JDBCRoleDao implements RoleDao {
    static {
        new DOMConfigurator().doConfigure(UtilitiesClass.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }
    private static Logger logger = Logger.getLogger(JDBCRoleDao.class);
    private Connection connection;

    public JDBCRoleDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Role> findByCode(String code){
        try (PreparedStatement pst = connection.prepareStatement(MySQLQueries
                .ROLE_FIND_BY_CODE_QUERY)) {
            pst.setString(1, code);
            ResultSet rs = pst.executeQuery();
            RoleMapper roleMapper = new RoleMapper();
            Role role = null;
            while (rs.next()) {
                role = roleMapper
                        .extractFromResultSet(rs,UtilitiesClass.ROLE_QUERY_DEFAULT_PREFIX);
            }
            return Optional.ofNullable(role);
        } catch (SQLException e) {
            logger.debug("get account by username sql exception : " + e.getMessage());
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
    public Pageable getPage(Pageable page) {
        return null;
    }

    @Override
    public Optional<List<Role>> findAll() {
        Map<Long, Role> roles = new HashMap<>();
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(MySQLQueries
                    .ROLE_FIND_ALL_QUERY);
            RoleMapper roleMapper = new RoleMapper();
            while (rs.next()) {
                Role role = roleMapper
                        .extractFromResultSet(rs, UtilitiesClass.ROLE_QUERY_DEFAULT_PREFIX);
                roles.put(role.getId(), role);
            }
            } catch (SQLException e) {
            logger.error("find all roles error : " + e.getMessage());
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
            logger.error("cannot close connection : " + e.getMessage());
            throw new WorkshopException(WorkshopErrors.DATABASE_CONNECTION_ERROR);
        }
    }
}
