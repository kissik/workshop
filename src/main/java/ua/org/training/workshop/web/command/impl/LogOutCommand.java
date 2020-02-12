package ua.org.training.workshop.web.command.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.security.SecurityAccount;
import ua.org.training.workshop.security.SecurityService;
import ua.org.training.workshop.utility.ApplicationConstants;
import ua.org.training.workshop.web.command.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogOutCommand implements Command {

    private final static Logger LOGGER = Logger.getLogger(LogOutCommand.class);

    static {
        new DOMConfigurator().doConfigure(ApplicationConstants.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response) {

        SecurityAccount account = SecurityService.loadAccountSecurity(request);
        SecurityService.removeLoggedUser(request, account.getUsername());
        LOGGER.info(account.getUsername() + " was logged out");
        return Pages.FIRST_PAGE;
    }

    @Override
    public void clearRequestAttributes(HttpServletRequest request) {
    }

}
