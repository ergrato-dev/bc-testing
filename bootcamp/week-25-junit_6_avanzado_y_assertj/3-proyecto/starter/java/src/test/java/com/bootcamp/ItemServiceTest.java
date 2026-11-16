package com.bootcamp;

// ============================================
// TEST SUITE: ItemService (Java), semana 25
// ============================================
// Esta plantilla sustituye a tu ItemServiceTest.java de la semana 6.
// Pasa cada test de entonces a su contexto y quita los @Disabled uno a uno.
//
// TODO: renombra ItemService e ItemServiceTest a la entidad de tu dominio.
// TODO: cambia los @DisplayName de los contextos por las operaciones de tu servicio.
// TODO: usa solo AssertJ (assertThat, assertThatThrownBy, assertSoftly).

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("ItemService")
class ItemServiceTest {

    // TODO: declara aquí el servicio bajo prueba.

    @BeforeEach
    void createService() {
        // TODO: crea un servicio nuevo antes de cada test.
    }

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @Disabled("TODO: TC-001, comprueba todos los campos del resultado con assertSoftly")
        @DisplayName("should create the item when the data is valid (TC-001)")
        void shouldCreateTheItemWhenTheDataIsValid() {
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   "})
        @Disabled("TODO: TC-002, un solo test para null, vacío y en blanco")
        @DisplayName("should reject a missing name (TC-002)")
        void shouldRejectAMissingName(String name) {
        }

        @Test
        @Disabled("TODO: TC-003, conviértelo en @ParameterizedTest con 0, -1 y el mínimo de tu dominio")
        @DisplayName("should reject an amount that is not positive (TC-003)")
        void shouldRejectAnAmountThatIsNotPositive() {
        }

        // TODO: añade los casos frontera de tu dominio (longitud máxima, formato del código, duplicados).
    }

    @Nested
    @DisplayName("TODO: segunda operación del dominio")
    class SecondOperation {

        // TODO: un @BeforeEach con los datos que comparten los tests de este contexto.

        @Test
        @Disabled("TODO: camino feliz de la segunda operación")
        @DisplayName("should ... when ...")
        void happyPath() {
        }

        @Test
        @Disabled("TODO: la operación rechaza un estado no válido")
        @DisplayName("should reject ... when ...")
        void rejectsAnInvalidState() {
        }
    }

    // TODO: un tercer @Nested con un @ParameterizedTest + @CsvSource para el cálculo de tu dominio.
}
