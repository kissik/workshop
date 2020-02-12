package ua.org.training.workshop.web.command.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.domain.Request;
import ua.org.training.workshop.service.RequestService;
import ua.org.training.workshop.service.StatusService;
import ua.org.training.workshop.utility.ApplicationConstants;
import ua.org.training.workshop.utility.Page;
import ua.org.training.workshop.utility.PageService;
import ua.org.training.workshop.utility.Utility;
import ua.org.training.workshop.web.command.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class WorkmanRequestsCommand implements Command {

    static {
        new DOMConfigurator().doConfigure(ApplicationConstants.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }

    private final static Logger LOGGER = Logger.getLogger(WorkmanRequestsCommand.class);
    private RequestService requestService = new RequestService();
    private StatusService statusService = new StatusService();

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response) {
        return createJSONRequestList(request, response);
    }

    private String createJSONRequestList(HttpServletRequest request,
                                         HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding(ApplicationConstants.APP_ENCODING);
        LOGGER.info("Send json page to user client!");
        PageService<Request> pageService = new PageService<>();
        Page<Request> page = pageService.createPage(request);
        String jsonString = requestService
                .getPageByLanguageAndStatus(page,
                        Utility.getLocale(request),
                        statusService.findByCode(ApplicationConstants.REQUEST_WORKMAN_STATUS).getId());
        try {
            PrintWriter writer = response.getWriter();
            writer.print(jsonString);
            LOGGER.info(jsonString);
        } catch (IOException e) {
            LOGGER.error("IO Exception : " + e.getMessage());
        }
        return ApplicationConstants.APP_STRING_DEFAULT_VALUE;
    }
}
