package com.bootcamp;

/** Un pedido: su número, su importe en pesos y si ya se pagó. */
public record Order(int id, int amount, Status status) {

    public enum Status { PENDING, PAID }

    Order paid() {
        return new Order(id, amount, Status.PAID);
    }
}
