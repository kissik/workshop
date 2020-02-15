package ua.org.training.workshop.service;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.dao.DaoFactory;
import ua.org.training.workshop.domain.Request;
import ua.org.training.workshop.domain.Status;
import ua.org.training.workshop.enums.WorkshopError;
import ua.org.training.workshop.exception.WorkshopException;
import ua.org.training.workshop.utility.ApplicationConstants;
import ua.org.training.workshop.utility.Page;
import ua.org.training.workshop.utility.PageService;
import ua.org.training.workshop.utility.Utility;
import ua.org.training.workshop.web.dto.RequestDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author kissik
 */
public class RequestService {

    private final static Logger LOGGER = Logger.getLogger(RequestService.class);

    static {
        new DOMConfigurator().doConfigure(ApplicationConstants.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }

    private DaoFactory requestRepository = DaoFactory.getInstance();

    public void setDaoFactory(DaoFactory daoFactory) {
        requestRepository = daoFactory;
    }

    public Long createRequest(Request request) throws WorkshopException {
        try {
            return requestRepository
                    .createRequestDao()
                    .create(request);
        } catch (SQLException e) {
            LOGGER.error("SQL exception after create account : " + e.getMessage());
            throw new WorkshopException(WorkshopError.REQUEST_CREATE_NEW_ERROR);
        } catch (Exception e) {
            LOGGER.error("error: " + e.getMessage());
            throw new WorkshopException(WorkshopError.REQUEST_CREATE_NEW_ERROR);
        }
    }

    public Request getRequestById(Long id) throws WorkshopException {
        return loadAggregateFields(requestRepository
                .createRequestDao()
                .findById(id)
                .orElseThrow(() -> new WorkshopException(WorkshopError.REQUEST_NOT_FOUND_ERROR)));
    }

    public String getPage(Locale locale, Page<Request> page) {
        return getDTOPage(locale, requestRepository
                .createRequestDao()
                .getPage(page));
    }

    public String getPageByAuthor(Page<Request> page, Locale locale, Long authorId) {
        return getDTOPage(locale, requestRepository
                .createRequestDao()
                .getPageByAuthor(page, authorId));
    }

    public String getPageByLanguageAndAuthor(Page<Request> page,
                                             Locale locale,
                                             Long authorId) throws WorkshopException {
        return getDTOPage(locale, requestRepository
                .createRequestDao()
                .getPageByLanguageAndAuthor(page,
                        Utility.getLanguageString(locale),
                        authorId));
    }

    public String getPageByLanguageAndStatus(Page<Request> page,
                                             Locale locale,
                                             Long statusId) throws WorkshopException {
        return getDTOPage(locale, requestRepository
                .createRequestDao()
                .getPageByLanguageAndStatus(page,
                        Utility.getLanguageString(locale),
                        statusId));
    }

    private String getDTOPage(Locale locale, Page<Request> page) {
        PageService<RequestDTO> pageService = new PageService<>();
        Page<RequestDTO> requestDTOPage = createDTOPage(locale, page);

        return pageService.getPage(requestDTOPage);
    }

    private Page<RequestDTO> createDTOPage(Locale locale, Page<Request> page) {
        Page<RequestDTO> historyRequestDTOPage = new Page<>();
        historyRequestDTOPage.setLanguage(page.getLanguage());
        historyRequestDTOPage.setPageNumber(page.getPageNumber());
        historyRequestDTOPage.setSize(page.getSize());
        historyRequestDTOPage.setTotalElements(page.getTotalElements());
        historyRequestDTOPage
                .setContent(formatRequest(locale, page.getContent()));
        return historyRequestDTOPage;
    }

    private Optional<List<RequestDTO>> formatRequest(Locale locale, Optional<List<Request>> requestsList) {
        return requestsList
                .map(requestList -> Optional.of(
                        requestList
                                .stream()
                                .map(request -> new RequestDTO(locale, loadAggregateFields(request)))
                                .collect(Collectors.toList()))
                )
                .orElse(null);
    }

    private Request loadAggregateFields(Request request) {
        try {
            Status status = requestRepository
                    .createStatusDao()
                    .findByRequestId(request.getId())
                    .orElseThrow(() -> new WorkshopException(WorkshopError.STATUS_NOT_FOUND_ERROR));
            status.setNextStatuses(requestRepository
                    .createStatusDao()
                    .findNextStatusesForCurrentStatusById(status.getId())
                    .orElseThrow(() -> new WorkshopException(WorkshopError.STATUS_LIST_IS_EMPTY_ERROR))
            );
            request.setStatus(status);
            request.setUser(requestRepository
                    .createAccountDao()
                    .findAuthorByRequestId(request.getId())
                    .orElseThrow(() -> new WorkshopException(WorkshopError.ACCOUNT_NOT_FOUND_ERROR)));
            request.setAuthor(requestRepository
                    .createAccountDao()
                    .findUserByRequestId(request.getId())
                    .orElseThrow(() -> new WorkshopException(WorkshopError.ACCOUNT_NOT_FOUND_ERROR)));
        } catch (WorkshopException e) {
            LOGGER.error("load aggreagate fields error : " + e.getMessage());
        }
        return request;
    }

    public void updateRequest(Request request) {
        try {
            requestRepository.createRequestDao().update(request);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new WorkshopException(WorkshopError.REQUEST_UPDATE_ERROR);
        }
        LOGGER.info("Request was successfully updated : " + request.toString());
    }
}