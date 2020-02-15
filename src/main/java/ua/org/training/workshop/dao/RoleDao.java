package ua.org.training.workshop.dao;

import ua.org.training.workshop.domain.Role;

import java.util.List;
import java.util.Optional;

/**
 * @author kissik
 */
public interface RoleDao extends GenericDao<Role> {

    Optional<Role> findByCode(String code);

    Optional<List<Role>> findRolesByAccountId(Long accountId);

    Optional<List<Role>> findAll();
}
