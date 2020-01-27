package ua.org.training.workshop.servlet.command.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.mindrot.jbcrypt.BCrypt;
import ua.org.training.workshop.security.AccountSecurity;
import ua.org.training.workshop.servlet.command.Command;
import ua.org.training.workshop.domain.Role;
import ua.org.training.workshop.service.AccountService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Login implements Command {

    private AccountService accountService = new AccountService();

    private static String LOGIN_PAGE = "/WEB-INF/login.jsp";
    private static String ADMIN_PAGE = "redirect:app/admin/page";
    private static String MANAGER_PAGE = "redirect:app/manager/page";
    private static String WORKMAN_PAGE = "redirect:app/workman/page";
    private static String USER_PAGE = "redirect:app/user/page";

    static {
        new DOMConfigurator().doConfigure("src/log4j.xml", LogManager.getLoggerRepository());
    }
    static Logger logger = Logger.getLogger(Login.class);

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if( username == null || username.equals("") || password == null || password.equals("")  ){
            return LOGIN_PAGE;
        }
        if (checkUserIsLogged(request, username)){
            logger.debug("user already logged in system!");
            try {
                response.sendError(403);
                request.setAttribute("message","access.denied.logged.user");
            }catch (IOException e){
                logger.error("IO exception on repeat logged user action : " + e.getMessage());
            }
            return "";
        }

        AccountSecurity account = new AccountSecurity(accountService.getAccountByUsername(username));
        logger.debug(account.getFullNameOrigin());
        for (Role r :
             account.getRoles()) {
            logger.debug("role : "+r.getCode());
        }
        logger.debug(BCrypt.checkpw(password, account.getPassword()));
        if (BCrypt.checkpw(password, account.getPassword())){
            request.getSession().setAttribute("user", account);
            if (account.hasRole("ADMIN")) return ADMIN_PAGE;
            if (account.hasRole("MANAGER")) return MANAGER_PAGE;
            if (account.hasRole("WORKMAN")) return WORKMAN_PAGE;
            return USER_PAGE;
        }
        return LOGIN_PAGE;
    }

    static boolean checkUserIsLogged(HttpServletRequest request, String username){
        Set<String> loggedUsers = (HashSet<String>) request.getServletContext().getAttribute("loggedUsers");
        logger.debug("try to check logged users!");

        if (loggedUsers!=null && loggedUsers.stream().anyMatch(username::equals)){
            logger.info("logged users : " + loggedUsers);
            return true;
        }

        if (loggedUsers==null) loggedUsers = new HashSet<>();

        loggedUsers.add(username);
        logger.info("logged users : " + loggedUsers);
        request.getServletContext()
                .setAttribute("loggedUsers", loggedUsers);
        return false;
    }
}
