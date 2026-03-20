package com.bootcamp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AmountValidatorTest {

    private final AmountValidator validator = new AmountValidator();

    // ============================================
    // PASO 1: Caso valido
    // ============================================
    // @Test
    // @DisplayName("should return true when amount is positive")
    // void shouldReturnTrueWhenAmountIsPositive() {
    //     boolean result = validator.isValid(10);
    //     assertTrue(result);
    // }

    // ============================================
    // PASO 2: Caso invalido
    // ============================================
    // @Test
    // @DisplayName("should return false when amount is negative")
    // void shouldReturnFalseWhenAmountIsNegative() {
    //     boolean result = validator.isValid(-1);
    //     assertFalse(result);
    // }

    // ============================================
    // PASO 3: Valor limite
    // ============================================
    // @Test
    // @DisplayName("should return true when amount is zero")
    // void shouldReturnTrueWhenAmountIsZero() {
    //     boolean result = validator.isValid(0);
    //     assertTrue(result);
    // }
}
