package ua.org.training.workshop.service;

import ua.org.training.workshop.dao.DaoFactory;
import ua.org.training.workshop.domain.Role;
import ua.org.training.workshop.enums.WorkshopError;
import ua.org.training.workshop.exception.WorkshopException;

import java.util.List;

public class RoleService {
    private DaoFactory roleRepository = DaoFactory.getInstance();

    public Role findByCode(String roleStr) throws WorkshopException {
        return roleRepository
                .createRoleDao()
                .findByCode(roleStr)
                .orElseThrow(() -> new WorkshopException(WorkshopError.ROLE_NOT_FOUND_ERROR));
    }

    public List<Role> findAll() throws WorkshopException {
        return roleRepository
                .createRoleDao()
                .findAll().orElseThrow(() -> new WorkshopException(WorkshopError.ROLE_LIST_IS_EMPTY_ERROR));
    }
}
