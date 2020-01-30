package ua.org.training.workshop.dao.mapper;

import ua.org.training.workshop.domain.Status;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class StatusMapper implements ObjectMapper<Status> {
    @Override
    public Status extractFromResultSet(ResultSet rs) throws SQLException {
        Status status = new Status();
        status.setId(rs.getLong("s.id"));
        status.setCode(rs.getString("s.scode"));
        status.setName(rs.getString("s.sname"));
        status.setClose(rs.getBoolean("s.bclose"));
        return status;
    }

    @Override
    public Status makeUnique(Map<Long, Status> cache, Status status) {
        return null;
    }
}
