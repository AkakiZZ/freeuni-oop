package store;

import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TestProductDAO {
    @Test
    public void testGetProduct() throws SQLException, ClassNotFoundException {
        Product product = new Product("HC","Classic Hoodie","Hoodie.jpg",40);
        ProductDAO dao = new ProductDAO();
        Product p = dao.getProduct(product.getId());
        assertEquals(product, p);
    }

    @Test
    public void testGetAllProducts() throws SQLException, ClassNotFoundException {
        Product product = new Product("HC","Classic Hoodie","Hoodie.jpg",40);
        ProductDAO dao = new ProductDAO();
        ArrayList<Product> p = dao.getAllProducts();
        assertEquals(product, p.get(0));
    }
}
