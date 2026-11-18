package com.bootcamp.bdd;

import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.spring.ScenarioScope;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.client.EntityExchangeResult;

/**
 * La última respuesta de la API, compartida por las clases de steps. Una nueva por escenario.
 * Los steps reciben un proxy: se usa siempre con métodos, nunca con campos (teoría 03).
 */
@Component
@ScenarioScope
public class ScenarioState {

    private int status;
    private String body;

    void record(EntityExchangeResult<String> result) {
        status = result.getStatus().value();
        body = result.getResponseBody();
    }

    /** Si el código no es el esperado, el mensaje del fallo incluye el cuerpo de la respuesta. */
    void expectStatus(int expected) {
        assertThat(status).as("response body: %s", body).isEqualTo(expected);
    }

    String body() {
        return body;
    }
}
