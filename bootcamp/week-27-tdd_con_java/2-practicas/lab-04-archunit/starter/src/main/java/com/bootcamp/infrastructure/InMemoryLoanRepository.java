package com.bootcamp.infrastructure;

import com.bootcamp.domain.Loan;
import com.bootcamp.domain.LoanRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/** Implementación en memoria. En producción sería una base de datos. */
public class InMemoryLoanRepository implements LoanRepository {

    private final Map<String, Loan> loans = new HashMap<>();

    @Override
    public Optional<Loan> findById(String id) {
        return Optional.ofNullable(loans.get(id));
    }

    @Override
    public void save(Loan loan) {
        loans.put(loan.id(), loan);
    }
}
