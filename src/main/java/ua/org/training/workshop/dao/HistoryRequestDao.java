package ua.org.training.workshop.dao;

import ua.org.training.workshop.domain.Account;
import ua.org.training.workshop.domain.HistoryRequest;
import ua.org.training.workshop.utilities.Pageable;

public interface HistoryRequestDao extends GenericDao<HistoryRequest>  {
    Pageable getPageByLanguageAndAuthor(Pageable page, String language, Account author);
}
