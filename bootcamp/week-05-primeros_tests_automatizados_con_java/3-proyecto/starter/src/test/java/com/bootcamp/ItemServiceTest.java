package com.bootcamp;

// ============================================
// TEST SUITE: ItemService
// Servicio para gestión de elementos del dominio
// ============================================

// NOTA PARA EL APRENDIZ:
// Adapta esta suite a tu dominio asignado (mismas funciones que en las semanas 03 y 04).
// Ejemplos neutrales (no los uses en tu entrega):
// - Museo: calculateTicketPrice, isVisitSlotAvailable
// - Planetario: calculateShowPrice, isSeatAvailable
// - Acuario: calculateGroupPrice, isTankCapacityValid
//
// TODO: crea src/main/java/com/bootcamp/ItemService.java con las funciones de tu dominio.
// TODO: sustituye cada @Disabled por un test real con Arrange / Act / Assert
//       y renombra cada método con el patrón should[ExpectedResult]When[Condition].

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemServiceTest {

    // TODO: declara el servicio bajo prueba
    // private ItemService service;

    @BeforeEach
    void setUp() {
        // TODO: service = new ItemService();
    }

    // ============================================
    // BLOQUE 1: Creación
    // ============================================
    // TODO: 1. Caso válido (happy path)
    // TODO: 2. Campo requerido faltante (assertThrows)
    // TODO: 3. Valor fuera de rango (assertThrows)

    @Test
    @Disabled("TODO: caso válido de creación")
    @DisplayName("should create item when payload is valid")
    void shouldCreateItemWhenPayloadIsValid() {
    }

    // ============================================
    // BLOQUE 2: Cálculo
    // ============================================
    // TODO: 1. Cálculo base correcto
    // TODO: 2. Borde inferior (edge case)
    // TODO: 3. Borde superior (edge case)
    // Recuerda: con double usa assertEquals(expected, actual, delta).

    @Test
    @Disabled("TODO: cálculo base del dominio")
    @DisplayName("should calculate total when input is valid")
    void shouldCalculateTotalWhenInputIsValid() {
    }

    // ============================================
    // BLOQUE 3: Validación
    // ============================================
    // TODO: 1. Valor permitido
    // TODO: 2. Valor no permitido

    @Test
    @Disabled("TODO: validación de un valor permitido")
    @DisplayName("should return true when value is allowed")
    void shouldReturnTrueWhenValueIsAllowed() {
    }
}
