package ua.org.training.workshop.dao;

import ua.org.training.workshop.domain.Account;

import java.util.List;
import java.util.Optional;

public interface AccountDao extends GenericDao<Account>  {
    void create(Account account, String password);

    void update(Account account, String password);

    Optional<Account> findByUsername(String username);

    Optional<Account> findByPhone(String phone);

    Optional<Account> findByEmail(String email);

    public List<Account> getAccountList();

}
