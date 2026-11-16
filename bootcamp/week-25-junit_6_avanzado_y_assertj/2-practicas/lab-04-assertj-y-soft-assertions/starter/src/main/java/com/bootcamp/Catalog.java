package com.bootcamp;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/** Catálogo de libros en memoria. */
public final class Catalog {

    private final Map<String, Book> books = new LinkedHashMap<>();

    public void add(Book book) {
        if (books.putIfAbsent(book.isbn(), book) != null) {
            throw new IllegalArgumentException("duplicate isbn: " + book.isbn());
        }
    }

    public Optional<Book> findByIsbn(String isbn) {
        return Optional.ofNullable(books.get(isbn));
    }

    /** Libros de un autor, del más antiguo al más reciente. */
    public List<Book> byAuthor(String author) {
        return books.values().stream()
            .filter(book -> book.author().equals(author))
            .sorted(Comparator.comparingInt(Book::year))
            .toList();
    }

    /** Libros cuyo título contiene el texto, sin distinguir mayúsculas. */
    public List<Book> search(String text) {
        String needle = text.toLowerCase(Locale.ROOT);
        return books.values().stream()
            .filter(book -> book.title().toLowerCase(Locale.ROOT).contains(needle))
            .toList();
    }
}
