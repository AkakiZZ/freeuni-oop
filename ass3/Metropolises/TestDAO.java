package Metropolises;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestDAO {
    MetropolisDAO metropolisDAO;
    Metropolis mumbai;
    Metropolis tokyo;
    @BeforeAll
    public void setup() throws SQLException, ClassNotFoundException, IOException {
        DatabaseConnection.getInstance().resetMetropolisesTable();
        metropolisDAO = MetropolisDAO.getInstance(DatabaseConnection.getInstance().getConnection());
        mumbai = new Metropolis("Mumbai", "Asia", 20400000);
        tokyo = new Metropolis("Tokyo", "Asia", 20000000);
    }

    @Test
    public void testAdd() throws SQLException {
        assertEquals(8, metropolisDAO.getAll().size());
        metropolisDAO.add(tokyo);
        assertEquals(9, metropolisDAO.getAll().size());
        assertEquals(tokyo, metropolisDAO.search(true, true, "Tokyo", "Asia", 0).get(0));
    }

    @Test
    public void testSearch() throws SQLException {
        List<Metropolis> res = metropolisDAO.search(true, true, "Mumbai", "", 0);
        assertEquals(1, res.size());
        assertEquals(mumbai, res.get(res.size() - 1));
        assertEquals(6, metropolisDAO.search(true, false, "", "", 3900000).size());
        assertEquals(2, metropolisDAO.search(false, false, "", "", 3900000).size());
        assertEquals(2, metropolisDAO.search(true, false, "a", "North", 3900000).size());
        assertEquals(9, metropolisDAO.getAll().size());
    }


}
