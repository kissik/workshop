package ua.org.training.workshop.dao.impl;

import ua.org.training.workshop.dao.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class JDBCDaoFactory extends DaoFactory {

    private DataSource dataSource = ConnectionPoolHolder.getDataSource();

    @Override
    public AccountDao createAccountDao() {
        return new JDBCAccountDao(getConnection());
    }
    @Override
    public RoleDao createRoleDao() {
        return new JDBCRoleDao(getConnection());
    }

    @Override
    public StatusDao createStatusDao() {
        return new JDBCStatusDao(getConnection());
    }
    @Override
    public RequestDao createRequestDao() {
        return new JDBCRequestDao(getConnection());
    }
    @Override
    public HistoryRequestDao createHistoryRequestDao() {
        return new JDBCHistoryRequestDao(getConnection());
    }

    private Connection getConnection(){
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
