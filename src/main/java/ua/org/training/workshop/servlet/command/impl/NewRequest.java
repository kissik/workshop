package ua.org.training.workshop.servlet.command.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.domain.Account;
import ua.org.training.workshop.domain.Request;
import ua.org.training.workshop.domain.Status;
import ua.org.training.workshop.exception.WorkshopException;
import ua.org.training.workshop.service.AccountService;
import ua.org.training.workshop.service.RequestService;
import ua.org.training.workshop.service.StatusService;
import ua.org.training.workshop.servlet.command.Command;
import ua.org.training.workshop.utilities.UtilitiesClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;

public class NewRequest implements Command {

    private AccountService accountService = new AccountService();
    private RequestService requestService = new RequestService();
    private StatusService statusService = new StatusService();

    static {
        new DOMConfigurator().doConfigure(UtilitiesClass.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }
    private static Logger logger = Logger.getLogger(NewRequest.class);

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response) {
        HttpSession session = request.getSession();
        String[] uriParts = UtilitiesClass.getUriParts(request);

        try {
            Request newRequest = new Request();
            Account author = accountService.getAccountByUsername(
                    UtilitiesClass.getCurrentUserName(request)
            );
            Status status = statusService.findByCode(
                    UtilitiesClass.REQUEST_DEFAULT_STATUS
            );
            newRequest.setTitle(
                    UtilitiesClass
                            .getParameterString(request.getParameter(UtilitiesClass.REQUEST_TITLE_ATTRIBUTE),
                                    UtilitiesClass.APP_STRING_DEFAULT_VALUE));
            newRequest.setDescription(
                    UtilitiesClass
                            .getParameterString(request.getParameter(UtilitiesClass.REQUEST_DESCRIPTION_ATTRIBUTE),
                                    UtilitiesClass.APP_STRING_DEFAULT_VALUE));
            newRequest.setStatus(status);
            newRequest.setAuthor(author);
            newRequest.setUser(author);
            newRequest.setClosed(status.isClose());
            newRequest.setLanguage(
                    UtilitiesClass.getLanguageString(
                            UtilitiesClass.getLocale(request)));
            logger.info("new request form creation: " + newRequest.toString());
            requestService.createRequest(newRequest);
        }
        catch(WorkshopException e){
            logger.error("custom error message: " + e.getMessage());
        }

        return Pages.USER_PAGE_REDIRECT_NEW_REQUEST_SUCCESSED;
    }

}
