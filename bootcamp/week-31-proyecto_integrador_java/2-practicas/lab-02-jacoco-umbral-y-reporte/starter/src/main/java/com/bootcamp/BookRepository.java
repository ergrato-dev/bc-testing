package com.bootcamp;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepository {

    private final JdbcClient jdbc;

    public BookRepository(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    public void save(Book book) {
        jdbc.sql("""
                INSERT INTO books (isbn, title, stock) VALUES (:isbn, :title, :stock)
                ON CONFLICT (isbn) DO UPDATE SET title = :title, stock = :stock""")
                .paramSource(book)
                .update();
    }

    public Optional<Book> findByIsbn(String isbn) {
        return jdbc.sql("SELECT isbn, title, stock FROM books WHERE isbn = :isbn")
                .param("isbn", isbn)
                .query(Book.class)
                .optional();
    }

    public List<Book> available() {
        return jdbc.sql("SELECT isbn, title, stock FROM books WHERE stock > 0 ORDER BY title")
                .query(Book.class)
                .list();
    }
}
