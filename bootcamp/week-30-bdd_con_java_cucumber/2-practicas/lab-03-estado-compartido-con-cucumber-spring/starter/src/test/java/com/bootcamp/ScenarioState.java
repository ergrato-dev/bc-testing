package com.bootcamp;

import io.cucumber.spring.ScenarioScope;
import org.springframework.stereotype.Component;

/**
 * Lo que un escenario recuerda entre steps de clases distintas. Uno nuevo por escenario.
 * Los steps reciben un proxy: se usa siempre con métodos, nunca con campos.
 */
@Component
@ScenarioScope
public class ScenarioState {

    private Order order;
    private Throwable error;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }
}
