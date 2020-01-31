package ua.org.training.workshop.dao;

import ua.org.training.workshop.domain.Account;
import ua.org.training.workshop.domain.Request;
import ua.org.training.workshop.utilities.Pageable;

public interface RequestDao extends GenericDao<Request>  {
    Pageable getPageByAuthor(Pageable page, Account author);
}
