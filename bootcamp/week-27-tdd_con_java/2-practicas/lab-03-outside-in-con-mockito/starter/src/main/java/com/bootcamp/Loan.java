package com.bootcamp;

import java.time.LocalDate;

/** Préstamo de un libro a un socio de la biblioteca. */
public record Loan(String id, String memberId, String bookTitle, LocalDate dueDate) {
}
