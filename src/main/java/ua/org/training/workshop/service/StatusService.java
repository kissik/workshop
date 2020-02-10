package ua.org.training.workshop.service;

import ua.org.training.workshop.dao.DaoFactory;
import ua.org.training.workshop.domain.Status;
import ua.org.training.workshop.enums.WorkshopError;
import ua.org.training.workshop.exception.WorkshopException;

public class StatusService {
    private DaoFactory statusRepository = DaoFactory.getInstance();

    public Status findByCode(String statusCode) throws WorkshopException {
        return statusRepository
                .createStatusDao()
                .findByCode(statusCode)
                .orElseThrow(() -> new WorkshopException(WorkshopError.STATUS_NOT_FOUND_ERROR));
    }

    public boolean hasNextStatus(Status status, Status newStatus) {
        for (Status nextStatus : status.getNextStatuses()) {
            if (nextStatus.getCode().equals(newStatus.getCode())) return true;
        }
        return false;
    }
}
