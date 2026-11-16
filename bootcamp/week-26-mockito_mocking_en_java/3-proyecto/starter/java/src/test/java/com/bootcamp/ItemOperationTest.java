package com.bootcamp;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Plantilla de la suite con Mockito para la operación principal de tu dominio
 * (un préstamo, una reserva, una venta). Renombra la clase: PieceLendingTest,
 * BookingTest...
 *
 * Campos que faltan (paso 3 del README):
 * - un mock por cada dependencia del servicio: repositorio, servicio externo y notificador;
 * - el servicio, creado por MockitoExtension con esos mocks;
 * - un captor para el objeto que el servicio guarda o envía.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ItemService operation")
class ItemOperationTest {

    @Test
    @Disabled("TODO: stub del repositorio y del servicio externo; verifica lo que se guarda con un captor")
    @DisplayName("should save the item after the operation")
    void shouldSaveTheItemAfterTheOperation() {
    }

    @Test
    @Disabled("TODO: captura el aviso y compara todos sus campos")
    @DisplayName("should notify with the operation details")
    void shouldNotifyWithTheOperationDetails() {
    }

    @Test
    @Disabled("TODO: comprueba con InOrder que se guarda antes de notificar")
    @DisplayName("should save before notifying")
    void shouldSaveBeforeNotifying() {
    }

    @Test
    @Disabled("TODO: el repositorio no encuentra el item; el servicio externo no recibe ninguna llamada")
    @DisplayName("should reject an unknown item")
    void shouldRejectAnUnknownItem() {
    }

    @Test
    @Disabled("TODO: el servicio externo dice que no; no se guarda ni se notifica nada")
    @DisplayName("should reject the operation when the external service says no")
    void shouldRejectTheOperationWhenTheExternalServiceSaysNo() {
    }

    @Test
    @Disabled("TODO: el servicio externo lanza una excepción; no se guarda ni se notifica nada")
    @DisplayName("should change nothing when the external service is down")
    void shouldChangeNothingWhenTheExternalServiceIsDown() {
    }
}
