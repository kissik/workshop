package ua.org.training.workshop.service;

import ua.org.training.workshop.dao.DaoFactory;
import ua.org.training.workshop.domain.Account;
import ua.org.training.workshop.domain.HistoryRequest;
import ua.org.training.workshop.enums.WorkshopError;
import ua.org.training.workshop.exception.WorkshopException;
import ua.org.training.workshop.utility.Page;
import ua.org.training.workshop.utility.PageService;
import ua.org.training.workshop.utility.Utility;
import ua.org.training.workshop.web.dto.HistoryRequestDTO;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author kissik
 */
public class HistoryRequestService {

    private DaoFactory historyRequestRepository = DaoFactory.getInstance();

    public void setDaoFactory(DaoFactory daoFactory) {
        historyRequestRepository = daoFactory;
    }

    public HistoryRequest getHistoryRequestById(Long id) throws WorkshopException {
        return historyRequestRepository
                .createHistoryRequestDao()
                .findById(id)
                .orElseThrow(() -> new WorkshopException(WorkshopError.REQUEST_NOT_FOUND_ERROR));
    }

    public String getPage(Locale locale, Page page) {
        return getDTOPage(locale, historyRequestRepository
                .createHistoryRequestDao()
                .getPage(page));
    }

    public String getPageByLanguageAndAuthor(
            Page page,
            Locale locale,
            Account author) throws WorkshopException {
        return getDTOPage(locale, historyRequestRepository
                .createHistoryRequestDao()
                .getPageByLanguageAndAuthor(page,
                        Utility.getLanguageString(locale),
                        author));
    }

    private String getDTOPage(Locale locale, Page page) {

        page.setContent(formatRequest(locale, (Optional<List<HistoryRequest>>) page.getContent()));

        return PageService.getPage(page);
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