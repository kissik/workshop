package ua.org.training.workshop.web.filter;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.security.SecurityAccount;
import ua.org.training.workshop.security.SecurityService;
import ua.org.training.workshop.utility.ApplicationConstants;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "AuthFilter", urlPatterns = {"/*"})
public class AuthFilter implements Filter {
    private static Logger logger = Logger.getLogger(AuthFilter.class);

    static {
        new DOMConfigurator().doConfigure(ApplicationConstants.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }

    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {

        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession();

        String path = req.getRequestURI();
        SecurityAccount account = SecurityService.loadAccountSecurity(req);

        /*TODO http status code from source*/
        if (!checkAccess(path, account)) {
            logger.debug("access-denied filter action!" + account.getUsername());
            logger.debug(account.getUsername() + " invalid path : " + path);
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            SecurityService.removeLoggedUser(req, account.getUsername());
            session.setAttribute(ApplicationConstants
                            .RequestAttributes.APP_MESSAGE_ATTRIBUTE,
                    ApplicationConstants.BUNDLE_ACCESS_DENIED_MESSAGE);
            return;
        } else filterChain.doFilter(request, response);
    }

    /*TODO stream*/
    private boolean checkAccess(String path, SecurityAccount account) {

        for (String role : ApplicationConstants.APP_ROLES)
            if (path.contains(role.toLowerCase()))
                return account != null && account.hasRole(role);

        return true;
    }

    public void destroy() {
    }

    public void init(FilterConfig arg0) throws ServletException {
    }
}
