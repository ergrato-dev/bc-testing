package com.bootcamp;

// ============================================
// TEST SUITE: ItemService (Java)
// ============================================
// TODO: crea src/main/java/com/bootcamp/ItemService.java con la lógica de tu dominio.
// TODO: sustituye cada @Disabled por un test AAA real (mismos TC que en test-plan.md
//       y que en las suites JavaScript y Python).

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ItemServiceTest {

    // TODO: declara aquí la instancia del servicio bajo prueba.

    @BeforeEach
    void setUp() {
        // TODO: crea una instancia nueva del servicio antes de cada test.
    }

    @Test
    @Disabled("TODO: happy path (TC-001)")
    @DisplayName("should create item when payload is valid")
    void shouldCreateItemWhenPayloadIsValid() {
    }

    @Test
    @Disabled("TODO: validación de nombre requerido (TC-002)")
    @DisplayName("should throw ValidationError when name is empty")
    void shouldThrowValidationErrorWhenNameIsEmpty() {
    }

    @Test
    @Disabled("TODO: validación de monto (TC-003)")
    @DisplayName("should throw ValidationError when amount is negative")
    void shouldThrowValidationErrorWhenAmountIsNegative() {
    }
}
