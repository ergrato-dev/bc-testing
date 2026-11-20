package com.bootcamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CalculatorTest {

    // Test activo que falla por diseño: add tiene un bug en Calculator.java.
    // Lee el informe y corrígelo en el PASO 1 (src/main/java/com/bootcamp/Calculator.java).
    @Test
    @DisplayName("should return 5 when adding 2 and 3")
    void shouldReturn5WhenAdding2And3() {
        // Arrange
        int a = 2;
        int b = 3;

        // Act
        int result = Calculator.add(a, b);

        // Assert
        assertEquals(5, result);
    }

    // ============================================
    // PASO 2: Test booleano simple
    // ============================================
    // Descomenta las siguientes líneas:
    // @Test
    // @DisplayName("should return true when number is even")
    // void shouldReturnTrueWhenNumberIsEven() {
    //     // Arrange
    //     int number = 10;
    //
    //     // Act
    //     boolean result = Calculator.isEven(number);
    //
    //     // Assert
    //     assertTrue(result);
    // }

    // ============================================
    // PASO 3: Test para número impar
    // ============================================
    // Descomenta las siguientes líneas:
    // @Test
    // @DisplayName("should return false when number is odd")
    // void shouldReturnFalseWhenNumberIsOdd() {
    //     // Arrange
    //     int number = 7;
    //
    //     // Act
    //     boolean result = Calculator.isEven(number);
    //
    //     // Assert
    //     assertFalse(result);
    // }
}
