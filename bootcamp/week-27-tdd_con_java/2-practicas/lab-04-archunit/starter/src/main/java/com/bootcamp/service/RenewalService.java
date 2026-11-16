package com.bootcamp.service;

import com.bootcamp.domain.Loan;
import com.bootcamp.domain.LoanRepository;
import java.time.Clock;
import java.time.LocalDate;
import java.util.NoSuchElementException;

/** Renueva un préstamo 14 días más, como mucho dos veces. */
public class RenewalService {

    public static final int MAX_RENEWALS = 2;
    public static final int RENEWAL_DAYS = 14;

    private final LoanRepository loans;
    private final Clock clock;

    public RenewalService(LoanRepository loans, Clock clock) {
        this.loans = loans;
        this.clock = clock;
    }

    public Loan renew(String loanId) {
        Loan loan = loans.findById(loanId)
            .orElseThrow(() -> new NoSuchElementException("unknown loan: " + loanId));
        LocalDate today = LocalDate.now(clock);
        if (loan.isOverdueOn(today)) {
            throw new IllegalStateException("loan is overdue: " + loanId);
        }
        if (loan.renewals() >= MAX_RENEWALS) {
            throw new IllegalStateException("renewal limit reached: " + loanId);
        }
        Loan renewed = loan.renewedUntil(loan.dueDate().plusDays(RENEWAL_DAYS));
        loans.save(renewed);
        return renewed;
    }
}
