package ua.org.training.workshop.web.command.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.utility.ApplicationConstants;
import ua.org.training.workshop.web.command.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ExceptionCommand implements Command {
    private final static Logger LOGGER = Logger.getLogger(ExceptionCommand.class);

    static {
        new DOMConfigurator().doConfigure(ApplicationConstants.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response) {
        LOGGER.debug("Generated exception");
        throw new RuntimeException("Generated exception");
    }
}
