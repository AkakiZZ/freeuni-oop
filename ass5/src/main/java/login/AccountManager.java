package login;

import java.util.HashMap;
import java.util.Map;

public class AccountManager {
    private final Map<String, String> accounts;
    public static final String NAME = "accountManager";

    public AccountManager() {
        accounts = new HashMap<>();
        accounts.put("Patrick", "1234");
        accounts.put("Molly", "FloPup");
    }

    public boolean accountExists(String username, String pass) {
        return usernameOccupied(username) && accounts.get(username).equals(pass);
    }

    public boolean usernameOccupied(String username) {
        return accounts.get(username) != null;
    }

    public void createAccount(String username, String pass) {
        accounts.put(username, pass);
    }
}