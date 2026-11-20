package com.bootcamp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class VisitorValidatorTest {

    // TC-PLAN-001
    @Test
    @DisplayName("should register the visitor when all fields are valid")
    void shouldRegisterTheVisitorWhenAllFieldsAreValid() {
        assertEquals("Visitante registrado", VisitorValidator.validate(new Visitor("Ana Ruiz", 30, false)));
    }

    // TC-PLAN-002
    @Test
    @DisplayName("should reject the name when it has 1 character")
    void shouldRejectTheNameWhenItHas1Character() {
        assertEquals("El nombre debe tener de 2 a 50 caracteres",
                VisitorValidator.validate(new Visitor("A", 30, false)));
    }

    // TC-PLAN-003
    @Test
    @DisplayName("should register the visitor when the name has 50 characters")
    void shouldRegisterTheVisitorWhenTheNameHas50Characters() {
        assertEquals("Visitante registrado", VisitorValidator.validate(new Visitor("a".repeat(50), 30, false)));
    }

    // TC-PLAN-004
    @Test
    @DisplayName("should reject the name when it contains a digit")
    void shouldRejectTheNameWhenItContainsADigit() {
        assertEquals("El nombre solo admite letras y espacios",
                VisitorValidator.validate(new Visitor("Ana 2", 30, false)));
    }

    // TC-PLAN-005
    @Test
    @DisplayName("should reject the age when it is 4")
    void shouldRejectTheAgeWhenItIs4() {
        assertEquals("La edad debe ser un número entero de 5 a 99",
                VisitorValidator.validate(new Visitor("Ana Ruiz", 4, false)));
    }

    // TC-PLAN-006
    @Test
    @DisplayName("should register the visitor when the age is 99")
    void shouldRegisterTheVisitorWhenTheAgeIs99() {
        assertEquals("Visitante registrado", VisitorValidator.validate(new Visitor("Ana Ruiz", 99, false)));
    }

    @Test
    @DisplayName("should reject the age when it is 100")
    void shouldRejectTheAgeWhenItIs100() {
        assertEquals("La edad debe ser un número entero de 5 a 99",
                VisitorValidator.validate(new Visitor("Ana Ruiz", 100, false)));
    }

    // TC-PLAN-007
    @Test
    @DisplayName("should register the school group visitor when the age is 17")
    void shouldRegisterTheSchoolGroupVisitorWhenTheAgeIs17() {
        assertEquals("Visitante registrado", VisitorValidator.validate(new Visitor("Ana Ruiz", 17, true)));
    }
}
