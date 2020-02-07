package ua.org.training.workshop;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ua.org.training.workshop.domain.Role;
import ua.org.training.workshop.service.RoleService;

import java.util.List;

public class RoleServiceIntegrationTest {
    private RoleService roleService;

    @Before
    public void testInit() {
        roleService = new RoleService();
    }

    @Test
    public void testGetRoleByCode() {
        String code = "USER";
        Role role = roleService.findByCode(code);
        Assert.assertEquals(code, role.getCode());
    }

    @Test
    public void testGetRoles() {
        int size = 4;
        List<Role> roles = roleService.findAll();
        Assert.assertEquals(roles.size(), size);
    }
}
