package ua.org.training.workshop.servlet;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.servlet.command.*;
import ua.org.training.workshop.servlet.command.impl.*;
import ua.org.training.workshop.servlet.command.impl.Exception;
import ua.org.training.workshop.domain.Account;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkshopServlet extends HttpServlet {
    private static String WELCOME_PAGE = "WEB-INF/jsp/welcome.jsp";
    static {
        new DOMConfigurator().doConfigure("src/log4j.xml", LogManager.getLoggerRepository());
    }
    static Logger logger = Logger.getLogger(WorkshopServlet.class);

    private Map<String, Command> commands = new HashMap<>();
    private List<Account> users;

    public void init(){
        commands.put("admin/page", new AdminPage());
        commands.put("access-denied", new AccessDenied());
        commands.put("manager/page", new ManagerPage());
        commands.put("workman/page", new WorkmanPage());
        commands.put("user/page", new UserPage());
        commands.put("admin/users", new Users());
        commands.put("logout", new LogOut());
        commands.put("login", new Login());
        commands.put("registration", new Registration());
        commands.put("exception" , new Exception());
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
        path = path.replaceAll(".*/app/" , "");
        if (path.contains("admin/users")) path="admin/users";
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
