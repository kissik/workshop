package ua.org.training.workshop.dao.impl;

import org.apache.commons.dbcp.BasicDataSource;
import ua.org.training.workshop.enums.WorkshopError;
import ua.org.training.workshop.exception.WorkshopException;
import ua.org.training.workshop.utility.ApplicationConstants;
import ua.org.training.workshop.utility.Utility;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

class ConnectionPoolHolder {
    private final static String MYSQL_URL_CONNECTION_STRING =
            Utility.getApplicationProperty("mysql.url.connection.string");
    private final static String MYSQL_USER = Utility.getApplicationProperty("mysql.user");
    private final static String MYSQL_PASSWORD = Utility.getApplicationProperty("mysql.password");
    private final static int MYSQL_MIN_IDLE =
            Utility.tryParseInteger(Utility.getApplicationProperty("mysql.min.idle"),
                    ApplicationConstants.MYSQL_DEFAULT_MIN_IDLE);
    private final static int MYSQL_MAX_IDLE =
            Utility.tryParseInteger(Utility.getApplicationProperty("mysql.max.idle"),
                    ApplicationConstants.MYSQL_DEFAULT_MAX_IDLE);
    private final static int MYSQL_MAX_OPEN_PREPARED_STATEMENTS =
            Utility.tryParseInteger(Utility.getApplicationProperty("mysql.max.open.prepared.statements"),
                    ApplicationConstants.MYSQL_DEFAULT_MAX_OPEN_PREPARED_STATEMENTS);
    private static DataSource dataSource;

    static {
        try{
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl(MYSQL_URL_CONNECTION_STRING);
        ds.setUsername(MYSQL_USER);
        ds.setPassword(MYSQL_PASSWORD);
        ds.setMinIdle(MYSQL_MIN_IDLE);
        ds.setMaxIdle(MYSQL_MAX_IDLE);
        ds.setMaxOpenPreparedStatements(MYSQL_MAX_OPEN_PREPARED_STATEMENTS);
        dataSource = ds;
        }
        catch(Exception ignored){

        }

    }

    public static DataSource getDataSource(){
        return dataSource;
    }

    public static Connection getConnection() throws WorkshopException{
        try {
            return dataSource.getConnection();
        }catch (SQLException e) {
            throw new WorkshopException(WorkshopError.DATABASE_CONNECTION_ERROR);
        }
    }
}
