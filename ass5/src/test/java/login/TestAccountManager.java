package login;

import junit.framework.TestCase;
import login.AccountManager;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestAccountManager {
    AccountManager accountManager;
    @Before
    public void setUp() {
        accountManager = new AccountManager();
    }

    @Test
    public void testAccountExists() {
        assertTrue(accountManager.accountExists("Patrick", "1234"));
        assertFalse(accountManager.accountExists("Patrick", "12333"));
    }

    @Test
    public void testUsernameOccupied() {
        assertTrue(accountManager.usernameOccupied("Patrick"));
        assertFalse(accountManager.usernameOccupied("Patric"));
    }

    @Test
    public void testCreateAccount() {
        accountManager.createAccount("test1", "test1");
        assertTrue(accountManager.accountExists("test1", "test1"));
    }
}
