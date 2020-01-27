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

public class Users implements Command {

    private AccountService accountService = new AccountService();
    private RoleService roleService = new RoleService();

    static {
        new DOMConfigurator().doConfigure("src/log4j.xml", LogManager.getLoggerRepository());
    }
    private static Logger logger = Logger.getLogger(Users.class);
    private static Long DEFAULT_ID = 1L;
    private static String USER_INFO_PAGE = "/WEB-INF/jsp/admin/user.jsp";
    private static String USER_EDIT_PAGE = "/WEB-INF/jsp/admin/edit-user-form.jsp";

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response) {
        HttpSession session = request.getSession();
        String path = request.getRequestURI();
        path = path.replaceAll(".*/app/" , "");
        String[] uriParts = path.split("/");
        Long id;

        if (uriParts.length == 2)
           return createJSONUserList(request, response);
        id = UtilitiesClass.tryParse(uriParts[2], DEFAULT_ID);
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
                return USER_EDIT_PAGE;
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
        return USER_INFO_PAGE;
    }

    private String createJSONUserList(HttpServletRequest request,
                                     HttpServletResponse response){
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        logger.info("Send json page to client!");
        Pageable page = new Pageable();
        page.setPage(
                UtilitiesClass.tryParse(
                        request.getParameter("page"),
                        UtilitiesClass.DEFAULT_PAGE_VALUE));
        page.setSize(
                UtilitiesClass.tryParse(
                        request.getParameter("size"),
                        UtilitiesClass.DEFAULT_SIZE_VALUE));
        page.setSearch(
                UtilitiesClass.getUnicodeString(
                        request.getParameter("search")));
        page.setSorting(
                UtilitiesClass.getUnicodeString(
                        request.getParameter("sorting")));

        logger.info("page: " + page.toString());
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
