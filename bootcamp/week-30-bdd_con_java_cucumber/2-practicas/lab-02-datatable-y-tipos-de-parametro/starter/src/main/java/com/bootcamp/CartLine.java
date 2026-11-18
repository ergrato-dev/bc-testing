package com.bootcamp;

/** Una línea del carrito: producto, cantidad e importe (precio por cantidad). */
public record CartLine(String product, int quantity, int amount) {
}
