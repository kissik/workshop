package ua.org.training.workshop;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import ua.org.training.workshop.dao.AccountDao;
import ua.org.training.workshop.dao.DaoFactory;
import ua.org.training.workshop.domain.Account;
import ua.org.training.workshop.domain.Role;
import ua.org.training.workshop.security.SecurityAccount;
import ua.org.training.workshop.service.AccountService;
import ua.org.training.workshop.service.RoleService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceIntegrationTest {

    private Long id = 0L;
    @Mock
    private DaoFactory daoFactory;
    @Mock
    private AccountDao accountDao;
    private AccountService accountService;
    private RoleService roleService;

    private Map<String, String> roles = new HashMap<>();

    private void initRolesMap() {
        roles.put("ADMIN", "Administrator");
        roles.put("MANAGER", "Manager");
        roles.put("WORKMAN", "Workman");
        roles.put("USER", "User");
    }

    private Role createRole(String code, String name) {
        Role role = new Role();
        role.setId(++id);
        role.setCode(code);
        role.setName(name);
        return role;
    }

    @Before
    public void testInit() {
        MockitoAnnotations.initMocks(this);
        accountService = new AccountService();
        roleService = new RoleService();
        initRolesMap();
    }

    @Test
    public void testRegisterAccount() throws SQLException {

        Role role = createRole("ADMIN", "Administrator");
        AccountService accountServiceMock = new AccountService();
        accountServiceMock.setDaoFactory(daoFactory);

        Account account = new Account();

        account.setId(++id);
        account.setFirstName("Iryna");
        account.setFirstNameOrigin("Ірина");
        account.setLastName("Afanasieva");
        account.setLastNameOrigin("Афанасьєва");
        account.setEmail("iryna.v.afanasieva2@gmail.com");
        account.setPhone("+380501457254");
        account.setEnabled(true);
        account.setUsername("kissik3");
        account.setRoles(Arrays.asList(new Role[]{role}));
        account.setPassword("password");
        account.setDateCreated(LocalDate.now());

        when(daoFactory.createAccountDao())
                .thenReturn(accountDao);
        when(accountDao.create(account)).thenReturn(id);

        Assert.assertEquals(id,
                accountServiceMock.registerAccount(account));
        verify(daoFactory).createAccountDao();
        verify(accountDao).create(account);
    }

    @Test
    public void testGetAccountByUsername() {
        String username = "kissik";
        Account account = accountService.getAccountByUsername(username);
        Assert.assertEquals(username, account.getUsername());
    }

    @Test
    public void testGetAccountByEmail() {
        String email = "jack.sparrow@yohoho.com";
        Account account = accountService.getAccountByEmail(email);
        Assert.assertEquals(email, account.getEmail());
    }

    @Test
    public void testGetAccountByPhone() {
        String phone = "+380001122336";
        Account account = accountService.getAccountByPhone(phone);
        Assert.assertEquals(phone, account.getPhone());
    }

    @Test
    public void testGetAccountById() {
        Long id = 1L;
        Account account = accountService.getAccountById(id);
        Assert.assertEquals(id, account.getId());
    }

    @Test
    public void testSetAccountRoles() {
        Long id = 11L;
        Account account = accountService.getAccountById(id);
        List<Role> roles = Collections.singletonList(roleService.findByCode("USER"));
        account.setRoles(roles);
        accountService.saveAccountRoles(account);
        SecurityAccount securityAccount = new SecurityAccount(accountService.getAccountById(id));
        Assert.assertFalse(securityAccount.hasRole("ADMIN"));
        Assert.assertFalse(securityAccount.hasRole("MANAGER"));
        Assert.assertFalse(securityAccount.hasRole("WORKMAN"));
        Assert.assertTrue(securityAccount.hasRole("USER"));
    }
}
