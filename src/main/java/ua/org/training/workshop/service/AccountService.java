package ua.org.training.workshop.service;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import ua.org.training.workshop.dao.DaoFactory;
import ua.org.training.workshop.domain.Account;
import ua.org.training.workshop.exception.WorkshopErrors;
import ua.org.training.workshop.exception.WorkshopException;
import ua.org.training.workshop.utilities.Pageable;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * @author kissik
 */
public class AccountService{

    private DaoFactory accountRepository = DaoFactory.getInstance();
    static {
        new DOMConfigurator().doConfigure("src/log4j.xml", LogManager.getLoggerRepository());
    }
    private static Logger logger = Logger.getLogger(AccountService.class);

    public void registerAccount(Account account) throws WorkshopException {

        try {
            accountRepository
                    .createAccountDao()
                    .create(account);
        }
        catch(SQLException e){
            logger.error("SQLException after create account : " + e.getMessage());
        }
        catch (Exception e){
            logger.error("error: " + e.getMessage());
            logger.info("error: " + e.getMessage());
            throw new WorkshopException(WorkshopErrors.ACCOUNT_CREATE_NEW_ERROR);
        }
        logger.info("Account " + account.getFullNameOrigin() + " was successfully created");
    }

    public List<Account> getAccountList() throws WorkshopException {

        return accountRepository
                .createAccountDao()
                .findAll()
                .orElseThrow(() -> new WorkshopException(WorkshopErrors.ACCOUNT_LIST_IS_EMPTY_ERROR));
    }

    public Account getAccountById(Long id) throws WorkshopException {
        return accountRepository
                .createAccountDao()
                .findById(id)
                .orElseThrow(() -> new WorkshopException(WorkshopErrors.ACCOUNT_NOT_FOUND_ERROR));
    }
//
//    @Transactional
//    public void setAccountInfo(Account account, String[] roleForm) {
//
//            Collection<Role> roles = new HashSet<>();
//            for (String roleStr : roleForm) roles.add(roleService.findByCode(roleStr));
//            account.setRoles(roles);
//            accountRepository.saveAndFlush(account);
//
//    }
//
//    @Transactional
//    public boolean newAccount(Account account) throws WorkshopException {
//
//        try{
//            accountRepository.save(account);
//        }catch(Exception e){
//            logger.error("error: " + e.getMessage());
//            logger.info("error: " + e.getMessage());
//            throw new WorkshopException(WorkshopErrors.ACCOUNT_CREATE_NEW_ERROR);
//        }
//        return true;
//    }

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
                .getPage(page);
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