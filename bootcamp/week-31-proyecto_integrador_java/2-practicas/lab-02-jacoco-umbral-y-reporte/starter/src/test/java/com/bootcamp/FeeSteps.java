package com.bootcamp;

import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class FeeSteps {

    private int fee;

    @When("devuelvo un libro con {int} días de retraso")
    public void iReturnABookDaysLate(int daysLate) {
        fee = LateFees.lateFee(daysLate);
    }

    @Then("pago una multa de {int}")
    public void iPayAFeeOf(int expected) {
        assertThat(fee).isEqualTo(expected);
    }
}
