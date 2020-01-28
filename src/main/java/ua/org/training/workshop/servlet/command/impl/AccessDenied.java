package ua.org.training.workshop.servlet.command.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.servlet.command.Command;
import ua.org.training.workshop.utilities.UtilitiesClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AccessDenied implements Command {

    private static final String ACCESS_DENIED_PAGE = "/WEB-INF/access-denied.jsp";
    public static final String MESSAGE_ATTRIBUTE = "message";
    public static final String MESSAGE = "access.denied.message";

    static {
        new DOMConfigurator().doConfigure(UtilitiesClass.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }
    private static Logger logger = Logger.getLogger(AccessDenied.class);

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response) {
        return ACCESS_DENIED_PAGE;
    }
}
