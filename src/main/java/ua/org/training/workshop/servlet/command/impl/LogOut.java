package ua.org.training.workshop.servlet.command.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.security.AccountSecurity;
import ua.org.training.workshop.security.ProcessingLoggedUsers;
import ua.org.training.workshop.servlet.command.Command;
import ua.org.training.workshop.utilities.UtilitiesClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogOut implements Command {

    static {
        new DOMConfigurator().doConfigure(UtilitiesClass.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }
    private static Logger logger = Logger.getLogger(LogOut.class);

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response) {

        AccountSecurity account = ProcessingLoggedUsers.loadAccountSecurity(request);
        ProcessingLoggedUsers.removeLoggedUser(request, account.getUsername());
        logger.info(account.getUsername() + " was logged out");
        return Pages.FIRST_PAGE;
    }

}
