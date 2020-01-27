package ua.org.training.workshop.dao.impl;

import ua.org.training.workshop.dao.AccountDao;
import ua.org.training.workshop.dao.DaoFactory;
import ua.org.training.workshop.dao.RoleDao;

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

    private Connection getConnection(){
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
