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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCRoleDao implements RoleDao {

    static {
        new DOMConfigurator().doConfigure(ApplicationConstants.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }

    private final static Logger LOGGER = Logger.getLogger(JDBCRoleDao.class);
    private final static String ROLE_FIND_BY_CODE_QUERY =
            " select * from role r where scode = ?";
    private final static String ROLE_FIND_ALL_QUERY =
            " select * from role r";
    private final static String FIND_ROLES_BY_ACCOUNT_ID_QUERY =
            " select * from user_role ur" +
                    " inner join role r " +
                    " on ur.nrole = r.id" +
                    " where ur.nuser = ? ";

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
            if (rs.next()) {
                role = roleMapper
                        .extractFromResultSet(rs);
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
    public Page<Role> getPage(Page<Role> page) {
        return null;
    }

    @Override
    public Optional<List<Role>> findAll() {
        List<Role> roles = new ArrayList<>();
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(
                    ROLE_FIND_ALL_QUERY);
            RoleMapper roleMapper = new RoleMapper();
            while (rs.next()) {
                roles.add(roleMapper
                        .extractFromResultSet(rs));
            }
        } catch (SQLException e) {
            LOGGER.error("find all roles error : " + e.getMessage());
        }
        close();
        return Optional.of(roles);
    }

    @Override
    public Optional<List<Role>> findRolesByAccountId(Long accountId) {
        List<Role> roles = new ArrayList<>();
        try (PreparedStatement pst = connection.prepareStatement(
                FIND_ROLES_BY_ACCOUNT_ID_QUERY)) {
            pst.setLong(1, accountId);
            ResultSet rs = pst.executeQuery();
            RoleMapper roleMapper = new RoleMapper();
            while (rs.next()) {
                roles.add(roleMapper
                        .extractFromResultSet(rs));
            }
        } catch (SQLException e) {
            LOGGER.error("find roles for user by id" + e.getMessage());
        }
        close();
        return Optional.of(roles);
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
