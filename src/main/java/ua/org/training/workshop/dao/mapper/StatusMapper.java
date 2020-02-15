package ua.org.training.workshop.dao.mapper;

import ua.org.training.workshop.domain.Status;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StatusMapper implements ObjectMapper<Status> {
    @Override
    public Status extractFromResultSet(ResultSet rs) throws SQLException {
        Status status = new Status();
        status.setId(rs.getLong("s.id"));
        status.setCode(rs.getString("s.scode"));
        status.setName(rs.getString("s.sname"));
        status.setClosed(rs.getBoolean("s.bclosed"));
        return status;
    }
}
