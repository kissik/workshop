package ua.org.training.workshop.servlet.filters;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.domain.Account;
import ua.org.training.workshop.security.AccountSecurity;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "AuthFilter", urlPatterns = {"/*"})
public class AuthFilter implements Filter {
    static final String[] roles = new String[]{
            "ADMIN",
            "MANAGER",
            "WORKMAN",
            "USER"};

    static {
        new DOMConfigurator().doConfigure("src/log4j.xml", LogManager.getLoggerRepository());
    }

    static Logger logger = Logger.getLogger(AuthFilter.class);

    public void doFilter(ServletRequest request,
                     ServletResponse response,
                     FilterChain filterChain) throws IOException, ServletException {

    final HttpServletRequest req = (HttpServletRequest) request;
    final HttpServletResponse res = (HttpServletResponse) response;

    HttpSession session = req.getSession();

    logger.debug(session);
    logger.debug(session.getAttribute("user"));

    String path = req.getRequestURI();

    AccountSecurity account = (AccountSecurity) session.getAttribute("user");
    if (!checkAccess(path, account)) {
        logger.debug("access-denied filter action!" + session.getAttribute("user"));
        res.sendError(403);
        session.setAttribute("user", null);
        return;
    }
    else filterChain.doFilter(request,response);
    }

    private boolean checkAccess(String path, AccountSecurity account) {

        for(String role : roles)
            if (path.contains(role.toLowerCase()))
                return account!=null && account.hasRole(role);

        return true;
    }

    public void destroy() {}
    public void init(FilterConfig arg0) throws ServletException {}
}
