package Metropolises;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/metro_db", "root", "root");
    }

    public Connection getConnection() {
        return connection;
    }

    public static DatabaseConnection getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public void resetMetropolisesTable() throws IOException, SQLException {
        StringBuilder sql = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader("Metropolises/metropolises.sql"));
        String s;

        while ((s = reader.readLine()) != null) {
            sql.append(s);
        }

        Statement statement = connection.createStatement();

        String[] inst = sql.toString().split(";");

        for (String value : inst) {
            if (!value.trim().equals("")) {
                statement.executeUpdate(value);
            }
        }
    }
}