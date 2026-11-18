package com.bootcamp.bdd;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;

/** Un reloj que los escenarios ponen en una fecha y adelantan días. */
public class TestClock extends Clock {

    private Instant now = Instant.EPOCH;

    public void setToday(LocalDate today) {
        now = today.atStartOfDay(ZoneOffset.UTC).toInstant();
    }

    public void advanceDays(int days) {
        now = now.plus(Duration.ofDays(days));
    }

    @Override
    public ZoneId getZone() {
        return ZoneOffset.UTC;
    }

    @Override
    public Clock withZone(ZoneId zone) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Instant instant() {
        return now;
    }
}
