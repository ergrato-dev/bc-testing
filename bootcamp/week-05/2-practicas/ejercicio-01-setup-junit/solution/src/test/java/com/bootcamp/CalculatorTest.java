package com.bootcamp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @Test
    @DisplayName("should return 5 when adding 2 and 3")
    void shouldReturnFiveWhenAddingTwoAndThree() {
        // Arrange
        int a = 2;
        int b = 3;

        // Act
        int result = Calculator.add(a, b);

        // Assert
        assertEquals(5, result);
    }

    @Test
    @DisplayName("should return true when number is even")
    void shouldReturnTrueWhenNumberIsEven() {
        // Arrange
        int value = 10;

        // Act
        boolean result = Calculator.isEven(value);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("should return false when number is odd")
    void shouldReturnFalseWhenNumberIsOdd() {
        // Arrange
        int value = 7;

        // Act
        boolean result = Calculator.isEven(value);

        // Assert
        assertFalse(result);
    }
}
