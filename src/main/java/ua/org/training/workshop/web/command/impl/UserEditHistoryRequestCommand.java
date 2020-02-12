package ua.org.training.workshop.web.command.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.domain.HistoryRequest;
import ua.org.training.workshop.exception.WorkshopException;
import ua.org.training.workshop.security.SecurityService;
import ua.org.training.workshop.service.AccountService;
import ua.org.training.workshop.service.HistoryRequestService;
import ua.org.training.workshop.utility.ApplicationConstants;
import ua.org.training.workshop.utility.Utility;
import ua.org.training.workshop.web.command.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserEditHistoryRequestCommand implements Command {

    private final static Logger LOGGER = Logger.getLogger(UserEditHistoryRequestCommand.class);

    static {
        new DOMConfigurator().doConfigure(ApplicationConstants.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }

    private AccountService accountService = new AccountService();
    private HistoryRequestService historyRequestService = new HistoryRequestService();

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response) {
        Long id = Utility.tryParseLong(request.getParameter(ApplicationConstants.RequestAttributes.HISTORY_REQUEST_ID_ATTRIBUTE),
                        ApplicationConstants.APP_DEFAULT_ID);
        try {
            HistoryRequest editHistoryRequest = historyRequestService.getHistoryRequestById(id);
            LOGGER.info("upload history request : " + editHistoryRequest.getTitle());
            LOGGER.info("upload history request user : " + editHistoryRequest.getAuthor().getUsername());

            if (editHistoryRequest.getAuthor().getUsername().equals(SecurityService.getCurrentUserName(request.getSession()))) {
                LOGGER.info("this user has rights to change this history request");
                editHistoryRequest.setReview(
                        Utility.getParameterString(
                                request.getParameter(ApplicationConstants
                                        .RequestAttributes.HISTORY_REQUEST_REVIEW_ATTRIBUTE),
                        ApplicationConstants.APP_STRING_DEFAULT_VALUE));
                editHistoryRequest.setRating(Utility.tryParseLong(
                        request.getParameter(ApplicationConstants
                                .RequestAttributes.HISTORY_REQUEST_RATING_ATTRIBUTE),
                        ApplicationConstants.APP_DEFAULT_RATING_VALUE));
                historyRequestService.update(editHistoryRequest);
            }
        } catch (WorkshopException e) {
            LOGGER.error("custom error message: " + e.getMessage());
        }
        clearRequestAttributes(request);
        return Pages.USER_PAGE_REDIRECT_UPDATE_HISTORY_REQUEST_SUCCESS;
    }

    @Override
    public void clearRequestAttributes(HttpServletRequest request) {
        request.getSession().removeAttribute(ApplicationConstants.RequestAttributes.HISTORY_REQUEST_RATING_ATTRIBUTE);
        request.getSession().removeAttribute(ApplicationConstants.RequestAttributes.HISTORY_REQUEST_REVIEW_ATTRIBUTE);
        request.getSession().removeAttribute(ApplicationConstants.RequestAttributes.HISTORY_REQUEST_ID_ATTRIBUTE);
    }

}
