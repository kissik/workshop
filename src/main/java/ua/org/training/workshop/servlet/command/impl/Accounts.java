package ua.org.training.workshop.servlet.command.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.domain.Account;
import ua.org.training.workshop.domain.Role;
import ua.org.training.workshop.service.AccountService;
import ua.org.training.workshop.service.RoleService;
import ua.org.training.workshop.servlet.command.Command;
import ua.org.training.workshop.utilities.UtilitiesClass;
import ua.org.training.workshop.utilities.Pageable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class Accounts implements Command {

    private AccountService accountService = new AccountService();
    private RoleService roleService = new RoleService();

    static {
        new DOMConfigurator().doConfigure(UtilitiesClass.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }
    private static Logger logger = Logger.getLogger(Accounts.class);
    private static final Long DEFAULT_ID = 1L;

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response) {
        HttpSession session = request.getSession();
        String[] uriParts = UtilitiesClass.getUriParts(request);
        Long id;

        if (uriParts.length == 2)
           return createJSONUserList(request, response);
        id = UtilitiesClass.tryParseLong(uriParts[2], DEFAULT_ID);
        Account account = accountService.getAccountById(id);
        session.setAttribute("account", account);
        if (uriParts.length == 4 && uriParts[3].equals("edit")) {
            String[] roles = (String[]) account.getRoles()
                    .stream()
                    .map(Role::getCode)
                    .toArray(String[]::new);
            logger.info("available roles list : " + Arrays.toString(roles));
            session.setAttribute("roles", roles);
            session.setAttribute("rolesList", roleService.findAll());
            if (request.getMethod().equals("GET")) {
                logger.debug("Edit user form was send");
                return Pages.USER_EDIT_PAGE;
            }
            else{
                logger.debug(request.getParameterValues("role"));
                logger.debug("Try to post my changed users roles!");
                Collection<Role> newRoles = new HashSet<>();
                for (String role : request.getParameterValues("role")) {
                    newRoles.add(roleService.findByCode(role));
                    logger.info("new role was added : " + role);
                }
                account.setRoles(newRoles);
                accountService.saveAccountRoles(account);
            }
        }
        return Pages.USER_INFO_PAGE;
    }

    private String createJSONUserList(HttpServletRequest request,
                                     HttpServletResponse response){
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        logger.info("Send json page to client!");

        Pageable page = UtilitiesClass.createPage(request);

        String jsonString = accountService.getPage(page);
        try {
            PrintWriter writer = response.getWriter();
            writer.print(jsonString);
            logger.info(jsonString);
        }catch(IOException e){
            logger.error("IO Exception : " + e.getMessage());
        }

        return "";
    }
}
