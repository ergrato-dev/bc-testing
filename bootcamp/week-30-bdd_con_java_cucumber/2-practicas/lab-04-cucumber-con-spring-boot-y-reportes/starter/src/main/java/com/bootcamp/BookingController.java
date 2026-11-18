package com.bootcamp;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Reservas de salas: una sala no se reserva dos veces el mismo día. */
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingStore store;

    public BookingController(BookingStore store) {
        this.store = store;
    }

    @PostMapping
    public ResponseEntity<?> book(@RequestBody Booking booking) {
        return store.book(booking)
            .<ResponseEntity<?>>map(previous -> ResponseEntity.status(HttpStatus.CONFLICT).body(
                ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
                    booking.room() + " already booked on " + booking.date() + " by " + previous.bookedBy())))
            .orElseGet(() -> ResponseEntity.status(HttpStatus.CREATED).body(booking));
    }

    @GetMapping
    public List<Booking> onDate(@RequestParam LocalDate date) {
        return store.onDate(date);
    }
}
