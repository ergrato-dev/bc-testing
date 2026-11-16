package com.bootcamp;

import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/** Texto del recordatorio de devolución según los días que faltan. */
public final class ReturnReminder {

    private final Clock clock;

    public ReturnReminder(Clock clock) {
        this.clock = clock;
    }

    public String messageFor(LoanRecord loan) {
        long days = ChronoUnit.DAYS.between(LocalDate.now(clock), loan.dueDate());
        if (days < 0) {
            return "overdue by " + -days + " day(s)";
        }
        if (days == 0) {
            return "due today";
        }
        if (days == 1) {
            return "due tomorrow";
        }
        return "due in " + days + " days";
    }
}
