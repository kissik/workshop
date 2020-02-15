package ua.org.training.workshop.service;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.dao.DaoFactory;
import ua.org.training.workshop.domain.HistoryRequest;
import ua.org.training.workshop.enums.WorkshopError;
import ua.org.training.workshop.exception.WorkshopException;
import ua.org.training.workshop.utility.ApplicationConstants;
import ua.org.training.workshop.utility.Page;
import ua.org.training.workshop.utility.PageService;
import ua.org.training.workshop.utility.Utility;
import ua.org.training.workshop.web.dto.HistoryRequestDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author kissik
 */
public class HistoryRequestService {

    private DaoFactory historyRequestRepository = DaoFactory.getInstance();
    private final static Logger LOGGER = Logger.getLogger(RequestService.class);

    static {
        new DOMConfigurator().doConfigure(ApplicationConstants.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }

    public void setDaoFactory(DaoFactory daoFactory) {
        historyRequestRepository = daoFactory;
    }

    public HistoryRequest getHistoryRequestById(Long id) throws WorkshopException {
        return loadAggregateFields(historyRequestRepository
                .createHistoryRequestDao()
                .findById(id)
                .orElseThrow(() -> new WorkshopException(WorkshopError.REQUEST_NOT_FOUND_ERROR)));
    }

    public String getPage(Locale locale, Page<HistoryRequest> page) {
        return getDTOPage(locale, historyRequestRepository
                .createHistoryRequestDao()
                .getPage(page));
    }

    public String getPageByLanguageAndAuthor(
            Page<HistoryRequest> page,
            Locale locale,
            Long authorId) throws WorkshopException {
        return getDTOPage(locale, historyRequestRepository
                .createHistoryRequestDao()
                .getPageByLanguageAndAuthor(page,
                        Utility.getLanguageString(locale),
                        authorId));
    }

    private String getDTOPage(Locale locale, Page<HistoryRequest> page) {
        Page<HistoryRequestDTO> historyRequestDTOPage = createDTOPage(locale, page);
        PageService<HistoryRequestDTO> historyRequestDTOPageService = new PageService<>();
        return historyRequestDTOPageService.getPage(historyRequestDTOPage);
    }

    private Page<HistoryRequestDTO> createDTOPage(Locale locale, Page<HistoryRequest> page) {
        Page<HistoryRequestDTO> historyRequestDTOPage = new Page<>();
        historyRequestDTOPage.setLanguage(page.getLanguage());
        historyRequestDTOPage.setPageNumber(page.getPageNumber());
        historyRequestDTOPage.setSize(page.getSize());
        historyRequestDTOPage.setTotalElements(page.getTotalElements());
        historyRequestDTOPage
                .setContent(formatRequest(locale, page.getContent()));
        return historyRequestDTOPage;
    }

    private Optional<List<HistoryRequestDTO>> formatRequest(Locale locale, Optional<List<HistoryRequest>> historyRequestsList) {
        return historyRequestsList.map(historyRequestList -> Optional.of(
                historyRequestList
                        .stream()
                        .map(historyRequest -> new HistoryRequestDTO(locale, loadAggregateFields(historyRequest)))
                        .collect(Collectors.toList()))).orElse(null);
    }

    private HistoryRequest loadAggregateFields(HistoryRequest historyRequest) {
        try {
            historyRequest.setStatus(historyRequestRepository
                    .createStatusDao()
                    .findByHistoryRequestId(historyRequest.getId())
                    .orElseThrow(() -> new WorkshopException(WorkshopError.STATUS_NOT_FOUND_ERROR)));
            historyRequest.setAuthor(historyRequestRepository
                    .createAccountDao()
                    .findAuthorByHistoryRequestId(historyRequest.getId())
                    .orElseThrow(() -> new WorkshopException(WorkshopError.ACCOUNT_NOT_FOUND_ERROR)));
            historyRequest.setUser(historyRequestRepository
                    .createAccountDao()
                    .findUserByHistoryRequestId(historyRequest.getId())
                    .orElseThrow(() -> new WorkshopException(WorkshopError.ACCOUNT_NOT_FOUND_ERROR)));
        } catch (WorkshopException e) {
            LOGGER.error("load aggreagate fields error : " + e.getMessage());
        }
        return historyRequest;
    }

    public void update(HistoryRequest historyRequest) {
        try {
            historyRequestRepository.createHistoryRequestDao().update(historyRequest);
        } catch (SQLException e) {
            LOGGER.error("update history request error : " + e.getMessage());
        }
        LOGGER.info("history request " + historyRequest.getTitle() + " was successfully changed");
    }
}