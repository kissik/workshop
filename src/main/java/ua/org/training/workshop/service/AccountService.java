package ua.org.training.workshop.service;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.dao.DaoFactory;
import ua.org.training.workshop.domain.Account;
import ua.org.training.workshop.exception.WorkshopErrors;
import ua.org.training.workshop.exception.WorkshopException;
import ua.org.training.workshop.utilities.Pageable;
import ua.org.training.workshop.utilities.UtilitiesClass;

import java.sql.SQLException;

/**
 * @author kissik
 */
public class AccountService{

    private DaoFactory accountRepository = DaoFactory.getInstance();
    static {
        new DOMConfigurator().doConfigure(UtilitiesClass.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }
    private static Logger logger = Logger.getLogger(AccountService.class);

    public void setDaoFactory(DaoFactory daoFactory){
        accountRepository = daoFactory;
    }

    public Long registerAccount(Account account) throws WorkshopException {
        logger.info("Register account : " + account.getUsername());
        try {
            return accountRepository
                    .createAccountDao()
                    .create(account);
        }
        catch(SQLException e){
            logger.error("SQLException after create account : " + e.getMessage());
        }
        catch (Exception e){
            logger.error("error: " + e.getMessage());
            throw new WorkshopException(WorkshopErrors.ACCOUNT_CREATE_NEW_ERROR);
        }
        logger.info("Account " + account.getFullNameOrigin() + " was successfully created");
        return 0L;
    }

    public Account getAccountById(Long id) throws WorkshopException {
        return accountRepository
                .createAccountDao()
                .findById(id)
                .orElseThrow(() -> new WorkshopException(WorkshopErrors.ACCOUNT_NOT_FOUND_ERROR));
    }

    public Account getAccountByUsername(String username) throws WorkshopException {
        return accountRepository
                .createAccountDao()
                .findByUsername(username)
                .orElseThrow(() -> new WorkshopException(WorkshopErrors.ACCOUNT_NOT_FOUND_ERROR));
    }

    public Account getAccountByPhone(String phone) throws WorkshopException {
        return accountRepository
                .createAccountDao()
                .findByPhone(phone)
                .orElseThrow(() -> new WorkshopException(WorkshopErrors.ACCOUNT_NOT_FOUND_ERROR));
    }

    public Account getAccountByEmail(String email) throws WorkshopException {
        return accountRepository
                .createAccountDao()
                .findByEmail(email)
                .orElseThrow(() -> new WorkshopException(WorkshopErrors.ACCOUNT_NOT_FOUND_ERROR));
    }

    public String getPage(Pageable page) {
        return accountRepository
                .createAccountDao()
                .getPage(page).getPage();
    }

    public void saveAccountRoles(Account account) throws WorkshopException {
        try {
            accountRepository
                    .createAccountDao()
                    .update(account);
        } catch (SQLException e) {
            logger.error("cannot update account : " + e.getMessage());
            throw new WorkshopException(WorkshopErrors.ACCOUNT_UPDATE_ERROR);
        }
    }
}