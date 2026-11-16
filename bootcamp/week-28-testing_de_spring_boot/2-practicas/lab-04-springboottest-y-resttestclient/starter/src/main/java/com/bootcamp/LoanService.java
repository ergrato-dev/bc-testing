package com.bootcamp;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LoanService {

    static final int LOAN_DAYS = 14;

    private final LoanRepository loans;
    private final Clock clock;

    public LoanService(LoanRepository loans, Clock clock) {
        this.loans = loans;
        this.clock = clock;
    }

    public Loan open(LoanRequest request) {
        Loan loan = new Loan("L-" + (loans.count() + 1), request.isbn(), request.memberId(),
            LocalDate.now(clock).plusDays(LOAN_DAYS));
        loans.save(loan);
        return loan;
    }

    public Loan find(String id) {
        return loans.findById(id).orElseThrow(() -> new LoanNotFoundException(id));
    }

    /** Préstamos cuya fecha de vencimiento ya pasó. */
    public List<Loan> overdue() {
        LocalDate today = LocalDate.now(clock);
        return loans.findAll().stream()
            .filter(loan -> loan.dueDate().isBefore(today))
            .sorted(Comparator.comparing(Loan::id))
            .toList();
    }
}
