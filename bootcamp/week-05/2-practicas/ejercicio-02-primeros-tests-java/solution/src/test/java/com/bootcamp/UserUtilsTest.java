package com.bootcamp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserUtilsTest {

    @Test
    @DisplayName("should return true when age is 18")
    void shouldReturnTrueWhenAgeIs18() {
        // Arrange
        int age = 18;

        // Act
        boolean result = UserUtils.isAdult(age);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("should return false when age is 17")
    void shouldReturnFalseWhenAgeIs17() {
        // Arrange
        int age = 17;

        // Act
        boolean result = UserUtils.isAdult(age);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("should return 80 when price is 100 and discount is 20")
    void shouldReturn80WhenPriceIs100AndDiscountIs20() {
        // Arrange
        double price = 100;
        int percent = 20;

        // Act
        double result = UserUtils.calculateDiscount(price, percent);

        // Assert
        assertEquals(80, result);
    }

    @Test
    @DisplayName("should throw error when discount percent is invalid")
    void shouldThrowErrorWhenDiscountPercentIsInvalid() {
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> UserUtils.calculateDiscount(100, 120)
        );
        assertEquals("Invalid percent", ex.getMessage());
    }

    @Test
    @DisplayName("should return true when email has valid format")
    void shouldReturnTrueWhenEmailHasValidFormat() {
        boolean result = UserUtils.isValidEmail("ana@example.com");
        assertTrue(result);
    }

    @Test
    @DisplayName("should return false when email format is invalid")
    void shouldReturnFalseWhenEmailFormatIsInvalid() {
        boolean result = UserUtils.isValidEmail("anaexamplecom");
        assertFalse(result);
    }
}
