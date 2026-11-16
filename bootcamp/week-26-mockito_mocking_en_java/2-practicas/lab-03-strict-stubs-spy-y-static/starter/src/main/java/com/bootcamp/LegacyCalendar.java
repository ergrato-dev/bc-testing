package com.bootcamp;

import java.time.LocalDate;

/** Código heredado: devuelve la fecha del sistema desde un método estático. */
public final class LegacyCalendar {

    private LegacyCalendar() {
    }

    public static LocalDate today() {
        return LocalDate.now();
    }
}
