package ua.org.training.workshop.web.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Command {
    String execute(HttpServletRequest request, HttpServletResponse response);
    void clearRequestAttributes(HttpServletRequest request);
}

