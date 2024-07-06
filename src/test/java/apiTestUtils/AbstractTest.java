package apiTestUtils;

import org.junit.Before;

public abstract class AbstractTest {
    protected String name;
    protected String email;

    @Before
    public void setUp() {
        name = TestUtils.generateUserName();
        email = TestUtils.generateEmail();
    }
}
