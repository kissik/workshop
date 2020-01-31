package ua.org.training.workshop.utilities;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.exception.WorkshopErrors;
import ua.org.training.workshop.exception.WorkshopException;
import ua.org.training.workshop.security.AccountSecurity;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author kissik
 */
public class UtilitiesClass {

    public static final String ACCOUNT_CONFIRM_PASSWORD_ATTRIBUTE = "confirmPassword";
    public static final String ACCOUNT_FIRST_NAME_ATTRIBUTE = "firstName";
    public static final String ACCOUNT_FIRST_NAME_ORIGIN_ATTRIBUTE = "firstNameOrigin";
    public static final String ACCOUNT_EMAIL_ATTRIBUTE = "email";
    public static final String ACCOUNT_LAST_NAME_ATTRIBUTE = "lastName";
    public static final String ACCOUNT_LAST_NAME_ORIGIN_ATTRIBUTE = "lastNameOrigin";
    public static final String ACCOUNT_PHONE_ATTRIBUTE = "phone";

    public static final String APP_ANONYMOUS_ACCOUNT_USERNAME = "anonymous";
    public static final int APP_BCRYPT_SALT = 11;
    public static final String APP_ENCODING = "UTF-8";
    public static final String APP_ERROR_ATTRIBUTE = "error";
    public static final String APP_DEFAULT_LANGUAGE = "en";
    public final static String APP_LANG_ATTRIBUTE = "lang";
    public static final String APP_LOGGED_USERS_HASH_SET_ATTRIBUTE = "loggedUsers";
    public static final String APP_MESSAGES_BUNDLE_NAME = "messages";
    public static final String APP_MESSAGE_ATTRIBUTE = "message";
    public static final String APP_PASSWORD_ATTRIBUTE = "password";
    public static final String APP_PATH_REG_EXP = ".*/app/";
    public static final String APP_STRING_DEFAULT_VALUE = "";
    public static final String APP_USER_ATTRIBUTE = "user";
    public static final String APP_USERNAME_ATTRIBUTE = "username";
    public static final String[] APP_ROLES = new String[]{
            "ADMIN",
            "MANAGER",
            "WORKMAN",
            "USER"};

    public static final String BUNDLE_ACCESS_DENIED_LOGGED_USERS = "access.denied.logged.user";
    public static final String BUNDLE_ACCESS_DENIED_MESSAGE = "access.denied.message";
    public static final String BUNDLE_LANGUAGE_FOR_REQUEST = "locale.string";

    public static final String LOG4J_XML_PATH = "src/log4j.xml";
    public static final String ROLE_QUERY_DEFAULT_PREFIX = "r";
    public static final String STATUS_QUERY_DEFAULT_PREFIX = "s";
    public static final String ACCOUNT_QUERY_DEFAULT_PREFIX = "u";
    public static final String REQUEST_QUERY_DEFAULT_PREFIX = "r";
    public static final String REQUEST_AUTHOR_QUERY_DEFAULT_PREFIX = "a";
    public static final String REQUEST_USER_QUERY_DEFAULT_PREFIX = "u";

    private static final Long PAGEABLE_PAGE_DEFAULT_VALUE = 0L;
    private static final String PAGEABLE_PAGE_ATTRIBUTE = "page";
    private static final String PAGEABLE_SEARCH_ATTRIBUTE = "search";
    private static final Long PAGEABLE_SIZE_DEFAULT_VALUE = 5L;
    private static final String PAGEABLE_SIZE_ATTRIBUTE = "size";
    private static final String PAGEABLE_SORTING_ATTRIBUTE = "sorting";

    public static final String REQUEST_DESCRIPTION_ATTRIBUTE = "description";
    public static final String REQUEST_DEFAULT_STATUS = "REGISTER";
    public static final String REQUEST_TITLE_ATTRIBUTE = "title";

    static {
        new DOMConfigurator().doConfigure(LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }
    private static Logger logger = Logger.getLogger(UtilitiesClass.class);
    public static Long tryParse(String value, Long defaultValue){
        try{
            return Long.parseLong(value);
        }
        catch(NumberFormatException e){
            logger.error("Number format exception : " + e.getMessage());
            logger.info("set default value = " + defaultValue);
        }
        return defaultValue;
    }

    public static String getBundleMessage(Locale locale, String bundleString){
        ResourceBundle bundle = ResourceBundle.getBundle(
                APP_MESSAGES_BUNDLE_NAME,
                locale);
        return bundle.getString(bundleString);
    }

    public static String getParameterString(String propertyString, String defaultValue) {
        return Optional.ofNullable(propertyString).orElse(defaultValue);
    }

    public static String[] getUriParts(HttpServletRequest request) {

        String path = request.getRequestURI();
        path = path.replaceAll(APP_PATH_REG_EXP , APP_STRING_DEFAULT_VALUE);
        return path.split("/");
    }

    public static Pageable createPage(HttpServletRequest request) {
        Pageable page = new Pageable();
        page.setPageNumber(
                tryParse(
                        request.getParameter(PAGEABLE_PAGE_ATTRIBUTE),
                        PAGEABLE_PAGE_DEFAULT_VALUE));
        page.setSize(
                tryParse(
                        request.getParameter(PAGEABLE_SIZE_ATTRIBUTE),
                        PAGEABLE_SIZE_DEFAULT_VALUE));
        page.setSearch(
                getParameterString(
                        request.getParameter(PAGEABLE_SEARCH_ATTRIBUTE),
                        APP_STRING_DEFAULT_VALUE));
        page.setSorting(
                getParameterString(
                        request.getParameter(PAGEABLE_SORTING_ATTRIBUTE),
                        APP_STRING_DEFAULT_VALUE));

        logger.info("page: " + page.toString());
        return page;
    }

    public static String getCurrentUserName(HttpServletRequest request) {
        AccountSecurity accountSecurity = (AccountSecurity) Optional
                .ofNullable(
                        request.getSession().getAttribute(APP_USER_ATTRIBUTE))
                .orElseThrow(() -> new WorkshopException(WorkshopErrors.ACCOUNT_NOT_FOUND_ERROR));
        return accountSecurity.getUsername();
    }

    public static String getUTF8String(String latin1String, String appStringDefaultValue) {
        try {
            return new String(latin1String.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        }
        catch(Exception e){
            logger.error("error convert : " + e.getMessage());
            return appStringDefaultValue;
        }
    }
}
