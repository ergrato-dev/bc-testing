package com.bootcamp;

// PASO 1

// import static org.assertj.core.api.Assertions.assertThat;
// import static org.assertj.core.api.Assertions.catchThrowable;

// import io.cucumber.java.en.Then;
// import io.cucumber.java.en.When;

// public class PaymentSteps {

//     private final OrderService orders;
//     private final ScenarioState state;

//     public PaymentSteps(OrderService orders, ScenarioState state) {
//         this.orders = orders;
//         this.state = state;
//     }

//     @When("pago {int} pesos")
//     public void iPay(int payment) {
//         state.setError(catchThrowable(() -> orders.pay(state.getOrder().id(), payment)));
//     }

//     @Then("el pago se rechaza con {string}")
//     public void thePaymentIsRejectedWith(String message) {
//         assertThat(state.getError()).hasMessage(message);
//     }

//     @Then("el pago se acepta")
//     public void thePaymentIsAccepted() {
//         assertThat(state.getError()).isNull();
//     }
// }
