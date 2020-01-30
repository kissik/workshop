package ua.org.training.workshop.service;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.dao.DaoFactory;
import ua.org.training.workshop.domain.Status;
import ua.org.training.workshop.exception.WorkshopErrors;
import ua.org.training.workshop.exception.WorkshopException;
import ua.org.training.workshop.utilities.UtilitiesClass;

public class StatusService {
    private DaoFactory statusRepository = DaoFactory.getInstance();
    static {
        new DOMConfigurator().doConfigure(UtilitiesClass.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }
    public static Logger logger = Logger.getLogger(StatusService.class);

    public Status findByCode(String statusCode) throws WorkshopException {
        return statusRepository
                .createStatusDao()
                .findByCode(statusCode)
                .orElseThrow(() -> new WorkshopException(WorkshopErrors.STATUS_NOT_FOUND_ERROR));
    }

}
