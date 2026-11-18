package com.bootcamp;

import static org.assertj.core.api.Assertions.assertThat;
// PASO 2
// import static org.assertj.core.api.Assertions.catchThrowable;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/** Cucumber crea una instancia nueva de esta clase para cada escenario: sus campos no se comparten. */
public class StampCardSteps {

    private StampCard card;
    // PASO 2
    // private Throwable error;

    @Given("una tarjeta nueva")
    public void aNewCard() {
        card = new StampCard();
    }

    // PASO 1
    // @Given("una tarjeta con {int} sello(s)")
    // public void aCardWithStamps(int stamps) {
    //     card = new StampCard();
    //     card.stamp(stamps);
    // }

    @When("compro {int} café(s)")
    public void iBuyCoffees(int coffees) {
        card.stamp(coffees);
    }

    // PASO 2
    // @When("canjeo un café gratis")
    // public void iRedeemAFreeCoffee() {
    //     card.redeem();
    // }

    // PASO 2
    // @When("intento canjear un café gratis")
    // public void iTryToRedeemAFreeCoffee() {
    //     error = catchThrowable(card::redeem);
    // }

    @Then("la tarjeta tiene {int} sello(s)")
    public void theCardHasStamps(int expected) {
        assertThat(card.stamps()).isEqualTo(expected);
    }

    // PASO 1
    // @Then("tengo un café gratis")
    // public void iHaveAFreeCoffee() {
    //     assertThat(card.hasFreeCoffee()).isTrue();
    // }

    // PASO 2
    // @Then("no tengo un café gratis")
    // public void iHaveNoFreeCoffee() {
    //     assertThat(card.hasFreeCoffee()).isFalse();
    // }

    // PASO 2
    // @Then("la cafetería responde {string}")
    // public void theShopAnswers(String message) {
    //     assertThat(error).hasMessage(message);
    // }

    // PASO 3
    // @Then("el café gratis está {string}")
    // public void theFreeCoffeeIs(String availability) {
    //     assertThat(card.hasFreeCoffee()).isEqualTo(availability.equals("disponible"));
    // }
}
