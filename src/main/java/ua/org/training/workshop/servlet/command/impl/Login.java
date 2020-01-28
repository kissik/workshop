package ua.org.training.workshop.servlet.command.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.mindrot.jbcrypt.BCrypt;
import ua.org.training.workshop.exception.WorkshopException;
import ua.org.training.workshop.security.AccountSecurity;
import ua.org.training.workshop.security.ProcessingLoggedUsers;
import ua.org.training.workshop.servlet.command.Command;
import ua.org.training.workshop.domain.Role;
import ua.org.training.workshop.service.AccountService;
import ua.org.training.workshop.utilities.UtilitiesClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class Login implements Command {

    private AccountService accountService = new AccountService();

    private static final String LOGIN_PAGE = "/WEB-INF/login.jsp";
    private static final String ADMIN_PAGE = "redirect:app/admin/page";
    private static final String MANAGER_PAGE = "redirect:app/manager/page";
    private static final String WORKMAN_PAGE = "redirect:app/workman/page";
    private static final String USER_PAGE = "redirect:app/user/page";

    static {
        new DOMConfigurator().doConfigure(UtilitiesClass.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }
    private static Logger logger = Logger.getLogger(Login.class);

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        request.getSession().removeAttribute("error");

        AccountSecurity account = (AccountSecurity) Optional
                .ofNullable(
                        request.getSession().getAttribute("user"))
                .orElse(AccountSecurity.ACCOUNT);

        if (!account.getUsername().equals(AccountSecurity.ANONYMOUS_ACCOUNT)){
                ProcessingLoggedUsers.removeLoggedUser(request, account.getUsername());
                return LOGIN_PAGE;
        }

        if( username == null || username.equals("") || password == null || password.equals("")  ){
            return LOGIN_PAGE;
        }

        if (ProcessingLoggedUsers.checkUserIsLogged(request, username)){
            logger.debug("user already logged in system!");
            try {
                response.sendError(403);
                request.setAttribute(AccessDenied.MESSAGE_ATTRIBUTE,ProcessingLoggedUsers.ACCESS_DENIED_LOGGED_USERS);
            }catch (IOException e){
                logger.error("IO exception on repeat logged user action : " + e.getMessage());
            }
            return "";
        }

        try {
            account = new AccountSecurity(
                    accountService.getAccountByUsername(username));
        }
        catch(WorkshopException e){
            logger.error(e.getMessage());
            account = AccountSecurity.ACCOUNT;
        }

        logger.debug(account.getFullNameOrigin() + Arrays.toString(account
                .getRoles()
                .stream()
                .map(Role::getCode)
                .toArray(String[]::new)));

        if (!account.getUsername().equals(AccountSecurity.ANONYMOUS_ACCOUNT)
                && BCrypt.checkpw(password, account.getPassword())){

            ProcessingLoggedUsers.addLoggedUser(request, account);

            if (account.hasRole("ADMIN")) return ADMIN_PAGE;
            if (account.hasRole("MANAGER")) return MANAGER_PAGE;
            if (account.hasRole("WORKMAN")) return WORKMAN_PAGE;
            return USER_PAGE;
        }

        request.getSession().setAttribute("error","true");
        return LOGIN_PAGE;
    }

}
