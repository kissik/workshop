package ua.org.training.workshop.exception;

public class WorkshopException extends RuntimeException{
    private WorkshopErrors workshopError;

    public WorkshopException(WorkshopErrors workshopError) {
        super(workshopError.message());
        this.workshopError = workshopError;
    }

    public WorkshopException(Throwable cause, WorkshopErrors workshopError) {
        super(workshopError.message(), cause);
        this.workshopError = workshopError;
    }

    public Integer getErrorCode() {
        return workshopError.code();
    }

    public WorkshopErrors getWorkshopError(){
        return workshopError;
    }
}
