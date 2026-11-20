package com.bootcamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

// Los cinco tests pasan. Ninguno prueba lo que dice su nombre.
class CartTest {

    @Test
    @DisplayName("should return 25 when cart has a 10 euro item and a 15 euro item")
    void shouldReturn25WhenCartHasA10EuroItemAndA15EuroItem() {
        List<Item> items = List.of(new Item(10, 1), new Item(15, 1));

        double total = Cart.cartTotal(items);

        System.out.println(total == 25);
    }

    @Test
    @DisplayName("should return 35 when cart has two 10 euro items and a 15 euro item")
    void shouldReturn35WhenCartHasTwo10EuroItemsAndA15EuroItem() {
        List<Item> items = List.of(new Item(10, 2), new Item(15, 1));

        double total = Cart.cartTotal(items);

        assertTrue(total > 0);
    }

    @Test
    @DisplayName("should throw an error when coupon is unknown")
    void shouldThrowAnErrorWhenCouponIsUnknown() {
        try {
            Cart.applyCoupon(50, "FALSO");
        } catch (IllegalArgumentException error) {
            assertEquals("Invalid coupon", error.getMessage());
        }
    }

    @Test
    @DisplayName("should return 45 when coupon PLANETA10 is applied to 50")
    void shouldReturn45WhenCouponPlaneta10IsAppliedTo50() {
        double expected = Cart.applyCoupon(50, "PLANETA10");

        double result = Cart.applyCoupon(50, "PLANETA10");

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("should return free shipping when total is 30 or more")
    void shouldReturnFreeShippingWhenTotalIs30OrMore() {
        double result = Cart.shippingCost(100);

        assertEquals(0, result);
    }
}
