package com.bootcamp;

/** Tarjeta de fidelidad de una cafetería: un sello por café, y el café número 10 es gratis. */
public final class StampCard {

    public static final int STAMPS_FOR_FREE_COFFEE = 10;

    private int stamps;

    public void stamp(int coffees) {
        if (coffees <= 0) {
            throw new IllegalArgumentException("coffees must be positive");
        }
        stamps += coffees;
    }

    public int stamps() {
        return stamps;
    }

    public boolean hasFreeCoffee() {
        return stamps >= STAMPS_FOR_FREE_COFFEE;
    }

    /** Canjea un café gratis: descuenta 10 sellos. */
    public void redeem() {
        if (!hasFreeCoffee()) {
            throw new IllegalStateException("not enough stamps: " + stamps);
        }
        stamps -= STAMPS_FOR_FREE_COFFEE;
    }
}
