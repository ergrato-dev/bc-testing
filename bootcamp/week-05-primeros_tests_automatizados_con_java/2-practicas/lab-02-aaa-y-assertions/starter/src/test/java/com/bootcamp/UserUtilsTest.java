package com.bootcamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserUtilsTest {

    // ============================================
    // PASO 1: tests de isAdult (descomenta)
    // ============================================
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
    //
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
    // PASO 2: tests de calculateDiscount (descomenta)
    // ============================================
    // @Test
    // @DisplayName("should return 80 when price is 100 and percent is 20")
    // void shouldReturn80WhenPriceIs100AndPercentIs20() {
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
    //
    // @Test
    // @DisplayName("should throw an error when percent is greater than 100")
    // void shouldThrowAnErrorWhenPercentIsGreaterThan100() {
    //     // Arrange
    //     double price = 100;
    //     int percent = 120;
    //
    //     // Act + Assert
    //     IllegalArgumentException error = assertThrows(
    //             IllegalArgumentException.class,
    //             () -> UserUtils.calculateDiscount(price, percent));
    //     assertEquals("Invalid percent", error.getMessage());
    // }

    // ============================================
    // PASO 3: tests de isValidEmail (descomenta)
    // ============================================
    // @Test
    // @DisplayName("should return true when email has an at sign and a dot")
    // void shouldReturnTrueWhenEmailHasAnAtSignAndADot() {
    //     // Arrange
    //     String email = "ana@example.com";
    //
    //     // Act
    //     boolean result = UserUtils.isValidEmail(email);
    //
    //     // Assert
    //     assertTrue(result);
    // }
    //
    // @Test
    // @DisplayName("should return false when email has no at sign")
    // void shouldReturnFalseWhenEmailHasNoAtSign() {
    //     // Arrange
    //     String email = "anaexample.com";
    //
    //     // Act
    //     boolean result = UserUtils.isValidEmail(email);
    //
    //     // Assert
    //     assertFalse(result);
    // }

    // ============================================
    // PASO 4: createUser con assertEquals (escríbelo tú)
    // ============================================
    // TODO: un test que llame a createUser("  Ana Pérez ", "ANA@Example.com")
    // y compruebe con assertEquals el User completo que devuelve.

    // ============================================
    // PASO 5: findUserByEmail con assertEquals y assertNull (escríbelos tú)
    // ============================================
    // TODO: un test que busque un email que está en la lista (assertEquals).
    // TODO: un test que busque un email que no está (assertNull).

    // ============================================
    // PASO 6: calculateDiscount con decimales (escríbelo tú)
    // ============================================
    // TODO: un test con precio 29.90 y 10 % que use assertEquals con delta.
}
