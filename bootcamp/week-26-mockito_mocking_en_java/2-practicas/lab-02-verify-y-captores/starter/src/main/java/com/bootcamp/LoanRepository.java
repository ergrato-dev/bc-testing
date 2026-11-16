package com.bootcamp;

import java.time.LocalDate;
import java.util.List;

public interface LoanRepository {

    /** Préstamos con fecha de devolución anterior a {@code today} y sin avisar. */
    List<Loan> findOverdue(LocalDate today);

    void markNotified(String loanId, LocalDate date);
}
