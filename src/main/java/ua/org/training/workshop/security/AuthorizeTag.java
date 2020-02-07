package ua.org.training.workshop.security;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.utility.ApplicationConstants;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

/**
 * @author kissik
 */
public class AuthorizeTag extends TagSupport {
    private final static Logger LOGGER = Logger.getLogger(AuthorizeTag.class);

    static {
        new DOMConfigurator().doConfigure(ApplicationConstants.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }

    private String access;

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access.replace("(", "").replace(")", "");
    }

    @Override
    public int doStartTag() throws JspException {

        SecurityAccount securityAccount = Optional.ofNullable(
                (SecurityAccount) pageContext.findAttribute(
                        ApplicationConstants
                                .RequestAttributes
                                .APP_USER_ATTRIBUTE))
                .orElse(SecurityAccount.ACCOUNT);

        LOGGER.debug(securityAccount.toString());
        try {
            if (getAccess().contains("'")) {
                if ((boolean) SecurityAccount.class.getMethod(
                        getAccess().split("'")[0],
                        String.class).invoke(securityAccount, getAccess().split("'")[1]))
                    return EVAL_BODY_INCLUDE;
            } else if ((boolean) SecurityAccount.class.getMethod(getAccess()).invoke(securityAccount))
                return EVAL_BODY_INCLUDE;

        } catch (NoSuchMethodException e) {
            LOGGER.debug("No such method exception: " + e.getMessage());
        } catch (IllegalAccessException e) {
            LOGGER.debug("Illegal Access Exception: " + e.getMessage());
        } catch (InvocationTargetException e) {
            LOGGER.debug("Invocation Target Exception: " + e.getMessage());
        }

        return SKIP_BODY;
    }

}
