package com.bootcamp;

/** Entrada vendida de un cine: butaca y precio en céntimos. */
public record Ticket(long id, String seat, int priceCents) {
}
