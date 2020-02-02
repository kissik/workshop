package ua.org.training.workshop.servlet.command.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.domain.Account;
import ua.org.training.workshop.service.AccountService;
import ua.org.training.workshop.service.RequestService;
import ua.org.training.workshop.servlet.command.Command;
import ua.org.training.workshop.utilities.Pageable;
import ua.org.training.workshop.utilities.UtilitiesClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class UserRequests implements Command {

    private RequestService requestService = new RequestService();
    private AccountService accountService = new AccountService();

    static {
        new DOMConfigurator().doConfigure(UtilitiesClass.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }
    private static Logger logger = Logger.getLogger(UserRequests.class);

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response) {
        HttpSession session = request.getSession();
        String[] uriParts = UtilitiesClass.getUriParts(request);

        return createJSONRequestList(request, response);
    }

    private String createJSONRequestList(HttpServletRequest request,
                                     HttpServletResponse response){
        response.setContentType("application/json");
        response.setCharacterEncoding(UtilitiesClass.APP_ENCODING);
        logger.info("Send json page to user client!");
        Pageable page = UtilitiesClass.createPage(request);
        Account author = accountService.getAccountByUsername(
                UtilitiesClass.getCurrentUserName(request));
        String jsonString = requestService.getPageByLanguageAndAuthor(page, UtilitiesClass.getLanguageString(request), author);
        try {
            PrintWriter writer = response.getWriter();
            writer.print(jsonString);
            logger.info(jsonString);
        }catch(IOException e){
            logger.error("IO Exception : " + e.getMessage());
        }

        return UtilitiesClass.APP_STRING_DEFAULT_VALUE;
    }
}
