package ua.org.training.workshop.servlet.listener;

import ua.org.training.workshop.security.AccountSecurity;
import ua.org.training.workshop.security.ProcessingLoggedUsers;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        AccountSecurity account = (AccountSecurity) httpSessionEvent.getSession().getAttribute("user");
        ProcessingLoggedUsers.removeLoggedUserContext(httpSessionEvent
                .getSession()
                .getServletContext(),
                account.getUsername()
        );
        httpSessionEvent.getSession().setAttribute("user",null);
    }
}
