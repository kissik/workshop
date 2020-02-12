package ua.org.training.workshop.dao;

import ua.org.training.workshop.domain.Request;
import ua.org.training.workshop.utility.Page;

public interface RequestDao extends GenericDao<Request> {
    Page<Request> getPageByAuthor(
            Page<Request> page,
            Long authorId);

    Page<Request> getPageByLanguageAndAuthor(
            Page<Request> page,
            String language,
            Long authorId);

    Page<Request> getPageByLanguageAndStatus(
            Page<Request> page,
            String language,
            Long statusId);
}
