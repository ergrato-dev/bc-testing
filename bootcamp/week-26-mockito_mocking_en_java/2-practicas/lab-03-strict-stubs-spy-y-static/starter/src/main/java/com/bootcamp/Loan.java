package com.bootcamp;

import java.time.LocalDate;

public record Loan(String id, String memberId, LocalDate dueDate, int renewals) {
}
