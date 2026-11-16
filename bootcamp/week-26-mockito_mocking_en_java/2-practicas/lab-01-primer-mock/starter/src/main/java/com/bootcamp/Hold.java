package com.bootcamp;

/** Una reserva: el socio queda en la posición {@code position} de la cola del libro. */
public record Hold(String memberId, String isbn, int position) {
}
