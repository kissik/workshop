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
import ua.org.training.workshop.service.dto.RequestDTO;
import ua.org.training.workshop.utilities.Pageable;
import ua.org.training.workshop.utilities.UtilitiesClass;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public String getPage(Locale locale, Pageable page) {
        return getDTOPage(locale, requestRepository
                .createRequestDao()
                .getPage(page));
    }

    public String getPageByAuthor(Pageable page, Locale locale, Account author) {
        return getDTOPage(locale, requestRepository
                .createRequestDao()
                .getPageByAuthor(page, author));
    }

    public String getPageByLanguageAndAuthor(Pageable page,
                                             Locale locale,
                                             Account author) throws WorkshopException{
        return getDTOPage(locale, requestRepository
                .createRequestDao()
                .getPageByLanguageAndAuthor(page,
                        UtilitiesClass.getLanguageString(locale),
                        author));
    }

    public String getPageByLanguageAndStatus(Pageable page,
                                             Locale locale,
                                             Status status) throws WorkshopException{
        return getDTOPage(locale, requestRepository
                .createRequestDao()
                .getPageByLanguageAndStatus(page,
                        UtilitiesClass.getLanguageString(locale),
                        status));
    }

    private String getDTOPage(Locale locale, Pageable page){

        page.setContent(formatRequest(locale, (Optional<List<Request>>) page.getContent()));

        return page.getPage();
    }

    private Optional<List<RequestDTO>> formatRequest(Locale locale, Optional<List<Request>> requestsList) {
        return requestsList
                .map(requestList -> Optional.of(
                        requestList
                                .stream()
                                .map(request -> new RequestDTO(locale, request))
                                .collect(Collectors.toList()))
                )
                .orElse(null);
    }
}