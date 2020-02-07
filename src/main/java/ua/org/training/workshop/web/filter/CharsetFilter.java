package ua.org.training.workshop.web.filter;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.utility.ApplicationConstants;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(filterName = "CharsetFilter", urlPatterns = {"/*"})
public class CharsetFilter implements Filter {
    private static Logger logger = Logger.getLogger(CharsetFilter.class);

    static {
        new DOMConfigurator().doConfigure(ApplicationConstants.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding(ApplicationConstants.APP_ENCODING);
        servletResponse.setCharacterEncoding(ApplicationConstants.APP_ENCODING);

        logger.debug("set charset encoding to UTF-8");

        filterChain.doFilter(servletRequest, servletResponse);
    }


    @Override
    public void destroy() {

    }
}
