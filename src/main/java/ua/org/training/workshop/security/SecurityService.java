package ua.org.training.workshop.security;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.dao.DaoFactory;
import ua.org.training.workshop.dao.SecurityDao;
import ua.org.training.workshop.dao.impl.JDBCDaoFactory;
import ua.org.training.workshop.dao.impl.JDBCSecurityDao;
import ua.org.training.workshop.enums.WorkshopError;
import ua.org.training.workshop.exception.WorkshopException;
import ua.org.training.workshop.utility.ApplicationConstants;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author kissik
 */
public class SecurityService implements Serializable {

    private static Logger logger = Logger.getLogger(SecurityService.class);

    static {
        new DOMConfigurator().doConfigure(ApplicationConstants.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }

    public static SecurityAccount loadAccountSecurity(HttpServletRequest request) {
        return (SecurityAccount) Optional
                .ofNullable(
                        request.getSession()
                                .getAttribute(ApplicationConstants
                                        .RequestAttributes
                                        .APP_USER_ATTRIBUTE))
                .orElse(SecurityAccount.ACCOUNT);
    }

    private static HashSet<String> getLoggedUsers(HttpServletRequest request) {
        return getLoggedUsersContext(request
                .getServletContext());
    }

    private static HashSet<String> getLoggedUsersContext(ServletContext context) {
        return Optional
                .ofNullable(
                        (HashSet<String>) context
                                .getAttribute(ApplicationConstants
                                        .RequestAttributes
                                        .APP_LOGGED_USERS_HASH_SET_ATTRIBUTE))
                .orElse(new HashSet<>());
    }

    private static void saveLoggedUser(HttpServletRequest request, Set<String> loggedUsers) {
        saveLoggedUserContext(request.getServletContext(), loggedUsers);
    }

    private static void saveLoggedUserContext(ServletContext context, Set<String> loggedUsers) {
        context
                .setAttribute(ApplicationConstants
                        .RequestAttributes
                        .APP_LOGGED_USERS_HASH_SET_ATTRIBUTE, loggedUsers);
        logger.info("logged users : " + loggedUsers);
    }

    public static boolean checkUserIsLogged(HttpServletRequest request, String username) {
        Set<String> loggedUsers = getLoggedUsers(request);
        logger.debug("try to check logged users!");
        if (loggedUsers.stream().anyMatch(username::equals)) {
            logger.info("logged users : " + loggedUsers);
            return true;
        }
        return false;
    }

    public static void addLoggedUser(HttpServletRequest request, SecurityAccount account) {
        Set<String> loggedUsers = getLoggedUsers(request);
        loggedUsers.add(account.getUsername());
        request.getSession().setAttribute(ApplicationConstants
                .RequestAttributes
                .APP_USER_ATTRIBUTE, account);
        saveLoggedUser(request, loggedUsers);
    }

    public static void removeLoggedUser(HttpServletRequest request, String username) {
        Set<String> loggedUsers = getLoggedUsers(request);
        loggedUsers.remove(username);
        request.getSession().removeAttribute(ApplicationConstants
                .RequestAttributes
                .APP_USER_ATTRIBUTE);
        saveLoggedUser(request, loggedUsers);
    }

    public static void removeLoggedUserContext(HttpSession session) {
        Set<String> loggedUsers = getLoggedUsersContext(session
                .getServletContext());
        loggedUsers.remove(getCurrentUserName(session));
        session.removeAttribute(ApplicationConstants
                .RequestAttributes
                .APP_USER_ATTRIBUTE);
        saveLoggedUserContext(session
                .getServletContext(), loggedUsers);
    }

    public static String getCurrentUserName(HttpSession session) {
        SecurityAccount securityAccount = (SecurityAccount) Optional
                .ofNullable(
                        session.getAttribute(ApplicationConstants
                                .RequestAttributes
                                .APP_USER_ATTRIBUTE))
                .orElseThrow(() -> new WorkshopException(WorkshopError.ACCOUNT_NOT_FOUND_ERROR));
        return securityAccount.getUsername();
    }

    public static String getPasswordByUsername(String username) {
        SecurityDao securityRepository = new JDBCSecurityDao();
        return securityRepository.findPasswordByUsername(username);
    }
}
