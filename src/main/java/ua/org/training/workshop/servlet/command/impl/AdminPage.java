package ua.org.training.workshop.servlet.command.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.servlet.command.Command;
import ua.org.training.workshop.service.AccountService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminPage implements Command {

    AccountService accountService = new AccountService();
    private static String ADMIN_PAGE = "/WEB-INF/jsp/admin/page.jsp";

    static {
        new DOMConfigurator().doConfigure("src/log4j.xml", LogManager.getLoggerRepository());
    }
    static Logger logger = Logger.getLogger(AdminPage.class);

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response) {
        return ADMIN_PAGE;
    }
}
