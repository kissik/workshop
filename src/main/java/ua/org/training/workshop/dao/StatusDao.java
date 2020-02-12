package ua.org.training.workshop.dao;

import ua.org.training.workshop.domain.Status;

import java.util.List;
import java.util.Optional;

/**
 * @author kissik
 */
public interface StatusDao extends GenericDao<Status> {

    Optional<Status> findByCode(String code);

    Optional<Status> findByRequestId(Long requestId);

    Optional<Status> findByHistoryRequestId(Long historyRequestId);

    Optional<List<Status>> findNextStatusesForCurrentStatusById(Long id);
}