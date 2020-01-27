package ua.org.training.workshop.servlet.filters;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.apache.log4j.LogManager;
import org.apache.log4j.xml.DOMConfigurator;

@WebFilter(filterName = "SessionLocaleFilter", urlPatterns = {"/*"})
public class SessionLocaleFilter implements Filter {
    static {
        new DOMConfigurator().doConfigure("src/log4j.xml",LogManager.getLoggerRepository());
    }
    static Logger logger = Logger.getLogger(SessionLocaleFilter.class);
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        if ((req.getParameter("lang") != null) && (!req.getParameter("lang").equals(req.getSession().getAttribute("lang")))) {
            req.getSession().setAttribute("lang", req.getParameter("lang"));
            logger.debug("locale was changed on: " + req.getParameter("lang").toString());
        }

        chain.doFilter(request, response);
    }
    public void destroy() {}
    public void init(FilterConfig arg0) throws ServletException {}
}
