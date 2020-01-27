package ua.org.training.workshop.servlet.listener;

import ua.org.training.workshop.security.AccountSecurity;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.HashSet;

public class SessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        HashSet<String> loggedUsers = (HashSet<String>) httpSessionEvent
                .getSession()
                .getServletContext()
                .getAttribute("loggedUsers");
        AccountSecurity account = (AccountSecurity) httpSessionEvent.getSession().getAttribute("user");
        loggedUsers.remove(account.getUsername());
        httpSessionEvent
                .getSession()
                .getServletContext()
                .setAttribute("loggedUsers", loggedUsers);
    }
}
