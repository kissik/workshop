package ua.org.training.workshop.web.command.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.domain.Account;
import ua.org.training.workshop.domain.Request;
import ua.org.training.workshop.domain.Status;
import ua.org.training.workshop.exception.WorkshopException;
import ua.org.training.workshop.security.SecurityService;
import ua.org.training.workshop.service.AccountService;
import ua.org.training.workshop.service.RequestService;
import ua.org.training.workshop.service.StatusService;
import ua.org.training.workshop.utility.ApplicationConstants;
import ua.org.training.workshop.utility.Utility;
import ua.org.training.workshop.web.command.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NewRequestCommand implements Command {

    private final static Logger LOGGER = Logger.getLogger(NewRequestCommand.class);

    static {
        new DOMConfigurator().doConfigure(ApplicationConstants.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }

    private AccountService accountService = new AccountService();
    private RequestService requestService = new RequestService();
    private StatusService statusService = new StatusService();

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response) {
        try {
            Request newRequest = new Request();
            Account author = accountService.getAccountByUsername(
                    SecurityService.getCurrentUserName(request.getSession())
            );
            Status status = statusService.findByCode(
                    ApplicationConstants.REQUEST_DEFAULT_STATUS
            );
            newRequest.setTitle(
                    Utility
                            .getParameterString(request.getParameter(ApplicationConstants.RequestAttributes.REQUEST_TITLE_ATTRIBUTE),
                                    ApplicationConstants.APP_STRING_DEFAULT_VALUE));
            newRequest.setDescription(
                    Utility
                            .getParameterString(request.getParameter(ApplicationConstants.RequestAttributes.REQUEST_DESCRIPTION_ATTRIBUTE),
                                    ApplicationConstants.APP_STRING_DEFAULT_VALUE));
            newRequest.setStatus(status);
            newRequest.setAuthor(author);
            newRequest.setUser(author);
            newRequest.setClosed(status.isClose());
            newRequest.setLanguage(
                    Utility.getLanguageString(
                            Utility.getLocale(request)));
            LOGGER.info("new request form creation: " + newRequest.toString());
            requestService.createRequest(newRequest);
        } catch (WorkshopException e) {
            LOGGER.error("custom error message: " + e.getMessage());
        }
        clearRequestAttributes(request);
        return Pages.USER_PAGE_REDIRECT_NEW_REQUEST_SUCCESS;
    }

    @Override
    public void clearRequestAttributes(HttpServletRequest request) {
        request.getSession().removeAttribute(ApplicationConstants.RequestAttributes.REQUEST_DESCRIPTION_ATTRIBUTE);
        request.getSession().removeAttribute(ApplicationConstants.RequestAttributes.REQUEST_TITLE_ATTRIBUTE);
    }

}
