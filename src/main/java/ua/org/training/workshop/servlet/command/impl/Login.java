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

public class Login implements Command {

    private AccountService accountService = new AccountService();

    static {
        new DOMConfigurator().doConfigure(UtilitiesClass.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }
    private static Logger logger = Logger.getLogger(Login.class);

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response) {
        String username = UtilitiesClass.getParameterString(
                request.getParameter(UtilitiesClass.APP_USERNAME_ATTRIBUTE),
                UtilitiesClass.APP_STRING_DEFAULT_VALUE);
        String password = UtilitiesClass.getParameterString(
                request.getParameter(UtilitiesClass.APP_PASSWORD_ATTRIBUTE),
                UtilitiesClass.APP_STRING_DEFAULT_VALUE);
        request.getSession().removeAttribute(UtilitiesClass.APP_ERROR_ATTRIBUTE);

        AccountSecurity account = ProcessingLoggedUsers.loadAccountSecurity(request);

        if (!account.getUsername().equals(UtilitiesClass.APP_ANONYMOUS_ACCOUNT_USERNAME)){
                ProcessingLoggedUsers.removeLoggedUser(request, account.getUsername());
                return Pages.LOGIN_PAGE;
        }

        if (username.equals(UtilitiesClass.APP_STRING_DEFAULT_VALUE)
                || password.equals(UtilitiesClass.APP_STRING_DEFAULT_VALUE)){
            return Pages.LOGIN_PAGE;
        }

        if (ProcessingLoggedUsers.checkUserIsLogged(request, username)){
            logger.debug("user already logged in system!");
            try {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                request.setAttribute(UtilitiesClass.APP_MESSAGE_ATTRIBUTE,
                        UtilitiesClass.BUNDLE_ACCESS_DENIED_LOGGED_USERS);
            }catch (IOException e){
                logger.error("IO exception on repeat logged user action : " + e.getMessage());
            }
            return UtilitiesClass.APP_STRING_DEFAULT_VALUE;
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

        if (!account.getUsername()
                .equals(UtilitiesClass.APP_ANONYMOUS_ACCOUNT_USERNAME)
                && BCrypt.checkpw(password, account.getPassword())){

            ProcessingLoggedUsers.addLoggedUser(request, account);

            if (account.hasRole("ADMIN")) return Pages.ADMIN_PAGE_REDIRECT;
            if (account.hasRole("MANAGER")) return Pages.MANAGER_PAGE_REDIRECT;
            if (account.hasRole("WORKMAN")) return Pages.WORKMAN_PAGE_REDIRECT;
            return Pages.USER_PAGE_REDIRECT;
        }

        request.getSession().setAttribute(
                UtilitiesClass.APP_ERROR_ATTRIBUTE,
                "true");
        return Pages.LOGIN_PAGE;
    }

}
