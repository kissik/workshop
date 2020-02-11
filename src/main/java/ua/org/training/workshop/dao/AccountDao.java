package ua.org.training.workshop.dao;

import ua.org.training.workshop.domain.Account;

import java.sql.SQLException;
import java.util.Optional;

public interface AccountDao extends GenericDao<Account> {
    Long create(Account account, String password) throws SQLException;

    void update(Account account, String password);

    Optional<Account> findByUsername(String username);

    Optional<Account> findByPhone(String phone);

    Optional<Account> findByEmail(String email);

}
