package ua.org.training.workshop.servlet.command.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.security.AccountSecurity;
import ua.org.training.workshop.servlet.command.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;

public class LogOut implements Command {

    private static String FIRST_PAGE = "/index.jsp";
    static {
        new DOMConfigurator().doConfigure("src/log4j.xml", LogManager.getLoggerRepository());
    }
    static Logger logger = Logger.getLogger(LogOut.class);

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response) {
        AccountSecurity account = (AccountSecurity) request.getSession().getAttribute("user");
        logger.info("try to log out : " + account.getUsername());
        clear(request, account.getUsername());
        request.getSession().setAttribute("user", null);
        logger.debug("successful logout");
        return FIRST_PAGE;
    }

    static void clear(HttpServletRequest request, String username){
        Set<String> loggedUsers = (HashSet<String>) request.getServletContext()
                .getAttribute("loggedUsers");
        loggedUsers.remove(username);
        logger.info("logged users : " + loggedUsers);
    }
}
