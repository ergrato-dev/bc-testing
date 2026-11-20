package com.bootcamp;

import java.util.List;

// Carrito de la tienda del planetario.
public class Cart {

    public static double cartTotal(List<Item> items) {
        double total = 0;
        for (Item item : items) {
            total += item.price() * item.quantity();
        }
        return total;
    }

    public static double applyCoupon(double total, String code) {
        if (code.equals("PLANETA10")) {
            return total * 0.9;
        }
        throw new IllegalArgumentException("Invalid coupon");
    }

    public static double shippingCost(double total) {
        return total >= 30 ? 0 : 3.9;
    }
}
