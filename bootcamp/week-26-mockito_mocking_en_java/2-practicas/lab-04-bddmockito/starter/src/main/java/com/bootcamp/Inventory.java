package com.bootcamp;

/** Ejemplares disponibles de cada libro. */
public interface Inventory {

    /** Aparta un ejemplar. Devuelve {@code false} si no queda ninguno. */
    boolean reserveCopy(String isbn);
}
