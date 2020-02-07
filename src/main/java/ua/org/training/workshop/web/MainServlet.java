package ua.org.training.workshop.web;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.utility.ApplicationConstants;
import ua.org.training.workshop.web.command.Command;
import ua.org.training.workshop.web.command.impl.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kissik
 */
public class MainServlet extends HttpServlet {

    static {
        new DOMConfigurator().doConfigure(ApplicationConstants.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }
    private final static Logger LOGGER = Logger.getLogger(MainServlet.class);
    private Map<String, Command> commands = new HashMap<>();
    public void init() {
        commands.put("access-denied", new AccessDeniedCommand());
        commands.put("admin/page", new AdminPageCommand());
        commands.put("admin/accounts", new AccountsCommand());
        commands.put("exception", new ExceptionCommand());
        commands.put("login", new LogInCommand());
        commands.put("logout", new LogOutCommand());
        commands.put("manager/page", new ManagerPageCommand());
        commands.put("manager/requests", new ManagerRequestsCommand());
        commands.put("registration", new RegistrationCommand());
        commands.put("user/page", new UserPageCommand());
        commands.put("user/history-requests", new UserHistoryRequestsCommand());
        commands.put("user/requests", new UserRequestsCommand());
        commands.put("user/new-request", new NewRequestCommand());
        commands.put("workman/page", new WorkmanPageCommand());
        commands.put("workman/requests", new WorkmanRequestsCommand());
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws IOException, ServletException {
        processRequest(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String page;

        LOGGER.debug(path);
        path = path.replaceAll(ApplicationConstants.APP_PATH_REG_EXP, "");
        if (path.contains("admin/accounts")) path = "admin/accounts";
        LOGGER.debug(path);

        Command command = commands.getOrDefault(path,
                (req, res) -> "/index.jsp");
        page = command.execute(request, response);

        if (page.contains("redirect:")) {
            LOGGER.debug("page contains 'redirect:'" + page);
            response.sendRedirect(page.replace("redirect:", "/"));
        } else if (!page.equals("")) {
            LOGGER.debug("page does not contain 'redirect:'" + page);
            request.getRequestDispatcher(page).forward(request, response);
        }
    }
}
