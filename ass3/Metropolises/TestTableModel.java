package Metropolises;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestTableModel {
    private MetropolisAbstractTableModel model;
    private Metropolis mumbai;
    private Metropolis cairo;
    private MetropolisDAO metropolisDAO;
    private static final int NUM_COLUMNS = 3;

    @BeforeAll
    public void setup() throws SQLException, ClassNotFoundException, IOException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/metro_db", "root", "root");
        LoadTestDatabase.resetMetropolisesTable(connection);
        metropolisDAO = MetropolisDAO.getInstance(connection);
        model = new MetropolisAbstractTableModel(metropolisDAO);
        mumbai = new Metropolis("Mumbai","Asia",20400000);
        cairo = new Metropolis("Cairo", "Africa", 9000000);

    }

    @Test
    public void testBasicMethods() {
        assertEquals(NUM_COLUMNS, model.getColumnCount());
        try {
            assertEquals(metropolisDAO.getAll().size(), model.getRowCount());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void testGetValueAt() {
        assertEquals(mumbai.getName(), model.getValueAt(0,0));
        assertEquals(mumbai.getContinent(), model.getValueAt(0,1));
        assertEquals(mumbai.getPopulation(), model.getValueAt(0,2));
        assertThrows(RuntimeException.class, () -> {model.getValueAt(0, 4);});
    }

    @Test
    public void testGetColumnName() {
        assertEquals("Metropolis", model.getColumnName(0));
        assertEquals("Continent", model.getColumnName(1));
        assertEquals("Population", model.getColumnName(2));
        assertThrows(RuntimeException.class, () -> {model.getColumnName(3);});
    }

    @Test
    public void testSearch() throws SQLException {
        model.search(true,
                true,
                mumbai.getName(),
                mumbai.getContinent(),
                0);
        assertEquals(1, model.getRowCount());
        assertEquals(mumbai.getName(), model.getValueAt(0, 0));
        assertEquals(mumbai.getContinent(), model.getValueAt(0, 1));
    }

    @Test
    public void testAdd() {
        try {
            model.add(cairo);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        assertEquals(cairo.getName(), model.getValueAt(8,0));
        assertEquals(cairo.getContinent(), model.getValueAt(8,1));
        assertEquals(cairo.getPopulation(), model.getValueAt(8,2));
    }
}
