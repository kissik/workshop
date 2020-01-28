package ua.org.training.workshop.servlet.filters;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.security.AccountSecurity;
import ua.org.training.workshop.security.ProcessingLoggedUsers;
import ua.org.training.workshop.servlet.command.impl.AccessDenied;
import ua.org.training.workshop.utilities.UtilitiesClass;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@WebFilter(filterName = "AuthFilter", urlPatterns = {"/*"})
public class AuthFilter implements Filter {
    static {
        new DOMConfigurator().doConfigure(UtilitiesClass.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }

    private static Logger logger = Logger.getLogger(AuthFilter.class);

    public void doFilter(ServletRequest request,
                     ServletResponse response,
                     FilterChain filterChain) throws IOException, ServletException {

    final HttpServletRequest req = (HttpServletRequest) request;
    final HttpServletResponse res = (HttpServletResponse) response;

    HttpSession session = req.getSession();

    logger.debug(session);
    logger.debug(session.getAttribute("user"));

    String path = req.getRequestURI();

    AccountSecurity account = Optional
            .ofNullable(
                (AccountSecurity) session.getAttribute("user"))
            .orElse(AccountSecurity.ACCOUNT);

    if (!checkAccess(path, account)) {
        logger.debug("access-denied filter action!" + account.getUsername());
        logger.debug(account.getUsername() + " invalid path : " + path);
        res.sendError(403);
        ProcessingLoggedUsers.removeLoggedUser(req, account.getUsername());
        session.setAttribute(AccessDenied.MESSAGE_ATTRIBUTE,AccessDenied.MESSAGE);
        return;
    }
    else filterChain.doFilter(request,response);
    }

    private boolean checkAccess(String path, AccountSecurity account) {

        for(String role : UtilitiesClass.APP_ROLES)
            if (path.contains(role.toLowerCase()))
                return account!=null && account.hasRole(role);

        return true;
    }

    public void destroy() {}
    public void init(FilterConfig arg0) throws ServletException {}
}
