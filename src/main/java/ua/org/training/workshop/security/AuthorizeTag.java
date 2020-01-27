package ua.org.training.workshop.security;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

public class AuthorizeTag extends TagSupport {
    static {
        new DOMConfigurator().doConfigure("src/log4j.xml", LogManager.getLoggerRepository());
    }
    static Logger logger = Logger.getLogger(AuthorizeTag.class);

    private String access;

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access.replace("(","").replace(")","");
    }

    @Override
    public int doStartTag() throws JspException{

        AccountSecurity account = Optional.ofNullable(
                (AccountSecurity) pageContext.findAttribute("user"))
                .orElse(AccountSecurity.ACCOUNT);

        logger.debug(account.toString());
        try {
            //Method method = account.getClass().getMethod(getAccess());
            if (getAccess().contains("'")){
                if ((boolean)AccountSecurity.class.getMethod(
                        getAccess().split("'")[0],
                        String.class).invoke(account, getAccess().split("'")[1]))
                    return EVAL_BODY_INCLUDE;
            }else if ((boolean)AccountSecurity.class.getMethod(getAccess()).invoke(account))
                return EVAL_BODY_INCLUDE;

        }catch (NoSuchMethodException e){
            logger.debug("No such method exception: " + e.getMessage());
        } catch (IllegalAccessException e) {
            logger.debug("Illegal Access Exception: " + e.getMessage());
        } catch (InvocationTargetException e) {
            logger.debug("Invocation Target Exception: " + e.getMessage());
        }

        return SKIP_BODY;
    }

}
