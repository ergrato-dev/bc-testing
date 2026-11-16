package com.bootcamp;

import java.time.LocalDate;

public record LoanRecord(String isbn, LocalDate dueDate) {
}
