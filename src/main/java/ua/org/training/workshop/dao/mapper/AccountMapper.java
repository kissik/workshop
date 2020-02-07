package ua.org.training.workshop.dao.mapper;

import ua.org.training.workshop.domain.Account;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountMapper implements ObjectMapper<Account> {
    @Override
    public Account extractFromResultSet(ResultSet rs, String prefix) throws SQLException {
        Account account = new Account();
        account.setId(rs.getLong(prefix + ".id"));
        account.setUsername(rs.getString(prefix + ".slogin"));
        account.setPassword(rs.getString(prefix + ".spassword"));
        account.setFirstName(rs.getString(prefix + ".sfirst_name"));
        account.setLastName(rs.getString(prefix + ".slast_name"));
        account.setFirstNameOrigin(rs.getString(prefix + ".sfirst_name_origin"));
        account.setLastNameOrigin(rs.getString(prefix + ".slast_name_origin"));
        account.setEmail(rs.getString(prefix + ".semail"));
        account.setPhone(rs.getString(prefix + ".sphone"));
        account.setEnabled(rs.getBoolean(prefix + ".benabled"));
        return account;
    }
}
