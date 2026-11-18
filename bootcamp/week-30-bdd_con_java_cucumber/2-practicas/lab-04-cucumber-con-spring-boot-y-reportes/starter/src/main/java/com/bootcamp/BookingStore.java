package com.bootcamp;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

/** Reservas en memoria. Un singleton de Spring: las reservas viven lo que vive el contexto. */
@Repository
public class BookingStore {

    private final Map<String, Booking> bookings = new ConcurrentHashMap<>();

    /** Guarda la reserva si la sala está libre ese día; si no, devuelve la que ya había. */
    public Optional<Booking> book(Booking booking) {
        return Optional.ofNullable(bookings.putIfAbsent(booking.room() + "|" + booking.date(), booking));
    }

    public List<Booking> onDate(LocalDate date) {
        return bookings.values().stream()
            .filter(b -> b.date().equals(date))
            .sorted(Comparator.comparing(Booking::room))
            .toList();
    }

    public void clear() {
        bookings.clear();
    }
}
