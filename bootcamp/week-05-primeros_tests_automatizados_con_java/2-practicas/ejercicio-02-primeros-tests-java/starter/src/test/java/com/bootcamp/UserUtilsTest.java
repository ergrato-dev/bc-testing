package com.bootcamp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class UserUtilsTest {

    // ============================================
    // PASO 1: Tests de isAdult
    // ============================================
    // Descomenta las siguientes líneas:
    // @Test
    // @DisplayName("should return true when age is 18")
    // void shouldReturnTrueWhenAgeIs18() {
    //     // Arrange
    //     int age = 18;
    //
    //     // Act
    //     boolean result = UserUtils.isAdult(age);
    //
    //     // Assert
    //     assertTrue(result);
    // }

    // @Test
    // @DisplayName("should return false when age is 17")
    // void shouldReturnFalseWhenAgeIs17() {
    //     // Arrange
    //     int age = 17;
    //
    //     // Act
    //     boolean result = UserUtils.isAdult(age);
    //
    //     // Assert
    //     assertFalse(result);
    // }

    // ============================================
    // PASO 2: Tests de calculateDiscount
    // ============================================
    // Descomenta las siguientes líneas:
    // @Test
    // @DisplayName("should return 80 when price is 100 and discount is 20")
    // void shouldReturn80WhenPriceIs100AndDiscountIs20() {
    //     // Arrange
    //     double price = 100;
    //     int percent = 20;
    //
    //     // Act
    //     double result = UserUtils.calculateDiscount(price, percent);
    //
    //     // Assert
    //     assertEquals(80, result);
    // }

    // @Test
    // @DisplayName("should throw error when discount percent is invalid")
    // void shouldThrowErrorWhenDiscountPercentIsInvalid() {
    //     // Arrange
    //     double price = 100;
    //     int percent = 120;
    //
    //     // Act + Assert
    //     IllegalArgumentException ex = assertThrows(
    //         IllegalArgumentException.class,
    //         () -> UserUtils.calculateDiscount(price, percent)
    //     );
    //     assertEquals("Invalid percent", ex.getMessage());
    // }

    // ============================================
    // PASO 3: Tests de isValidEmail
    // ============================================
    // Descomenta las siguientes líneas:
    // @Test
    // @DisplayName("should return true when email has valid format")
    // void shouldReturnTrueWhenEmailHasValidFormat() {
    //     // Arrange
    //     String email = "ana@example.com";
    //
    //     // Act
    //     boolean result = UserUtils.isValidEmail(email);
    //
    //     // Assert
    //     assertTrue(result);
    // }

    // @Test
    // @DisplayName("should return false when email format is invalid")
    // void shouldReturnFalseWhenEmailFormatIsInvalid() {
    //     // Arrange
    //     String email = "anaexamplecom";
    //
    //     // Act
    //     boolean result = UserUtils.isValidEmail(email);
    //
    //     // Assert
    //     assertFalse(result);
    // }

    // ============================================
    // PASO 4: Comparar decimales (double)
    // ============================================
    // Ejecuta primero sin delta y lee el fallo; luego usa assertEquals(expected, actual, delta).
    // Descomenta las siguientes líneas:
    // @Test
    // @DisplayName("should return 15.992 when price is 19.99 and discount is 20")
    // void shouldReturnDiscountedPriceWithinDeltaWhenPriceHasDecimals() {
    //     // Arrange
    //     double price = 19.99;
    //     int percent = 20;
    //
    //     // Act
    //     double result = UserUtils.calculateDiscount(price, percent);
    //
    //     // Assert
    //     assertEquals(15.992, result);
    // }

    // ============================================
    // PASO 5: Failure vs Error: email null
    // ============================================
    // Ejecuta y observa que Surefire lo reporta como Error, no como Failure;
    // después corrige isValidEmail en src/main para que devuelva false.
    // Descomenta las siguientes líneas:
    // @Test
    // @DisplayName("should return false when email is null")
    // void shouldReturnFalseWhenEmailIsNull() {
    //     // Arrange
    //     String email = null;
    //
    //     // Act
    //     boolean result = UserUtils.isValidEmail(email);
    //
    //     // Assert
    //     assertFalse(result);
    // }

    // ============================================
    // PASO 6: Assertions fluentes con AssertJ
    // ============================================
    // Descomenta las siguientes líneas:
    // @Test
    // @DisplayName("should return 80 when price is 100 and discount is 20 (AssertJ)")
    // void shouldReturn80WhenPriceIs100AndDiscountIs20WithAssertJ() {
    //     // Arrange
    //     double price = 100;
    //     int percent = 20;
    //
    //     // Act
    //     double result = UserUtils.calculateDiscount(price, percent);
    //
    //     // Assert
    //     assertThat(result).isEqualTo(80.0);
    // }

    // @Test
    // @DisplayName("should throw error when discount percent is negative (AssertJ)")
    // void shouldThrowErrorWhenDiscountPercentIsNegativeWithAssertJ() {
    //     // Arrange
    //     double price = 100;
    //     int percent = -5;
    //
    //     // Act + Assert
    //     assertThatThrownBy(() -> UserUtils.calculateDiscount(price, percent))
    //         .isInstanceOf(IllegalArgumentException.class)
    //         .hasMessage("Invalid percent");
    // }
}
