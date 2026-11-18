package com.bootcamp;

/** Multa por devolver un libro tarde: 50 por día, hasta un máximo de 1000. */
public final class LateFees {

    static final int PER_DAY = 50;
    static final int MAX_FEE = 1000;

    private LateFees() {
    }

    public static int lateFee(int daysLate) {
        if (daysLate < 0) {
            throw new IllegalArgumentException("days late must not be negative");
        }
        return Math.min(daysLate * PER_DAY, MAX_FEE);
    }
}
