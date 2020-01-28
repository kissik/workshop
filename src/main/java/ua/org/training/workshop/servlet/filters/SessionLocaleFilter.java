package ua.org.training.workshop.servlet.filters;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.apache.log4j.LogManager;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.utilities.UtilitiesClass;

/**
 * @author kissik
 */
@WebFilter(filterName = "SessionLocaleFilter", urlPatterns = {"/*"})
public class SessionLocaleFilter implements Filter {
    public final static String LANG_ATTRIBUTE = "lang";
    static {
        new DOMConfigurator().doConfigure(UtilitiesClass.LOG4J_XML_PATH,LogManager.getLoggerRepository());
    }
    private static Logger logger = Logger.getLogger(SessionLocaleFilter.class);
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        if ((req.getParameter(LANG_ATTRIBUTE) != null) && (!req.getParameter(LANG_ATTRIBUTE).equals(req.getSession().getAttribute(LANG_ATTRIBUTE)))) {
            req.getSession().setAttribute(LANG_ATTRIBUTE, req.getParameter(LANG_ATTRIBUTE));
            logger.debug("locale was changed on: " + req.getParameter(LANG_ATTRIBUTE).toString());
        }

        chain.doFilter(request, response);
    }
    public void destroy() {}
    public void init(FilterConfig arg0) throws ServletException {}
}
