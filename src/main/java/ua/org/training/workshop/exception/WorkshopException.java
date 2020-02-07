package ua.org.training.workshop.exception;

import ua.org.training.workshop.enums.WorkshopError;

public class WorkshopException extends RuntimeException {
    private WorkshopError workshopError;

    public WorkshopException(WorkshopError workshopError) {
        super(workshopError.message());
        this.workshopError = workshopError;
    }

    public WorkshopException(Throwable cause, WorkshopError workshopError) {
        super(workshopError.message(), cause);
        this.workshopError = workshopError;
    }

    public Integer getErrorCode() {
        return workshopError.code();
    }

    public WorkshopError getWorkshopError() {
        return workshopError;
    }
}
