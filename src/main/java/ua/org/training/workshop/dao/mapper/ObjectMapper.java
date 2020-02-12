package ua.org.training.workshop.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ObjectMapper<T> {

    T extractFromResultSet(ResultSet rs) throws SQLException;

}

