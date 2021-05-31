package Metropolises;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class LoadTestDatabase {
    public static void resetMetropolisesTable(Connection connection) throws IOException, SQLException {
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
