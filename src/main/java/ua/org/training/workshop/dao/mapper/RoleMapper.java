package ua.org.training.workshop.dao.mapper;

import ua.org.training.workshop.domain.Role;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleMapper implements ObjectMapper<Role> {
    @Override
    public Role extractFromResultSet(ResultSet rs, String prefix) throws SQLException {
        Role role = new Role();
        role.setId(rs.getLong(prefix + ".id"));
        role.setCode(rs.getString(prefix + ".scode"));
        role.setName(rs.getString(prefix + ".sname"));
        return role;
    }
}
