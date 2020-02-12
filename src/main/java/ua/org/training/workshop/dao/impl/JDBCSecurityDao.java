package ua.org.training.workshop.dao.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.dao.SecurityDao;
import ua.org.training.workshop.enums.WorkshopError;
import ua.org.training.workshop.exception.WorkshopException;
import ua.org.training.workshop.utility.ApplicationConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCSecurityDao implements SecurityDao {

    static {
        new DOMConfigurator().doConfigure(ApplicationConstants.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }

    private final static String FIND_PASSWORD_BY_USERNAME_QUERY =
            " select spassword from user_list where slogin = ? ";

    private final static Logger LOGGER = Logger.getLogger(JDBCSecurityDao.class);

    private final static Connection connection = getConnection();

    private static Connection getConnection() {
        try {
            return ConnectionPoolHolder.getDataSource().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String findPasswordByUsername(String username) {
        String password = "";
        try (PreparedStatement pst = connection.prepareStatement(
                FIND_PASSWORD_BY_USERNAME_QUERY)) {
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                password = rs.getString("spassword");
            }
        } catch (SQLException e) {
            LOGGER.debug("get password by " + username + " sql exception : " + e.getMessage());
        }
        return password;
    }

    private void close() throws WorkshopException {
        try {
            connection.close();
        } catch (SQLException e) {
            LOGGER.error("cannot close connection : " + e.getMessage());
            throw new WorkshopException(WorkshopError.DATABASE_CONNECTION_ERROR);
        }
    }
}
