package com.bootcamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;

/** Un préstamo tal como lo ve el cliente de la API. */
public record Loan(
    String id,
    String isbn,
    @JsonProperty("member") String memberId,
    LocalDate dueDate,
    LocalDate returnedOn,
    BigDecimal lateFee,
    Status status,
    @JsonIgnore String librarianNote) {

    public enum Status { OPEN, RETURNED }
}
