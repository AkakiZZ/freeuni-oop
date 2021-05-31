package Metropolises;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MetropolisDAO {
    private static final String ADD_SQL = "insert into metropolises (metropolis, continent, population) values (?, ?, ?)";
    private static final String GET_ALL_SQL = "select * from metropolises";

    private static MetropolisDAO dao = null;

    private final Connection connection;

    private MetropolisDAO(Connection connection) {
        this.connection = connection;
    }

    public static MetropolisDAO getInstance(Connection connection) {
        if (dao == null) {
            dao = new MetropolisDAO(connection);
        }
        return dao;
    }

    public void add(Metropolis m) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(ADD_SQL);
        statement.setString(1, m.getName());
        statement.setString(2, m.getContinent());
        statement.setLong(3, m.getPopulation());
        statement.executeUpdate();
    }

    public List<Metropolis> search(boolean populationGreater, boolean exactMatch, String metropolis, String continent, long population) throws SQLException {
        String sql = getSearchSQL(populationGreater, exactMatch, metropolis, continent, population);
        return executeSelectQuery(sql);
    }

    public List<Metropolis> getAll() throws SQLException {
        return executeSelectQuery(GET_ALL_SQL);
    }

    private String getSearchSQL(boolean populationGreater, boolean exactMatch, String metropolis, String continent, long population) {
        StringBuilder sql = new StringBuilder("select * from metropolises where ");

        if (populationGreater) {
            sql.append("population > ").append(population);
        } else {
            sql.append("population < ").append(population);
        }

        if (exactMatch) {
            if (!metropolis.isEmpty()) {
                sql.append(" and metropolis = '").append(metropolis).append("'");
            }
            if (!continent.isEmpty()) {
                sql.append(" and continent = '").append(continent).append("'");
            }
        } else {
            if (!metropolis.isEmpty()) {
                sql.append(" and lower(metropolis) like '%").append(metropolis).append("%'");
            }
            if (!continent.isEmpty()) {
                sql.append(" and lower(continent) like '%").append(continent).append("%'");
            }
        }
        return sql.toString();
    }

    private List<Metropolis> executeSelectQuery(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        List<Metropolis> metropolises = new ArrayList<>();
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            metropolises.add(new Metropolis(rs.getString(1), rs.getString(2), rs.getLong(3)));
        }
        return metropolises;
    }
}
