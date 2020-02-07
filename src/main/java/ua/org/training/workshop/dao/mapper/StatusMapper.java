package ua.org.training.workshop.dao.mapper;

import ua.org.training.workshop.domain.Status;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StatusMapper implements ObjectMapper<Status> {
    @Override
    public Status extractFromResultSet(ResultSet rs, String prefix) throws SQLException {
        Status status = new Status();
        status.setId(rs.getLong(prefix + ".id"));
        status.setCode(rs.getString(prefix + ".scode"));
        status.setName(rs.getString(prefix + ".sname"));
        status.setClose(rs.getBoolean(prefix + ".bclose"));
        return status;
    }
}
