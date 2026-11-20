package com.bootcamp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class VisitorValidatorTest {

    // Un visitante con los tres campos válidos. Cada test cambia un solo campo:
    // una clase inválida por caso, como en la semana 02.
    private static final Visitor VALID_VISITOR = new Visitor("Ana", 30, false);

    @Nested
    @DisplayName("name")
    class Name {

        // TC-PLAN-REG-001
        @Test
        @DisplayName("should reject the visitor when name has 1 character")
        void shouldRejectTheVisitorWhenNameHas1Character() {
            // Arrange
            Visitor visitor = VALID_VISITOR.withName("A");

            // Act
            String result = VisitorValidator.validate(visitor);

            // Assert
            assertEquals("El nombre debe tener de 2 a 50 caracteres", result);
        }

        // TODO: TC-PLAN-REG-002 a TC-PLAN-REG-008 (tabla del README)
    }

    @Nested
    @DisplayName("age")
    class Age {

        // TC-PLAN-REG-009
        @Test
        @DisplayName("should register the visitor when age is 30")
        void shouldRegisterTheVisitorWhenAgeIs30() {
            // Arrange
            Visitor visitor = VALID_VISITOR.withAge(30);

            // Act
            String result = VisitorValidator.validate(visitor);

            // Assert
            assertEquals("Visitante registrado", result);
        }

        // TODO: TC-PLAN-REG-010 a TC-PLAN-REG-016 (tabla del README)
    }

    @Nested
    @DisplayName("schoolGroup")
    class SchoolGroup {

        // TODO: TC-PLAN-REG-017 a TC-PLAN-REG-019 (tabla del README)
    }
}
