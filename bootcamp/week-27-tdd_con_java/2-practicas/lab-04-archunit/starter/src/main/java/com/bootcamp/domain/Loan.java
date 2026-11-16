package com.bootcamp.domain;

import java.time.LocalDate;

/** Préstamo de un libro: quién lo tiene y hasta cuándo. */
public record Loan(String id, String memberId, LocalDate dueDate, int renewals) {

    public boolean isOverdueOn(LocalDate today) {
        return today.isAfter(dueDate);
    }

    public Loan renewedUntil(LocalDate newDueDate) {
        return new Loan(id, memberId, newDueDate, renewals + 1);
    }
}
