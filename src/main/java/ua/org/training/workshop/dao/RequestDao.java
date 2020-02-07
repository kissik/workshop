package ua.org.training.workshop.dao;

import ua.org.training.workshop.domain.Account;
import ua.org.training.workshop.domain.Request;
import ua.org.training.workshop.domain.Status;
import ua.org.training.workshop.utility.Page;

public interface RequestDao extends GenericDao<Request> {
    Page getPageByAuthor(Page page, Account author);

    Page getPageByLanguageAndAuthor(Page page, String language, Account author);

    Page getPageByLanguageAndStatus(Page page, String language, Status status);
}
