package ua.org.training.workshop.web.command.impl;

import ua.org.training.workshop.web.command.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserPageCommand implements Command {

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response) {
        return Pages.USER_PAGE;
    }

    @Override
    public void clearRequestAttributes(HttpServletRequest request) {
    }
}
