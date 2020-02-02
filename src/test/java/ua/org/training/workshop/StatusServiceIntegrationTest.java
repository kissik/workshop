package ua.org.training.workshop;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ua.org.training.workshop.domain.Status;
import ua.org.training.workshop.service.StatusService;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class StatusServiceIntegrationTest {
    private StatusService statusService;

    @Before
    public void testInit(){
        statusService = new StatusService();
    }

    @Test
    public void testGetRoleByCode(){
        String code = "DONE";
        Status status = statusService.findByCode(code);
        assertEquals(code, status.getCode());
    }
}
