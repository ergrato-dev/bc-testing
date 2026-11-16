package com.bootcamp;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;

/** Reloj de test que avanza solo cuando el test lo pide. Clock.fixed no puede avanzar. */
class MutableClock extends Clock {

    private final ZoneId zone;
    private Instant instant;

    MutableClock(Instant instant, ZoneId zone) {
        this.instant = instant;
        this.zone = zone;
    }

    void set(Instant newInstant) {
        instant = newInstant;
    }

    void advance(Duration duration) {
        instant = instant.plus(duration);
    }

    @Override
    public Instant instant() {
        return instant;
    }

    @Override
    public ZoneId getZone() {
        return zone;
    }

    @Override
    public Clock withZone(ZoneId newZone) {
        return new MutableClock(instant, newZone);
    }
}
