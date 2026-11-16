package com.bootcamp;

/** Resultado de renovar: el préstamo con la nueva fecha y lo que se cobra. */
public record Renewal(Loan loan, int fee) {
}
