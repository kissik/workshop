package ua.org.training.workshop.dao.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.dao.mapper.ObjectMapper;
import ua.org.training.workshop.utilities.UtilitiesClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCGenericDao<T> {
    static {
        new DOMConfigurator().doConfigure(UtilitiesClass.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }
    private static Logger logger = Logger.getLogger(JDBCAccountDao.class);

    public T findById(
            Long id,
            ObjectMapper<T> mapper,
            String query,
            Connection connection) {
        T entity = null;

        try (PreparedStatement pst =
                     connection.prepareStatement(query)) {
            pst.setLong(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                entity = mapper.extractFromResultSet(rs);
            }

        } catch (SQLException e) {
            logger.debug("get account by " + id + " sql exception : " + e.getMessage());
        }

        return entity;
    }
}
