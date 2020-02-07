package ua.org.training.workshop.web.command.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.domain.Account;
import ua.org.training.workshop.domain.Role;
import ua.org.training.workshop.service.AccountService;
import ua.org.training.workshop.service.RoleService;
import ua.org.training.workshop.utility.ApplicationConstants;
import ua.org.training.workshop.utility.Page;
import ua.org.training.workshop.utility.PageService;
import ua.org.training.workshop.utility.Utility;
import ua.org.training.workshop.web.command.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class AccountsCommand implements Command {

    private final static Logger LOGGER = Logger.getLogger(AccountsCommand.class);
    private final static Long DEFAULT_ID = 1L;

    static {
        new DOMConfigurator().doConfigure(ApplicationConstants.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }

    private AccountService accountService = new AccountService();
    private RoleService roleService = new RoleService();

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response) {
        HttpSession session = request.getSession();
        String[] uriParts = Utility.getUriParts(request);
        Long id;

        if (uriParts.length == 2)
            return createJSONUserList(request, response);
        id = Utility.tryParseLong(uriParts[2], DEFAULT_ID);
        Account account = accountService.getAccountById(id);
        session.setAttribute("account", account);
        if (uriParts.length == 4 && uriParts[3].equals("edit")) {
            String[] roles = (String[]) account.getRoles()
                    .stream()
                    .map(Role::getCode)
                    .toArray(String[]::new);
            LOGGER.info("available roles list : " + Arrays.toString(roles));
            session.setAttribute("roles", roles);
            session.setAttribute("rolesList", roleService.findAll());
            if (request.getMethod().equals("GET")) {
                LOGGER.debug("Edit user form was send");
                return Pages.USER_EDIT_PAGE;
            } else {
                LOGGER.debug(request.getParameterValues("role"));
                LOGGER.debug("Try to post my changed users roles!");
                Collection<Role> newRoles = new HashSet<>();
                for (String role : request.getParameterValues("role")) {
                    newRoles.add(roleService.findByCode(role));
                    LOGGER.info("new role was added : " + role);
                }
                account.setRoles(newRoles);
                accountService.saveAccountRoles(account);
            }
        }
        return Pages.USER_INFO_PAGE;
    }

    private String createJSONUserList(HttpServletRequest request,
                                      HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        LOGGER.info("Send json page to client!");

        Page page = PageService.createPage(request);

        String jsonString = accountService.getPage(page);
        try {
            PrintWriter writer = response.getWriter();
            writer.print(jsonString);
            LOGGER.info(jsonString);
        } catch (IOException e) {
            LOGGER.error("IO Exception : " + e.getMessage());
        }

        return "";
    }
}
