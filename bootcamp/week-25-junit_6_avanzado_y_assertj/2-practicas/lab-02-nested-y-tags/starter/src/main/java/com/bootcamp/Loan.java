package com.bootcamp;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/** Préstamo de un libro: se puede renovar dos veces y devolver una sola vez. */
public final class Loan {

    public static final int MAX_RENEWALS = 2;
    public static final int RENEWAL_DAYS = 7;

    public enum Status { ACTIVE, RETURNED }

    private LocalDate dueDate;
    private Status status = Status.ACTIVE;
    private LocalDate returnedOn;
    private int renewals;

    public Loan(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate dueDate() {
        return dueDate;
    }

    public Status status() {
        return status;
    }

    public boolean isOverdue(LocalDate today) {
        return status == Status.ACTIVE && today.isAfter(dueDate);
    }

    public void renew() {
        if (status == Status.RETURNED) {
            throw new IllegalStateException("cannot renew a returned loan");
        }
        if (renewals == MAX_RENEWALS) {
            throw new IllegalStateException("renewal limit reached");
        }
        dueDate = dueDate.plusDays(RENEWAL_DAYS);
        renewals++;
    }

    public void returnOn(LocalDate date) {
        if (status == Status.RETURNED) {
            throw new IllegalStateException("loan already returned");
        }
        status = Status.RETURNED;
        returnedOn = date;
    }

    public long daysLate() {
        if (status == Status.ACTIVE) {
            throw new IllegalStateException("loan not returned yet");
        }
        return Math.max(0, ChronoUnit.DAYS.between(dueDate, returnedOn));
    }
}
