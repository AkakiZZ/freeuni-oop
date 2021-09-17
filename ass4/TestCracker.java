import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestCracker {

    @Test
    public void testGenerateHash() throws NoSuchAlgorithmException {
        assertEquals("34800e15707fae815d7c90d49de44aca97e2d759", Cracker.generateHash("a!"));
        assertEquals("4181eecbd7a755d19fdf73887c54837cbecf63fd", Cracker.generateHash("molly"));
    }

    @Test
    public void testCrackPassword() throws InterruptedException, NoSuchAlgorithmException {
        Cracker cracker = new Cracker("66b27417d37e024c46526c2f6d358a754fc552f3", 3, 20);
        assertEquals("xyz", cracker.crackPassword());
        Cracker cracker1 = new Cracker("34800e15707fae815d7c90d49de44aca97e2d759", 2, 8);
        assertEquals("a!", cracker1.crackPassword());
    }

    @Test
    public void testMain() throws NoSuchAlgorithmException, InterruptedException {
        Cracker.main(new String[] {"molly"});
        Cracker.main(new String[] {"34800e15707fae815d7c90d49de44aca97e2d759", "2", "8"});
        assertThrows(IllegalArgumentException.class, () -> {
            Cracker.main(new String[] {});
        });
    }
}
