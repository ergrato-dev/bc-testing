package com.bootcamp;

import java.util.NoSuchElementException;

/** Reserva libros prestados: el socio entra en la cola del libro. */
public class HoldService {

    public static final int MAX_HOLDS = 5;

    private final BookRepository books;
    private final MemberDirectory members;

    public HoldService(BookRepository books, MemberDirectory members) {
        this.books = books;
        this.members = members;
    }

    public Hold placeHold(String memberId, String isbn) {
        if (!members.isActive(memberId)) {
            throw new IllegalArgumentException("inactive member: " + memberId);
        }
        Book book = books.findByIsbn(isbn)
            .orElseThrow(() -> new NoSuchElementException("unknown book: " + isbn));
        int holds = books.countHolds(isbn);
        if (holds >= MAX_HOLDS) {
            throw new IllegalStateException("hold queue full: " + isbn);
        }
        Hold hold = new Hold(memberId, book.isbn(), holds + 1);
        books.saveHold(hold);
        return hold;
    }
}
