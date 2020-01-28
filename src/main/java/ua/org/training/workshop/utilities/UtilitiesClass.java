package ua.org.training.workshop.utilities;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java.nio.charset.StandardCharsets;

/**
 * @author kissik
 */
public class UtilitiesClass {

    public static final Long DEFAULT_PAGE_VALUE = 0L;
    public static final Long DEFAULT_SIZE_VALUE = 5L;
    public static final String LOG4J_XML_PATH = "src/log4j.xml";
    public static final String[] APP_ROLES = new String[]{
            "ADMIN",
            "MANAGER",
            "WORKMAN",
            "USER"};

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

    public static String getUnicodeString(String latin1) {
        try {
            return new String(latin1.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        } catch (Exception e) {
            logger.error("convert error : " + e.getMessage());
        }
        return "";
    }

}
