package ua.org.training.workshop.web.listener;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.exception.WorkshopException;
import ua.org.training.workshop.security.SecurityService;
import ua.org.training.workshop.utility.ApplicationConstants;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {
    static {
        new DOMConfigurator().doConfigure(ApplicationConstants.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }

    private final static Logger LOGGER = Logger.getLogger(SessionListener.class);
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        try {
            SecurityService.removeLoggedUserContext(httpSessionEvent
                    .getSession());
        }catch(WorkshopException e){
            LOGGER.info(e.getMessage());
        }
    }
}
