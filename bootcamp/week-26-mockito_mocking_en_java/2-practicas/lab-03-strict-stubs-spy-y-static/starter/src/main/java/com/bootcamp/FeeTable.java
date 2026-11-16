package com.bootcamp;

/** Tarifas de renovación. Clase real, sin dependencias. */
public class FeeTable {

    public static final int SECOND_RENEWAL_FEE = 300;

    /** La primera renovación es gratis; las siguientes cuestan {@link #SECOND_RENEWAL_FEE}. */
    public int renewalFee(int renewalNumber) {
        if (renewalNumber < 1) {
            throw new IllegalArgumentException("renewal number must be at least 1");
        }
        return renewalNumber == 1 ? 0 : SECOND_RENEWAL_FEE;
    }
}
