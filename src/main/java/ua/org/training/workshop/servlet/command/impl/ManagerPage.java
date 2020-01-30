package ua.org.training.workshop.servlet.command.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.servlet.command.Command;
import ua.org.training.workshop.service.AccountService;
import ua.org.training.workshop.utilities.UtilitiesClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ManagerPage implements Command {

    AccountService accountService = new AccountService();

    static {
        new DOMConfigurator().doConfigure(UtilitiesClass.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }
    static Logger logger = Logger.getLogger(ManagerPage.class);

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response) {
        return Pages.MANAGER_PAGE;
    }
}
