package ua.org.training.workshop.service;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.dao.DaoFactory;
import ua.org.training.workshop.domain.Account;
import ua.org.training.workshop.domain.Request;
import ua.org.training.workshop.domain.Status;
import ua.org.training.workshop.exception.WorkshopErrors;
import ua.org.training.workshop.exception.WorkshopException;
import ua.org.training.workshop.utilities.Pageable;
import ua.org.training.workshop.utilities.UtilitiesClass;

import java.sql.SQLException;

/**
 * @author kissik
 */
public class RequestService {

    private DaoFactory requestRepository = DaoFactory.getInstance();
    static {
        new DOMConfigurator().doConfigure(UtilitiesClass.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }
    private static Logger logger = Logger.getLogger(RequestService.class);

    public void setDaoFactory(DaoFactory daoFactory){
        requestRepository = daoFactory;
    }

    public Long createRequest(Request request) throws WorkshopException {
        try {
            return requestRepository
                    .createRequestDao()
                    .create(request);
        }
        catch(SQLException e){
            logger.error("SQL exception after create account : " + e.getMessage());
            throw new WorkshopException(WorkshopErrors.REQUEST_CREATE_NEW_ERROR);
        }
        catch (Exception e){
            logger.error("error: " + e.getMessage());
            throw new WorkshopException(WorkshopErrors.REQUEST_CREATE_NEW_ERROR);
        }
    }

    public Request getRequestById(Long id) throws WorkshopException {
        return requestRepository
                .createRequestDao()
                .findById(id)
                .orElseThrow(() -> new WorkshopException(WorkshopErrors.REQUEST_NOT_FOUND_ERROR));
    }

    public String getPage(Pageable page) {
        return requestRepository
                .createRequestDao()
                .getPage(page).getPage();
    }

    public String getPageByAuthor(Pageable page, Account author) {
        return requestRepository
                .createRequestDao()
                .getPageByAuthor(page, author)
                .getPage();
    }

    public String getPageByLanguageAndAuthor(Pageable page, String language, Account author) throws WorkshopException{
        return requestRepository
                .createRequestDao()
                .getPageByLanguageAndAuthor(page, language, author)
                .getPage();
    }

    public String getPageByLanguageAndStatus(Pageable page, String language, Status status) throws WorkshopException{
        return requestRepository
                .createRequestDao()
                .getPageByLanguageAndStatus(page, language, status)
                .getPage();
    }
}