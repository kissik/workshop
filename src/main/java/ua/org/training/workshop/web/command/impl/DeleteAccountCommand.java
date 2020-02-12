package ua.org.training.workshop.web.command.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.domain.Account;
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

public class DeleteAccountCommand implements Command {

    private final static Logger LOGGER = Logger.getLogger(DeleteAccountCommand.class);

    static {
        new DOMConfigurator().doConfigure(ApplicationConstants.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }

    private AccountService accountService = new AccountService();
    private RequestService requestService = new RequestService();
    private StatusService statusService = new StatusService();

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response) {
        Long id = Utility.tryParseLong(request.getParameter(ApplicationConstants.RequestAttributes.ACCOUNT_ID_ATTRIBUTE),
                        ApplicationConstants.APP_DEFAULT_ID);
        try {
            Account deleteAccount = accountService.getAccountById(id);

            if (!SecurityService.loadAccountSecurity(request).hasRole(ApplicationConstants.APP_SUPERUSER_ROLE)) {
                LOGGER.error("user has no rights to delete accounts!");
                throw new WorkshopException(WorkshopError.REQUEST_UPDATE_ERROR);
            }

            accountService.delete(deleteAccount.getId());
        } catch (WorkshopException e) {
            LOGGER.error("custom error message: " + e.getMessage());
            return Pages.ADMIN_PAGE_REDIRECT_DELETE_ACCOUNT_FAILED;
        }
        clearRequestAttributes(request);
        return Pages.ADMIN_PAGE_REDIRECT_DELETE_ACCOUNT_SUCCESS;
    }

    @Override
    public void clearRequestAttributes(HttpServletRequest request) {
        request.getSession().removeAttribute(ApplicationConstants.RequestAttributes.ACCOUNT_ID_ATTRIBUTE);
    }

}
