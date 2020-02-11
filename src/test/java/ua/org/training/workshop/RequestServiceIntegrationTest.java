package ua.org.training.workshop;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import ua.org.training.workshop.dao.DaoFactory;
import ua.org.training.workshop.dao.RequestDao;
import ua.org.training.workshop.domain.Account;
import ua.org.training.workshop.domain.Request;
import ua.org.training.workshop.service.RequestService;
import ua.org.training.workshop.service.StatusService;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RequestServiceIntegrationTest {

    private Long id = 0L;
    @Mock
    private DaoFactory daoFactory;
    @Mock
    private RequestDao requestDao;
    private RequestService requestService;
    private StatusService statusService;

    @Before
    public void testInit() {
        MockitoAnnotations.initMocks(this);
        requestService = new RequestService();
        statusService = new StatusService();
    }

    @Test
    public void testRegisterRequest() throws SQLException {
        RequestService requestServiceMock = new RequestService();
        requestServiceMock.setDaoFactory(daoFactory);

        Request request = new Request();

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
        account.setDateCreated(LocalDate.now());

        request.setAuthor(account);
        request.setUser(account);
        request.setStatus(statusService.findByCode("REGISTER"));

        when(daoFactory.createRequestDao())
                .thenReturn(requestDao);
        when(requestDao.create(request)).thenReturn(id);

        Assert.assertEquals(id,
                requestServiceMock.createRequest(request));
        verify(daoFactory).createRequestDao();
        verify(requestDao).create(request);
    }

    @Test
    public void testGetRequestById() {
        Long id = 16L;
        Request request = requestService.getRequestById(id);
        Assert.assertEquals("Kettle", request.getTitle());
    }
}
