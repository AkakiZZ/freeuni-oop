import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestAccount {
    private Account account;
    @BeforeAll
    public void init() {
        account = new Account(null, 0, 1000);
    }

    @Test
    public void testUpdates() {
        account.moneyIn(100);
        assertEquals(1100, account.getBalance());
        account.moneyOut(100);
        assertEquals(1000, account.getBalance());
    }

    @Test
    public void testGetters() {
        assertNull(account.getBank());
        assertEquals(2, account.getTransactions());
        assertEquals(0, account.getId());
    }
}
