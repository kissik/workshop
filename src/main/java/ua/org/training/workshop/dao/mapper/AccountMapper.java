package ua.org.training.workshop.dao.mapper;

import ua.org.training.workshop.domain.Account;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountMapper implements ObjectMapper<Account> {
    @Override
    public Account extractFromResultSet(ResultSet rs) throws SQLException {
        Account account = new Account();
        account.setId(rs.getLong("u.id"));
        account.setUsername(rs.getString("u.slogin"));
        account.setFirstName(rs.getString("u.sfirst_name"));
        account.setLastName(rs.getString("u.slast_name"));
        account.setFirstNameOrigin(rs.getString("u.sfirst_name_origin"));
        account.setLastNameOrigin(rs.getString("u.slast_name_origin"));
        account.setEmail(rs.getString("u.semail"));
        account.setPhone(rs.getString("u.sphone"));
        account.setEnabled(rs.getBoolean("u.benabled"));
        return account;
    }
}
