package ua.org.training.workshop.dao.mapper;

import ua.org.training.workshop.domain.Request;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class RequestMapper implements ObjectMapper<Request> {
    @Override
    public Request extractFromResultSet(ResultSet rs, String prefix) throws SQLException {
        Request request = new Request();
        request.setId(rs.getLong(prefix + ".id"));
        request.setTitle(rs.getString(prefix + ".stitle"));
        request.setDescription(rs.getString(prefix+".sdescription"));

        request.setClosed(rs.getBoolean(prefix+".bclosed"));
        request.setPrice(rs.getBigDecimal(prefix+".nprice"));
        request.setCause(rs.getString(prefix+".scause"));
        request.setLanguage(rs.getString(prefix+".slang"));

        return request;
    }

}
