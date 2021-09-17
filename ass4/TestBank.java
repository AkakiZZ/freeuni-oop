import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestBank {
    private Bank bank;
    @BeforeEach
    public void init()  {
        bank = new Bank();
    }

    @Test
    public void testNumberOfTransactions() throws InterruptedException, IOException {
        bank.processFile("100k.txt", 4);
        List<Account> accounts = bank.getAccounts();
        int countTransactions = 0;
        for (Account account : accounts) {
            countTransactions += account.getTransactions();
        }
        assertEquals(100000, countTransactions / 2);
    }

    @Test
    public void testBalance() throws InterruptedException, IOException {
        bank.processFile("5k.txt", 4);
        List<Account> accounts = bank.getAccounts();
        for (Account account : accounts) {
            assertEquals(1000, account.getBalance());
        }
    }

    @Test
    public void testException() {
        assertThrows(FileNotFoundException.class, () -> {
            bank.readFile("null");
        });
    }

    @Test
    public void testMain() throws IOException, InterruptedException {
        Bank.main(new String[] {"small.txt"});
        Bank.main(new String[] {"small.txt", "4"});
        assertThrows(IllegalArgumentException.class, () -> {
            Bank.main(new String[] {});
        });
    }
}
