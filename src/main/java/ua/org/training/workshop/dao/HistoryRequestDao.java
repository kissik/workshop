package ua.org.training.workshop.dao;

import ua.org.training.workshop.domain.HistoryRequest;
import ua.org.training.workshop.utility.Page;

public interface HistoryRequestDao extends GenericDao<HistoryRequest> {
    Page<HistoryRequest> getPageByLanguageAndAuthor(
            Page<HistoryRequest> page,
            String language,
            Long authorId);
}
