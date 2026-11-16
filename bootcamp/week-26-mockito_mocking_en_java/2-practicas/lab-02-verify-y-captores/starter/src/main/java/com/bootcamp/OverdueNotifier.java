package com.bootcamp;

import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/** Avisa a cada socio con un préstamo vencido y marca el préstamo como avisado. */
public class OverdueNotifier {

    private final LoanRepository loans;
    private final Notifier notifier;
    private final Clock clock;

    public OverdueNotifier(LoanRepository loans, Notifier notifier, Clock clock) {
        this.loans = loans;
        this.notifier = notifier;
        this.clock = clock;
    }

    /** Devuelve cuántos avisos ha enviado. */
    public int notifyOverdue() {
        LocalDate today = LocalDate.now(clock);
        List<Loan> overdue = loans.findOverdue(today);
        for (Loan loan : overdue) {
            long daysLate = ChronoUnit.DAYS.between(loan.dueDate(), today);
            notifier.send(new Notice(
                loan.memberEmail(),
                "Overdue: " + loan.title(),
                "\"" + loan.title() + "\" is " + daysLate + " day(s) late."));
            loans.markNotified(loan.id(), today);
        }
        return overdue.size();
    }
}
