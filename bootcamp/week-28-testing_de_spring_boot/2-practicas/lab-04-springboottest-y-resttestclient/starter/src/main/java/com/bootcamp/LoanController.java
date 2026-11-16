package com.bootcamp;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService service;

    public LoanController(LoanService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Loan> open(@RequestBody LoanRequest request) {
        Loan loan = service.open(request);
        return ResponseEntity.created(URI.create("/api/loans/" + loan.id())).body(loan);
    }

    @GetMapping("/{id}")
    public Loan find(@PathVariable String id) {
        return service.find(id);
    }

    @GetMapping("/overdue")
    public List<Loan> overdue() {
        return service.overdue();
    }
}
