package ua.org.training.workshop.dao;

import ua.org.training.workshop.domain.Account;
import ua.org.training.workshop.domain.Request;
import ua.org.training.workshop.utilities.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RequestDao extends GenericDao<Request>  {

    Optional<List<Request>> getPageByAuthor(Pageable page, Account author, Map<String, Object> mapJson);
}
