package com.bootcamp;

/**
 * Multa por devolver tarde un libro: 2 días de gracia, 500 por día después
 * y un tope de 10 000. Estudiantes y mayores pagan la mitad.
 */
public final class LateFeeCalculator {

    public static final int GRACE_DAYS = 2;
    public static final int DAILY_FEE = 500;
    public static final int MAX_FEE = 10_000;

    public int feeFor(int daysLate, Membership membership) {
        if (daysLate < 0) {
            throw new IllegalArgumentException("daysLate must not be negative: " + daysLate);
        }
        if (daysLate <= GRACE_DAYS) {
            return 0;
        }
        int fee = Math.min((daysLate - GRACE_DAYS) * DAILY_FEE, MAX_FEE);
        return membership == Membership.STANDARD ? fee : fee / 2;
    }
}
