package store;

import org.junit.Test;

import java.sql.SQLException;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;

public class TestCart {
    @Test
    public void testAddProduct() {
        Cart cart = new Cart();
        Product product = new Product("HC","Classic Hoodie","Hoodie.jpg",40);
        cart.addProduct(product, 1);
        assertEquals(1, cart.getQuantity(product));
        cart.addProduct(product, 2);
        assertEquals(2, cart.getQuantity(product));
    }

    @Test
    public void testGetTotal() {
        Cart cart = new Cart();
        Product product = new Product("HC","Classic Hoodie","Hoodie.jpg",40);
        cart.addProduct(product, 1);
        assertEquals(40, cart.getTotal(), 0.000001);
        cart.addProduct(product, 2);
        assertEquals(80, cart.getTotal(), 0.000001);
    }

    @Test
    public void testRemove() {
        Cart cart = new Cart();
        Product product = new Product("HC","Classic Hoodie","Hoodie.jpg",40);
        cart.addProduct(product, 1);
        assertEquals(1, cart.getQuantity(product));
        cart.removeProduct(product);
        assertEquals(0, cart.getQuantity(product));
    }

    @Test
    public void test() throws SQLException, ClassNotFoundException {
        Cart cart = new Cart();
        ProductDAO dao = new ProductDAO();
        Product p = dao.getProduct("HC");
        Product product = new Product("HC","Classic Hoodie","Hoodie.jpg",40);
        cart.addProduct(p, 1);
        cart.addProduct(product, cart.getQuantity(product) + 1);
        assertEquals(2, cart.getQuantity(product));
    }

    @Test
    public void testIterator() {
        Cart cart = new Cart();
        Product product = new Product("HC","Classic Hoodie","Hoodie.jpg",40);
        cart.addProduct(product, 1);
        Iterator<Product> it = cart.getProducts();
        while (it.hasNext()) {
            assertEquals(product, it.next());
        }
    }
}
