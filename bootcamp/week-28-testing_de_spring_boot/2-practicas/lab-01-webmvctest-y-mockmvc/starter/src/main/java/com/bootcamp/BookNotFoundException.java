package com.bootcamp;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(String isbn) {
        super("book not found: " + isbn);
    }
}
