package ua.org.training.workshop.dao.mapper;

import ua.org.training.workshop.domain.HistoryRequest;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HistoryRequestMapper implements ObjectMapper<HistoryRequest> {
    @Override
    public HistoryRequest extractFromResultSet(ResultSet rs) throws SQLException {
        HistoryRequest historyRequest = new HistoryRequest();
        historyRequest.setId(rs.getLong("h.id"));
        historyRequest.setTitle(rs.getString("h.stitle"));
        historyRequest.setDescription(rs.getString("h.sdescription"));
        historyRequest.setPrice(rs.getBigDecimal("h.nprice"));
        historyRequest.setCause(rs.getString("h.scause"));
        historyRequest.setLanguage(rs.getString("h.slang"));
        historyRequest.setReview(rs.getString("h.sreview"));
        historyRequest.setRating(rs.getLong("h.nrating"));
        return historyRequest;
    }
}
