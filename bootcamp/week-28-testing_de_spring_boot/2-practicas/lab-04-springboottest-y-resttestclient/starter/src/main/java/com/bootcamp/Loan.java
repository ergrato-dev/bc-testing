package com.bootcamp;

import java.time.LocalDate;

public record Loan(String id, String isbn, String memberId, LocalDate dueDate) {
}
