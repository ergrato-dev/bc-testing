package com.bootcamp;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @GetMapping("/{isbn}")
    public Book find(@PathVariable String isbn) {
        return service.find(isbn);
    }

    @GetMapping
    public List<Book> byAuthor(@RequestParam String author) {
        return service.byAuthor(author);
    }

    @PostMapping
    public ResponseEntity<Book> add(@RequestBody Book book) {
        Book saved = service.add(book);
        return ResponseEntity.created(URI.create("/api/books/" + saved.isbn())).body(saved);
    }
}
