package ua.org.training.workshop.dao.mapper;

import ua.org.training.workshop.domain.Request;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class RequestMapper implements ObjectMapper<Request> {
    @Override
    public Request extractFromResultSet(ResultSet rs) throws SQLException {
        Request request = new Request();
        request.setId(rs.getLong("r.id"));
        request.setTitle(rs.getString("r.stitle"));
        request.setDescription(rs.getString("r.sdescription"));

        request.setClosed(rs.getBoolean("r.bclosed"));
        request.setPrice(rs.getBigDecimal("r.nprice"));
        request.setCause(rs.getString("r.scause"));
        request.setLanguage(rs.getString("r.slang"));

        return request;
    }

    @Override
    public Request makeUnique(Map<Long, Request> cache, Request request) {
        cache.putIfAbsent(request.getId(), request);
        return cache.get(request.getId());
    }
}
