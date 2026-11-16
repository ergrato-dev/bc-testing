package com.bootcamp;

import java.time.LocalDate;
import java.util.NoSuchElementException;

/** Renueva un préstamo dos semanas más, como mucho dos veces. */
public class RenewalService {

    public static final int MAX_RENEWALS = 2;
    public static final int RENEWAL_DAYS = 14;

    private final LoanRepository loans;
    private final FeeTable fees;

    public RenewalService(LoanRepository loans, FeeTable fees) {
        this.loans = loans;
        this.fees = fees;
    }

    public Renewal renew(String loanId) {
        Loan loan = loans.findById(loanId)
            .orElseThrow(() -> new NoSuchElementException("unknown loan: " + loanId));
        if (loan.renewals() >= MAX_RENEWALS) {
            throw new IllegalStateException("renewal limit reached: " + loanId);
        }
        LocalDate today = LegacyCalendar.today();
        if (today.isAfter(loan.dueDate())) {
            throw new IllegalStateException("loan is overdue: " + loanId);
        }
        int renewalNumber = loan.renewals() + 1;
        Loan renewed = new Loan(loan.id(), loan.memberId(), loan.dueDate().plusDays(RENEWAL_DAYS), renewalNumber);
        loans.save(renewed);
        return new Renewal(renewed, fees.renewalFee(renewalNumber));
    }
}
