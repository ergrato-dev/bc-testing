package com.bootcamp;

import java.util.Optional;

public interface LoanRepository {

    Optional<Loan> findById(String loanId);

    void save(Loan loan);
}
