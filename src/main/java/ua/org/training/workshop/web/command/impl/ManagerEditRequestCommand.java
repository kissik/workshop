package ua.org.training.workshop.web.command.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.domain.Account;
import ua.org.training.workshop.domain.Request;
import ua.org.training.workshop.domain.Status;
import ua.org.training.workshop.enums.WorkshopError;
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
import java.math.BigDecimal;

public class ManagerEditRequestCommand implements Command {

    private final static Logger LOGGER = Logger.getLogger(ManagerEditRequestCommand.class);

    static {
        new DOMConfigurator().doConfigure(ApplicationConstants.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }

    private AccountService accountService = new AccountService();
    private RequestService requestService = new RequestService();
    private StatusService statusService = new StatusService();

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response) {
        Long id = Utility.tryParseLong(request.getParameter(ApplicationConstants.RequestAttributes.REQUEST_ID_ATTRIBUTE),
                ApplicationConstants.APP_DEFAULT_ID);
        try {
            Request editRequest = requestService.getRequestById(id);
            BigDecimal price = BigDecimal.valueOf(Utility.tryParseInteger(
                    request.getParameter(
                            ApplicationConstants
                                    .RequestAttributes.REQUEST_PRICE_ATTRIBUTE),
                    ApplicationConstants.APP_DEFAULT_PRICE));
            Account user = accountService.getAccountByUsername(
                    SecurityService.getCurrentUserName(request.getSession())
            );
            String cause = Utility.getParameterString(request.getParameter(
                    ApplicationConstants.RequestAttributes.REQUEST_CAUSE_ATTRIBUTE),
                    ApplicationConstants.APP_STRING_DEFAULT_VALUE);
            Status newStatus = statusService.findByCode(
                    Utility.getParameterString(request.getParameter(ApplicationConstants.RequestAttributes.REQUEST_STATUS_ATTRIBUTE),
                            ApplicationConstants.APP_STRING_DEFAULT_VALUE));

            if (!statusService.hasNextStatus(statusService.findByCode(editRequest.getStatus().getCode()), newStatus)) {
                throw new WorkshopException(WorkshopError.REQUEST_UPDATE_ERROR);
            }

            editRequest.setStatus(newStatus);
            editRequest.setUser(user);
            editRequest.setPrice(price);
            editRequest.setCause(cause);
            editRequest.setClosed(newStatus.isClosed());
            requestService.updateRequest(editRequest);
        } catch (WorkshopException e) {
            LOGGER.error("custom error message: " + e.getMessage());
        }
        clearRequestAttributes(request);
        return Pages.MANAGER_PAGE_REDIRECT_UPDATE_REQUEST_SUCCESS;
    }

    @Override
    public void clearRequestAttributes(HttpServletRequest request) {
        request.getSession().removeAttribute(ApplicationConstants.RequestAttributes.REQUEST_CAUSE_ATTRIBUTE);
        request.getSession().removeAttribute(ApplicationConstants.RequestAttributes.REQUEST_ID_ATTRIBUTE);
        request.getSession().removeAttribute(ApplicationConstants.RequestAttributes.REQUEST_PRICE_ATTRIBUTE);
        request.getSession().removeAttribute(ApplicationConstants.RequestAttributes.REQUEST_STATUS_ATTRIBUTE);
    }

}
