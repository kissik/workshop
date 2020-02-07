package ua.org.training.workshop.utility;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author kissik
 */
public class Utility implements ApplicationConstants {

    private static final String APP_PROPERTIES_BUNDLE_NAME = "application";

    private final static Logger LOGGER = Logger.getLogger(Utility.class);

    static {
        new DOMConfigurator().doConfigure(LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }

    public static Long tryParseLong(String value, Long defaultValue) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            LOGGER.error("Number format exception : " + e.getMessage());
            LOGGER.info("set default value = " + defaultValue);
        }
        return defaultValue;
    }

    public static Integer tryParseInteger(String value, Integer defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            LOGGER.error("Number format exception : " + e.getMessage());
            LOGGER.info("set default value = " + defaultValue);
        }
        return defaultValue;
    }

    public static String getBundleMessage(Locale locale, String bundleString) {
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
        path = path.replaceAll(APP_PATH_REG_EXP, APP_STRING_DEFAULT_VALUE);
        return path.split("/");
    }

    public static String getUTF8String(String latin1String, String appStringDefaultValue) {
        try {
            return new String(latin1String.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        } catch (Exception e) {
            LOGGER.error("error convert : " + e.getMessage());
            return appStringDefaultValue;
        }
    }

    public static String getLanguageString(Locale locale) {
        return getBundleMessage(
                locale,
                BUNDLE_LANGUAGE_FOR_REQUEST);
    }

    static String getCurrentLanguage(HttpServletRequest request) {
        return getParameterString(
                (String) request
                        .getSession()
                        .getAttribute(
                                ApplicationConstants
                                        .RequestAttributes
                                        .APP_LANG_ATTRIBUTE),
                APP_DEFAULT_LANGUAGE);
    }

    public static Locale getLocale(HttpServletRequest request) {
        return new Locale(getCurrentLanguage(request));
    }

    public static String getLocaleDate(Locale locale, LocalDate dateCreated) {
        return APP_STRING_DEFAULT_VALUE;
    }

    public static String getLocalePrice(Locale locale, BigDecimal price) {
        String result = getBundleMessage(locale, BUNDLE_CURRENCY_STRING);
        Integer rate = tryParseInteger(getBundleMessage(locale, BUNDLE_CURRENCY_RATE_INTEGER),
                APP_DEFAULT_PRICE);
        result = price.multiply(BigDecimal.valueOf(rate)) + " " + result;
        return result;
    }

    public static String getApplicationProperty(String property) {
        ResourceBundle bundle = ResourceBundle.getBundle(
                APP_PROPERTIES_BUNDLE_NAME,
                new Locale(APP_DEFAULT_LANGUAGE));
        return bundle.getString(property);
    }
}
