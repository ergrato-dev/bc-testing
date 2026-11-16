package com.bootcamp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

/** Préstamos en memoria. Es un bean: vive lo mismo que el contexto de Spring. */
@Repository
public class LoanRepository {

    private final Map<String, Loan> loans = new ConcurrentHashMap<>();

    public void save(Loan loan) {
        loans.put(loan.id(), loan);
    }

    public Optional<Loan> findById(String id) {
        return Optional.ofNullable(loans.get(id));
    }

    public List<Loan> findAll() {
        return new ArrayList<>(loans.values());
    }

    public int count() {
        return loans.size();
    }
}
