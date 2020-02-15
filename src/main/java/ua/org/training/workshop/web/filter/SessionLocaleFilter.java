package ua.org.training.workshop.web.filter;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.utility.ApplicationConstants;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author kissik
 */
@WebFilter(filterName = "SessionLocaleFilter", urlPatterns = {"/*"})
public class SessionLocaleFilter implements Filter {

    private final static Logger LOGGER = Logger.getLogger(SessionLocaleFilter.class);

    static {
        new DOMConfigurator().doConfigure(ApplicationConstants.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String languageAttribute = ApplicationConstants
                .RequestAttributes
                .APP_LANG_ATTRIBUTE;
        if ((req.getParameter(languageAttribute) != null) &&
                (!req.getParameter(languageAttribute)
                        .equals(req.getSession().getAttribute(languageAttribute)))) {
            req.getSession().setAttribute(languageAttribute,
                    req.getParameter(languageAttribute));
            LOGGER.debug("locale was changed on: " + req.getParameter(languageAttribute));
        }

        chain.doFilter(request, response);
    }

    public void destroy() {
    }

    public void init(FilterConfig arg0) throws ServletException {
    }
}
