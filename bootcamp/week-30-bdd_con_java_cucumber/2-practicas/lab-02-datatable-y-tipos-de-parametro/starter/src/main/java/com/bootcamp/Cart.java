package com.bootcamp;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/** Carrito de una tienda: desde 100.000 pesos de subtotal, un 10 % de descuento. */
public final class Cart {

    public static final int DISCOUNT_THRESHOLD = 100_000;
    public static final int DISCOUNT_PERCENT = 10;

    private final Map<String, Product> catalog = new LinkedHashMap<>();
    private final Map<String, Integer> quantities = new LinkedHashMap<>();

    public Cart(List<Product> catalog) {
        catalog.forEach(product -> this.catalog.put(product.name(), product));
    }

    public void add(String product, int quantity) {
        if (!catalog.containsKey(product)) {
            throw new NoSuchElementException("unknown product: " + product);
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
        quantities.merge(product, quantity, Integer::sum);
    }

    /** Las líneas en el orden en que se añadió cada producto por primera vez. */
    public List<CartLine> lines() {
        return quantities.entrySet().stream()
            .map(e -> new CartLine(e.getKey(), e.getValue(), catalog.get(e.getKey()).price() * e.getValue()))
            .toList();
    }

    public int subtotal() {
        return lines().stream().mapToInt(CartLine::amount).sum();
    }

    public int discount() {
        int subtotal = subtotal();
        return subtotal >= DISCOUNT_THRESHOLD ? subtotal * DISCOUNT_PERCENT / 100 : 0;
    }

    public int total() {
        return subtotal() - discount();
    }
}
