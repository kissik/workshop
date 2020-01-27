package ua.org.training.workshop.dao.mapper;

import ua.org.training.workshop.domain.Account;
import ua.org.training.workshop.domain.Role;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class RoleMapper implements ObjectMapper<Role> {
    @Override
    public Role extractFromResultSet(ResultSet rs) throws SQLException {
        Role role = new Role();
        role.setId(rs.getLong("r.id"));
        role.setCode(rs.getString("r.scode"));
        role.setName(rs.getString("r.sname"));
        return role;
    }

    @Override
    public Role makeUnique(Map<Long, Role> cache, Role role) {

        return null;
    }
}
