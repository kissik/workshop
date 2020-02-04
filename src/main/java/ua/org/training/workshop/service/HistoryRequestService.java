package ua.org.training.workshop.service;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.dao.DaoFactory;
import ua.org.training.workshop.domain.Account;
import ua.org.training.workshop.domain.HistoryRequest;
import ua.org.training.workshop.exception.WorkshopErrors;
import ua.org.training.workshop.exception.WorkshopException;
import ua.org.training.workshop.service.dto.HistoryRequestDTO;
import ua.org.training.workshop.utilities.Pageable;
import ua.org.training.workshop.utilities.UtilitiesClass;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author kissik
 */
public class HistoryRequestService {

    private DaoFactory historyRequestRepository = DaoFactory.getInstance();
    static {
        new DOMConfigurator().doConfigure(UtilitiesClass.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }
    private static Logger logger = Logger.getLogger(HistoryRequestService.class);

    public void setDaoFactory(DaoFactory daoFactory){
        historyRequestRepository = daoFactory;
    }

    public HistoryRequest getHistoryRequestById(Long id) throws WorkshopException {
        return historyRequestRepository
                .createHistoryRequestDao()
                .findById(id)
                .orElseThrow(() -> new WorkshopException(WorkshopErrors.REQUEST_NOT_FOUND_ERROR));
    }

    public String getPage(Locale locale, Pageable page) {
        return getDTOPage(locale, historyRequestRepository
                .createHistoryRequestDao()
                .getPage(page));
    }

    public String getPageByLanguageAndAuthor(
            Pageable page,
            Locale locale,
            Account author) throws WorkshopException{
        return getDTOPage(locale, historyRequestRepository
                .createHistoryRequestDao()
                .getPageByLanguageAndAuthor(page,
                        UtilitiesClass.getLanguageString(locale),
                        author));
    }

    private String getDTOPage(Locale locale, Pageable page){

        page.setContent(formatRequest(locale, (Optional<List<HistoryRequest>>) page.getContent()));

        return page.getPage();
    }

    private Optional<List<HistoryRequestDTO>> formatRequest(Locale locale, Optional<List<HistoryRequest>> requestsList) {
        return requestsList
                .map(requestList -> Optional.of(
                        requestList
                                .stream()
                                .map(request -> new HistoryRequestDTO(locale, request))
                                .collect(Collectors.toList()))
                )
                .orElse(null);
    }
}