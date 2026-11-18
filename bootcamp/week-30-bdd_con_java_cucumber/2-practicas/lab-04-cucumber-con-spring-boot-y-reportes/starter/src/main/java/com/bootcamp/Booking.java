package com.bootcamp;

import java.time.LocalDate;

/** La reserva de una sala para un día. */
public record Booking(String room, LocalDate date, String bookedBy) {
}
