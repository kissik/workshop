package ua.org.training.workshop.dao;

import ua.org.training.workshop.domain.Status;

import java.util.Optional;

/**
 * @author kissik
 */
public interface StatusDao extends GenericDao<Status> {

    Optional<Status> findByCode(String code);

}
