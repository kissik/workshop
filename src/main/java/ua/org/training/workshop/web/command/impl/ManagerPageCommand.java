package ua.org.training.workshop.web.command.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.service.AccountService;
import ua.org.training.workshop.utility.Utility;
import ua.org.training.workshop.web.command.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ManagerPageCommand implements Command {

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response) {
        return Pages.MANAGER_PAGE;
    }
}
