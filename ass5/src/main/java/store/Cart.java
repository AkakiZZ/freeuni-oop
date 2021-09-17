package store;

import java.util.HashMap;
import java.util.Iterator;

public class Cart {
    public static final String NAME = "cart";
    private final HashMap<Product, Integer> map;
    private double total;

    public Cart() {
        map = new HashMap<>();
        total = 0;
    }

    public void addProduct(Product p, int quantity) {
        if (quantity <= 0) return;
        if (map.containsKey(p)) {
            removeProduct(p);
        }
        map.put(p, quantity);
        total += p.getPrice() * quantity;
    }

    public double getTotal() {
        return total;
    }

    public int getQuantity(Product p) {
        if (!map.containsKey(p)) return 0;
        return map.get(p);
    }

    public void removeProduct(Product p) {
        total -= p.getPrice() * getQuantity(p);
        map.remove(p);
    }

    public Iterator<Product> getProducts() {
        return map.keySet().iterator();
    }
}
