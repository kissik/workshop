package ua.org.training.workshop.dao.mapper;

import ua.org.training.workshop.domain.HistoryRequest;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HistoryRequestMapper implements ObjectMapper<HistoryRequest> {
    @Override
    public HistoryRequest extractFromResultSet(ResultSet rs, String prefix) throws SQLException {
        HistoryRequest historyRequest = new HistoryRequest();
        historyRequest.setId(rs.getLong(prefix + ".id"));
        historyRequest.setTitle(rs.getString(prefix + ".stitle"));
        historyRequest.setDescription(rs.getString(prefix + ".sdescription"));
        historyRequest.setPrice(rs.getBigDecimal(prefix + ".nprice"));
        historyRequest.setCause(rs.getString(prefix + ".scause"));
        historyRequest.setLanguage(rs.getString(prefix + ".slang"));
        historyRequest.setReview(rs.getString(prefix + ".sreview"));
        historyRequest.setRating(rs.getLong(prefix + ".nrating"));
        return historyRequest;
    }
}
