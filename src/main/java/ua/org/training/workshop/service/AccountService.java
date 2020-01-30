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
import ua.org.training.workshop.utilities.UtilitiesClass;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kissik
 */
public class AccountService{

    private DaoFactory accountRepository = DaoFactory.getInstance();
    static {
        new DOMConfigurator().doConfigure(UtilitiesClass.LOG4J_XML_PATH, LogManager.getLoggerRepository());
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
            throw new WorkshopException(WorkshopErrors.ACCOUNT_CREATE_NEW_ERROR);
        }
        logger.info("Account " + account.getFullNameOrigin() + " was successfully created");
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

        String jsonString = "";
        ObjectMapper jsonMapper = new ObjectMapper();

        Map<String, Object> myMap = makeMap(page);

        try{
            jsonString = jsonMapper.writeValueAsString(myMap);

        }catch (JsonGenerationException e) {
            logger.error("JSON generation exception : " + e.getMessage());
        } catch (JsonMappingException e) {
            logger.error("JSON mapping exception : " + e.getMessage());
        } catch (IOException e) {
            logger.error("IO exception : " + e.getMessage());
        }

        return jsonString;
    }

    private Map<String, Object> makeMap(Pageable page) {
        Map<String, Object> myMap = new HashMap<>();

        myMap.put("content", accountRepository
                .createAccountDao()
                .getPage(page, myMap)
                .orElse(Collections.emptyList()));
        myMap.put("size", page.getSize());

        return myMap;
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