package ua.org.training.workshop.security;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.utilities.UtilitiesClass;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author kissik
 */
public class ProcessingLoggedUsers {

    static {
        new DOMConfigurator().doConfigure(UtilitiesClass.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }
    private static Logger logger = Logger.getLogger(ProcessingLoggedUsers.class);

    public static AccountSecurity loadAccountSecurity(HttpServletRequest request) {
        return (AccountSecurity) Optional
                .ofNullable(
                        request.getSession().getAttribute(UtilitiesClass.APP_USER_ATTRIBUTE))
                .orElse(AccountSecurity.ACCOUNT);
    }

    private static HashSet<String> getLoggedUsers(HttpServletRequest request){
        return Optional
                .ofNullable(
                        (HashSet<String>) request
                                .getServletContext()
                                .getAttribute(UtilitiesClass
                                        .APP_LOGGED_USERS_HASH_SET_ATTRIBUTE))
                .orElse(new HashSet<>());
    }

    private static void saveLoggedUser(HttpServletRequest request, Set<String> loggedUsers){
        request.getServletContext()
                .setAttribute(UtilitiesClass
                        .APP_LOGGED_USERS_HASH_SET_ATTRIBUTE, loggedUsers);
        logger.info("logged users : " + loggedUsers);
    }

    public static boolean checkUserIsLogged(HttpServletRequest request, String username){
        Set<String> loggedUsers = getLoggedUsers(request);
        logger.debug("try to check logged users!");
        if (loggedUsers.stream().anyMatch(username::equals)){
            logger.info("logged users : " + loggedUsers);
            return true;
        }
        return false;
    }

    public static void addLoggedUser(HttpServletRequest request, AccountSecurity account){
        Set<String> loggedUsers = getLoggedUsers(request);
        loggedUsers.add(account.getUsername());
        request.getSession().setAttribute(UtilitiesClass.APP_USER_ATTRIBUTE, account);
        saveLoggedUser(request, loggedUsers);
    }

    public static void removeLoggedUser(HttpServletRequest request, String username){
        Set<String> loggedUsers = getLoggedUsers(request);
        loggedUsers.remove(username);
        request.getSession().setAttribute(UtilitiesClass.APP_USER_ATTRIBUTE, null);
        saveLoggedUser(request, loggedUsers);
    }

}
