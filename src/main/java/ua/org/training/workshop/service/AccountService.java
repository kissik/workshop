package ua.org.training.workshop.service;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.dao.DaoFactory;
import ua.org.training.workshop.domain.Account;
import ua.org.training.workshop.enums.WorkshopError;
import ua.org.training.workshop.exception.WorkshopException;
import ua.org.training.workshop.utility.ApplicationConstants;
import ua.org.training.workshop.utility.Page;
import ua.org.training.workshop.utility.PageService;

import java.sql.SQLException;

/**
 * @author kissik
 */
public class AccountService {

    private final static Logger LOGGER = Logger.getLogger(AccountService.class);

    static {
        new DOMConfigurator().doConfigure(ApplicationConstants.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }

    private DaoFactory accountRepository = DaoFactory.getInstance();

    public void setDaoFactory(DaoFactory daoFactory) {
        accountRepository = daoFactory;
    }

    public Long registerAccount(Account account, String hashPassword) throws WorkshopException {
        LOGGER.info("Register account : " + account.getUsername());
        try {
            return accountRepository
                    .createAccountDao()
                    .create(account, hashPassword);
        } catch (SQLException e) {
            LOGGER.error("SQLException after create account : " + e.getMessage());
        } catch (Exception e) {
            LOGGER.error("error: " + e.getMessage());
            throw new WorkshopException(WorkshopError.ACCOUNT_CREATE_NEW_ERROR);
        }
        LOGGER.info("Account " + account.getFullNameOrigin() + " was successfully created");
        return 0L;
    }

    public Account getAccountById(Long id) throws WorkshopException {
        Account account = accountRepository
                .createAccountDao()
                .findById(id)
                .orElseThrow(() -> new WorkshopException(WorkshopError.ACCOUNT_NOT_FOUND_ERROR));
        account.setRoles(accountRepository
                .createRoleDao()
                .findRolesByAccountId(account.getId())
                .orElseThrow(() -> new WorkshopException((WorkshopError.ROLE_LIST_IS_EMPTY_ERROR))));
        return account;
    }

    public Account getAccountByUsername(String username) throws WorkshopException {
        Account account = accountRepository
                .createAccountDao()
                .findByUsername(username)
                .orElseThrow(() -> new WorkshopException(WorkshopError.ACCOUNT_NOT_FOUND_ERROR));
        account.setRoles(accountRepository
                .createRoleDao()
                .findRolesByAccountId(account.getId())
                .orElseThrow(() -> new WorkshopException((WorkshopError.ROLE_LIST_IS_EMPTY_ERROR))));
        return account;
    }

    public Account getAccountByPhone(String phone) throws WorkshopException {
        return accountRepository
                .createAccountDao()
                .findByPhone(phone)
                .orElseThrow(() -> new WorkshopException(WorkshopError.ACCOUNT_NOT_FOUND_ERROR));
    }

    public Account getAccountByEmail(String email) throws WorkshopException {
        return accountRepository
                .createAccountDao()
                .findByEmail(email)
                .orElseThrow(() -> new WorkshopException(WorkshopError.ACCOUNT_NOT_FOUND_ERROR));
    }

    public String getPage(Page<Account> page) {
        PageService<Account> pageService = new PageService<>();
        return pageService.getPage(accountRepository
                .createAccountDao()
                .getPage(page));
    }

    public void saveAccountRoles(Account account) throws WorkshopException {
        try {
            accountRepository
                    .createAccountDao()
                    .update(account);
        } catch (SQLException e) {
            LOGGER.error("cannot update account : " + e.getMessage());
            throw new WorkshopException(WorkshopError.ACCOUNT_UPDATE_ERROR);
        }
    }
}