package store;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestProduct {
    @Test
    public void testGetters() {
        Product product = new Product("HC","Classic Hoodie","Hoodie.jpg",40);
        assertEquals("HC", product.getId());
        assertEquals("Classic Hoodie", product.getName());
        assertEquals("Hoodie.jpg", product.getImagePath());
        assertEquals(40, product.getPrice(), 0.000001);
    }

    @Test
    public void testSetters() {
        Product product = new Product("HC","Classic Hoodie","Hoodie.jpg",40);
        product.setId("0");
        product.setName("a");
        product.setPrice(1);
        product.setImagePath("img.jpg");
        assertEquals("0", product.getId());
        assertEquals("a", product.getName());
        assertEquals("img.jpg", product.getImagePath());
        assertEquals(1, product.getPrice(), 0.000001);
    }

    @Test
    public void testEquals() {
        Product product1 = new Product("HC","Classic Hoodie","Hoodie.jpg",40);
        Product product2 = new Product("HC","Hoodie","Hoodie.jpg",40);
        Product product3 = new Product("HC","Classic Hoodie","Hoodie.jpg",40);
        assertEquals(product1, product2);
        assertEquals(product1, product1);
        assertEquals(product1, product3);
    }
}
