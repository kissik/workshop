package ua.org.training.workshop.servlet.command.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.servlet.command.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AccessDenied implements Command {

    private static String ACCESS_DENIED_PAGE = "/WEB-INF/access-denied.jsp";

    static {
        new DOMConfigurator().doConfigure("src/log4j.xml", LogManager.getLoggerRepository());
    }
    static Logger logger = Logger.getLogger(AccessDenied.class);

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response) {
        return ACCESS_DENIED_PAGE;
    }
}
