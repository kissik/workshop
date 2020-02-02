package ua.org.training.workshop;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AccountServiceIntegrationTest.class,
        RequestServiceIntegrationTest.class,
        RoleServiceIntegrationTest.class,
        StatusServiceIntegrationTest.class
})
public class TestSuiteIntegration {
}
