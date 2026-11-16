package com.bootcamp;

import java.util.Optional;

/** Acceso a los libros y a sus reservas. En producción, una base de datos. */
public interface BookRepository {

    Optional<Book> findByIsbn(String isbn);

    int countHolds(String isbn);

    void saveHold(Hold hold);
}
