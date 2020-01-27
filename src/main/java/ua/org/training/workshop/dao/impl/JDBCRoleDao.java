package ua.org.training.workshop.dao.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.dao.RoleDao;
import ua.org.training.workshop.dao.mapper.RoleMapper;
import ua.org.training.workshop.domain.Role;
import ua.org.training.workshop.utilities.Pageable;

import java.sql.*;
import java.util.*;

public class JDBCRoleDao implements RoleDao {
    static {
        new DOMConfigurator().doConfigure("src/log4j.xml", LogManager.getLoggerRepository());
    }
    private static Logger logger = Logger.getLogger(JDBCRoleDao.class);
    private static final String FIND_BY_CODE_QUERY =
            " select * from role r where scode = ?";
    private static final String FIND_ALL_ROLES =
            " select * from role r";
    private Connection connection;

    public JDBCRoleDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Role> findByCode(String code){

        try (PreparedStatement pst = connection.prepareStatement(FIND_BY_CODE_QUERY)) {
            pst.setString(1, code);
            ResultSet rs = pst.executeQuery();

            RoleMapper roleMapper = new RoleMapper();
            Role role = null;
            while (rs.next()) {
                role = roleMapper
                        .extractFromResultSet(rs);
            }
            return Optional.ofNullable(role);

        } catch (SQLException e) {
            logger.debug("get account by username sql exception : " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Role> getRoleList() {
        return null;
    }

    @Override
    public void create(Role entity) {

    }

    @Override
    public Optional<Role> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<List<Role>> findAll() {

        Map<Long, Role> roles = new HashMap<>();

        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(FIND_ALL_ROLES);

            RoleMapper roleMapper = new RoleMapper();

            while (rs.next()) {
                Role role = roleMapper
                        .extractFromResultSet(rs);
                roles.put(role.getId(), role);
            }
            connection.close();
            } catch (SQLException e) {
            logger.error("find all roles error : " + e.getMessage());
            }
        return Optional.of(new ArrayList<>(roles.values()));
    }

    @Override
    public String getPage(Pageable page) {
        return "";
    }

    @Override
    public void update(Role entity) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
