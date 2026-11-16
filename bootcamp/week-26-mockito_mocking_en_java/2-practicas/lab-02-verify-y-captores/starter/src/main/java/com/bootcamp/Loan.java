package com.bootcamp;

import java.time.LocalDate;

public record Loan(String id, String memberEmail, String title, LocalDate dueDate) {
}
