package ua.org.training.workshop.service;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import ua.org.training.workshop.dao.DaoFactory;
import ua.org.training.workshop.domain.Account;
import ua.org.training.workshop.domain.Request;
import ua.org.training.workshop.exception.WorkshopErrors;
import ua.org.training.workshop.exception.WorkshopException;
import ua.org.training.workshop.utilities.Pageable;
import ua.org.training.workshop.utilities.UtilitiesClass;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kissik
 */
public class RequestService {

    private DaoFactory requestRepository = DaoFactory.getInstance();
    static {
        new DOMConfigurator().doConfigure(UtilitiesClass.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }
    private static Logger logger = Logger.getLogger(RequestService.class);

    public void createRequest(Request request) throws WorkshopException {

        try {
            requestRepository
                    .createRequestDao()
                    .create(request);
        }
        catch(SQLException e){
            logger.error("SQL exception after create account : " + e.getMessage());
        }
        catch (Exception e){
            logger.error("error: " + e.getMessage());
            throw new WorkshopException(WorkshopErrors.REQUEST_CREATE_NEW_ERROR);
        }
        logger.info("Request " + request.getTitle() + " was successfully created");
    }

    public Request getRequestById(Long id) throws WorkshopException {
        return requestRepository
                .createRequestDao()
                .findById(id)
                .orElseThrow(() -> new WorkshopException(WorkshopErrors.REQUEST_NOT_FOUND_ERROR));
    }

    public String getPage(Pageable page) {

        String jsonString = "";
        ObjectMapper jsonMapper = new ObjectMapper();

        Map<String, Object> mapJson = new HashMap<>();
        mapJson.put("content", requestRepository
                .createRequestDao()
                .getPage(page, mapJson)
                .orElse(Collections.emptyList()));
        mapJson.put("size", page.getSize());

        try{
            jsonString = jsonMapper.writeValueAsString(mapJson);

        }catch (JsonGenerationException e) {
            logger.error("JSON generation exception : " + e.getMessage());
        } catch (JsonMappingException e) {
            logger.error("JSON mapping exception : " + e.getMessage());
        } catch (IOException e) {
            logger.error("IO exception : " + e.getMessage());
        }

        return jsonString;
    }

    public String getPageByAuthor(Pageable page, Account author) {

        String jsonString = "";
        ObjectMapper jsonMapper = new ObjectMapper();

        Map<String, Object> mapJson = new HashMap<>();
        mapJson.put("content", requestRepository
                .createRequestDao()
                .getPageByAuthor(page, author, mapJson)
                .orElse(Collections.emptyList()));
        mapJson.put("size", page.getSize());

        try{
            jsonString = jsonMapper.writeValueAsString(mapJson);

        }catch (JsonGenerationException e) {
            logger.error("JSON generation exception : " + e.getMessage());
        } catch (JsonMappingException e) {
            logger.error("JSON mapping exception : " + e.getMessage());
        } catch (IOException e) {
            logger.error("IO exception : " + e.getMessage());
        }

        return jsonString;
    }

}