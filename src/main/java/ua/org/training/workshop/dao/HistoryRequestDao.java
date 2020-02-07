package ua.org.training.workshop.dao;

import ua.org.training.workshop.domain.Account;
import ua.org.training.workshop.domain.HistoryRequest;
import ua.org.training.workshop.utility.Page;

public interface HistoryRequestDao extends GenericDao<HistoryRequest> {
    Page getPageByLanguageAndAuthor(Page page, String language, Account author);
}
