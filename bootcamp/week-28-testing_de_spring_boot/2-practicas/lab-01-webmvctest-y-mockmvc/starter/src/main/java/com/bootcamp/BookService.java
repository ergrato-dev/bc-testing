package com.bootcamp;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

/** Catálogo en memoria. En los tests del controlador se sustituye por un mock. */
@Service
public class BookService {

    private final Map<String, Book> books = new ConcurrentHashMap<>();

    public Book find(String isbn) {
        Book book = books.get(isbn);
        if (book == null) {
            throw new BookNotFoundException(isbn);
        }
        return book;
    }

    public List<Book> byAuthor(String author) {
        return books.values().stream()
            .filter(book -> book.author().equals(author))
            .sorted(Comparator.comparing(Book::year))
            .toList();
    }

    public Book add(Book book) {
        books.put(book.isbn(), book);
        return book;
    }
}
