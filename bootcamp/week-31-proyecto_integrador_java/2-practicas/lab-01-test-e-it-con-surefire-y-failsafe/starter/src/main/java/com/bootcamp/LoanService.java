package com.bootcamp;

import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

@Service
public class LoanService {

    private final BookRepository books;

    public LoanService(BookRepository books) {
        this.books = books;
    }

    /** Presta un ejemplar: descuenta uno del stock y devuelve el libro actualizado. */
    public Book checkout(String isbn) {
        Book book = books.findByIsbn(isbn)
                .orElseThrow(() -> new NoSuchElementException("unknown book: " + isbn));
        if (book.stock() == 0) {
            throw new IllegalStateException("no stock: " + isbn);
        }
        Book lent = new Book(isbn, book.title(), book.stock() - 1);
        books.save(lent);
        return lent;
    }
}
