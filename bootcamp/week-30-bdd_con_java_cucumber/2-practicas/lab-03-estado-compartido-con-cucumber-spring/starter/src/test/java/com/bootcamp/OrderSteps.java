package com.bootcamp;

import static org.assertj.core.api.Assertions.assertThat;

// PASO 3
// import io.cucumber.java.After;
// PASO 2
// import io.cucumber.java.Before;
// PASO 3
// import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class OrderSteps {

    private final OrderService orders;
    private final ScenarioState state;

    /** cucumber-spring crea esta clase con los beans del contexto, como cualquier otro bean. */
    public OrderSteps(OrderService orders, ScenarioState state) {
        this.orders = orders;
        this.state = state;
    }

    // PASO 2
    // /** Antes de cada escenario: OrderService es un singleton y guarda los pedidos del anterior. */
    // @Before
    // public void clearOrders() {
    //     orders.clear();
    // }

    // PASO 3
    // /** Después de cada escenario fallido, deja los pedidos pendientes en la salida y en el reporte. */
    // @After
    // public void logPendingOrders(Scenario scenario) {
    //     if (scenario.isFailed()) {
    //         scenario.log("pending orders: " + orders.pending());
    //     }
    // }

    @Given("un pedido de {int} pesos")
    public void anOrderOf(int amount) {
        state.setOrder(orders.create(amount));
    }

    // PASO 1
    // @Then("el pedido está pagado")
    // public void theOrderIsPaid() {
    //     assertThat(orders.find(state.getOrder().id()).status()).isEqualTo(Order.Status.PAID);
    // }

    @Then("hay {int} pedido(s) pendiente(s)")
    public void thereArePendingOrders(int expected) {
        assertThat(orders.pending()).hasSize(expected);
    }
}
