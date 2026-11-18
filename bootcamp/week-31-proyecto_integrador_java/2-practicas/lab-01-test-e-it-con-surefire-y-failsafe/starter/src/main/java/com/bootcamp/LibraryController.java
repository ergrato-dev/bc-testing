package com.bootcamp;

import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class LibraryController {

    private final LoanService loans;

    public LibraryController(LoanService loans) {
        this.loans = loans;
    }

    @GetMapping("/fees")
    public Map<String, Integer> fee(@RequestParam int daysLate) {
        return Map.of("fee", LateFees.lateFee(daysLate));
    }

    @PostMapping("/books/{isbn}/checkout")
    public Book checkout(@PathVariable String isbn) {
        try {
            return loans.checkout(isbn);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
}
