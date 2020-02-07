package ua.org.training.workshop.web.command.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.mindrot.jbcrypt.BCrypt;
import ua.org.training.workshop.domain.Role;
import ua.org.training.workshop.exception.WorkshopException;
import ua.org.training.workshop.security.SecurityAccount;
import ua.org.training.workshop.security.SecurityService;
import ua.org.training.workshop.service.AccountService;
import ua.org.training.workshop.utility.ApplicationConstants;
import ua.org.training.workshop.utility.Utility;
import ua.org.training.workshop.web.command.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public class LogInCommand implements Command {

    private final static Logger LOGGER = Logger.getLogger(LogInCommand.class);

    static {
        new DOMConfigurator().doConfigure(ApplicationConstants.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }

    private AccountService accountService = new AccountService();

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response) {
        String username = Utility.getParameterString(
                request.getParameter(ApplicationConstants.RequestAttributes
                        .APP_USERNAME_ATTRIBUTE),
                ApplicationConstants.APP_STRING_DEFAULT_VALUE);
        String password = Utility.getParameterString(
                request.getParameter(ApplicationConstants.RequestAttributes.APP_PASSWORD_ATTRIBUTE),
                ApplicationConstants.APP_STRING_DEFAULT_VALUE);
        request.getSession().removeAttribute(ApplicationConstants.RequestAttributes.APP_ERROR_ATTRIBUTE);

        SecurityAccount securityAccount = SecurityService.loadAccountSecurity(request);

        if (!securityAccount.getUsername().equals(ApplicationConstants.APP_ANONYMOUS_ACCOUNT_USERNAME)) {
            SecurityService.removeLoggedUser(request, securityAccount.getUsername());
            return Pages.LOGIN_PAGE;
        }

        if (username.equals(ApplicationConstants.APP_STRING_DEFAULT_VALUE)
                || password.equals(ApplicationConstants.APP_STRING_DEFAULT_VALUE)) {
            return Pages.LOGIN_PAGE;
        }

        if (SecurityService.checkUserIsLogged(request, username)) {
            LOGGER.debug("user already logged in system!");
            try {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                request.setAttribute(ApplicationConstants.RequestAttributes.APP_MESSAGE_ATTRIBUTE,
                        ApplicationConstants.BUNDLE_ACCESS_DENIED_LOGGED_USERS);
            } catch (IOException e) {
                LOGGER.error("IO exception on repeat logged user action : " + e.getMessage());
            }
            return ApplicationConstants.APP_STRING_DEFAULT_VALUE;
        }

        try {
            securityAccount = new SecurityAccount(
                    accountService.getAccountByUsername(username));
        } catch (WorkshopException e) {
            LOGGER.error(e.getMessage());
            securityAccount = SecurityAccount.ACCOUNT;
        }

        LOGGER.debug(securityAccount.getFullNameOrigin() + Arrays.toString(securityAccount
                .getRoles()
                .stream()
                .map(Role::getCode)
                .toArray(String[]::new)));

        if (!securityAccount.getUsername()
                .equals(ApplicationConstants.APP_ANONYMOUS_ACCOUNT_USERNAME)
                && BCrypt.checkpw(password, securityAccount.getPassword())) {

            SecurityService.addLoggedUser(request, securityAccount);

            if (securityAccount.hasRole("ADMIN")) return Pages.ADMIN_PAGE_REDIRECT;
            if (securityAccount.hasRole("MANAGER")) return Pages.MANAGER_PAGE_REDIRECT;
            if (securityAccount.hasRole("WORKMAN")) return Pages.WORKMAN_PAGE_REDIRECT;
            return Pages.USER_PAGE_REDIRECT;
        }
        clearRequestAttributes(request);
        request.getSession().setAttribute(
                ApplicationConstants.RequestAttributes.APP_ERROR_ATTRIBUTE,
                "true");
        return Pages.LOGIN_PAGE;
    }

    private void clearRequestAttributes(HttpServletRequest request) {
        request.getSession().removeAttribute(ApplicationConstants.RequestAttributes.APP_USERNAME_ATTRIBUTE);
        request.getSession().removeAttribute(ApplicationConstants.RequestAttributes.APP_PASSWORD_ATTRIBUTE);
    }

}
