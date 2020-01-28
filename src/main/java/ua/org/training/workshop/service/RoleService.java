package ua.org.training.workshop.service;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.dao.DaoFactory;
import ua.org.training.workshop.domain.Role;
import ua.org.training.workshop.exception.WorkshopErrors;
import ua.org.training.workshop.exception.WorkshopException;
import ua.org.training.workshop.utilities.UtilitiesClass;

import java.util.List;

public class RoleService  {
    private DaoFactory roleRepository = DaoFactory.getInstance();
    static {
        new DOMConfigurator().doConfigure(UtilitiesClass.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }
    public static Logger logger = Logger.getLogger(RoleService.class);

    public Role findByCode(String roleStr) throws WorkshopException {
        return roleRepository
                .createRoleDao()
                .findByCode(roleStr)
                .orElseThrow(() -> new WorkshopException(WorkshopErrors.ROLE_NOT_FOUND_ERROR));
    }

    public boolean newRole(Role role) throws WorkshopException {
        try {
            logger.info("Before save");
            roleRepository.createRoleDao().create(role);
            logger.info("After save");
        }
        catch(Exception e){
            throw new WorkshopException(WorkshopErrors.DATABASE_CONNECTION_ERROR);
        }
        return true;
    }

    public List<Role> findAll() throws WorkshopException {
        return roleRepository
                    .createRoleDao()
                    .findAll().orElseThrow(()->new WorkshopException(WorkshopErrors.ROLE_LIST_IS_EMPTY_ERROR));
    }
}
