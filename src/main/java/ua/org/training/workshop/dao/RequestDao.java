package ua.org.training.workshop.dao;

import ua.org.training.workshop.domain.Account;
import ua.org.training.workshop.domain.Request;
import ua.org.training.workshop.domain.Status;
import ua.org.training.workshop.service.RequestService;
import ua.org.training.workshop.utilities.Pageable;

public interface RequestDao extends GenericDao<Request>  {
    Pageable getPageByAuthor(Pageable page, Account author);

    Pageable getPageByLanguageAndAuthor(Pageable page, String language, Account author);

    Pageable getPageByLanguageAndStatus(Pageable page, String language, Status status);
}
