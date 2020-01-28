package ua.org.training.workshop.servlet.filters;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.utilities.UtilitiesClass;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(filterName = "CharsetFilter", urlPatterns = {"/*"})
public class CharsetFilter implements Filter {
    static {
        new DOMConfigurator().doConfigure(UtilitiesClass.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }

    private static Logger logger = Logger.getLogger(CharsetFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding("UTF-8");
        servletResponse.setCharacterEncoding("UTF-8");

        logger.debug("set charset encoding to UTF-8");

        filterChain.doFilter(servletRequest, servletResponse);
    }


    @Override
    public void destroy() {

    }
}
