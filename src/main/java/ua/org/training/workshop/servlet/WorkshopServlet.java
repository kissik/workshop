package ua.org.training.workshop.servlet;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.servlet.command.*;
import ua.org.training.workshop.servlet.command.impl.*;
import ua.org.training.workshop.servlet.command.impl.Exception;
import ua.org.training.workshop.domain.Account;
import ua.org.training.workshop.utilities.UtilitiesClass;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kissik
 */
public class WorkshopServlet extends HttpServlet {
    static {
        new DOMConfigurator().doConfigure(UtilitiesClass.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }
    private static Logger logger = Logger.getLogger(WorkshopServlet.class);

    private Map<String, Command> commands = new HashMap<>();

    public void init(){
        commands.put("access-denied", new AccessDenied());
        commands.put("admin/page", new AdminPage());
        commands.put("admin/accounts", new Accounts());
        commands.put("exception" , new Exception());
        commands.put("login", new Login());
        commands.put("logout", new LogOut());
        commands.put("manager/page", new ManagerPage());
        commands.put("registration", new Registration());
        commands.put("user/page", new UserPage());
        commands.put("user/requests", new Requests());
        commands.put("user/new-request", new NewRequest());
        commands.put("workman/page", new WorkmanPage());
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws IOException, ServletException {
        processRequest(request, response);
/*
        httpServletRequest
                .getRequestDispatcher(WELCOME_PAGE)
                .forward(httpServletRequest, httpServletResponse);*/
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String page;

        logger.debug(path);
        path = path.replaceAll(UtilitiesClass.APP_PATH_REG_EXP , "");
        if (path.contains("admin/accounts")) path="admin/accounts";
        logger.debug(path);

        Command command = commands.getOrDefault(path,
                    (req, res) -> "/index.jsp)");
        page = command.execute(request, response);

        if (page.contains("redirect:")) {
                logger.debug("page contains 'redirect:'" + page);
                response.sendRedirect(page.replace("redirect:", "/"));
            } else
                if (!page.equals("")){
                logger.debug("page does not contain 'redirect:'" + page);
                request.getRequestDispatcher(page).forward(request, response);
            }

    }

}
