package store;

import java.sql.*;
import java.util.ArrayList;

public class ProductDAO {
    public static final String NAME = "ProductDAO";

    Connection connection;
    public ProductDAO() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "root");
    }

    public ArrayList<Product> getAllProducts() throws SQLException {
        String sql = "select * from products";
        executeQuery(sql);
        return executeQuery(sql);
    }

    public Product getProduct(String id) throws SQLException {
        String sql = "select * from products where productid = " + "'" + id + "'";
        ArrayList<Product> products = executeQuery(sql);
        if (products.size() == 0) return null;
        return products.get(0);
    }

    private ArrayList<Product> executeQuery(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        ArrayList<Product> products = new ArrayList<>();
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            products.add(new Product(rs.getString(1), rs.getString(2), rs.getString(3), rs.getDouble(4)));
        }
        return products;
    }
}
