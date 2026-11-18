package com.bootcamp;

import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.java.Before;
// PASO 1
// import io.cucumber.java.DataTableType;
// PASO 1
// import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDate;
// PASO 1
// import java.util.List;
// import java.util.Map;
// PASO 1
// import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.servlet.client.EntityExchangeResult;
import org.springframework.test.web.servlet.client.RestTestClient;

/** Los steps hablan HTTP con la API, como lo haría un cliente. */
public class BookingSteps {

    private final RestTestClient client;
    private final BookingStore store;
    private EntityExchangeResult<String> response;

    public BookingSteps(RestTestClient client, BookingStore store) {
        this.client = client;
        this.store = store;
    }

    @Before
    public void startWithNoBookings() {
        store.clear();
    }

    // PASO 1
    // @DataTableType
    // public Booking booking(Map<String, String> row) {
    //     return new Booking(row.get("sala"), LocalDate.parse(row.get("fecha")), row.get("reservada por"));
    // }

    // PASO 1
    // @Given("estas reservas:")
    // public void theseBookings(List<Booking> bookings) {
    //     bookings.forEach(this::post);
    // }

    @When("{word} reserva la sala {word} para el {isoDate}")
    public void someoneBooks(String person, String room, LocalDate date) {
        post(new Booking(room, date, person));
    }

    @Then("la API responde {int}")
    public void theApiAnswers(int status) {
        assertThat(response.getStatus().value()).isEqualTo(status);
    }

    // PASO 1
    // @Then("el motivo es {string}")
    // public void theReasonIs(String detail) {
    //     assertThat(response.getResponseBody()).contains("\"detail\":\"" + detail + "\"");
    // }

    // PASO 1
    // @Then("las reservas del {isoDate} son:")
    // public void theBookingsOn(LocalDate date, List<Booking> expected) {
    //     List<Booking> bookings = client.get().uri("/api/bookings?date={date}", date)
    //         .exchange()
    //         .expectStatus().isOk()
    //         .expectBody(new ParameterizedTypeReference<List<Booking>>() { })
    //         .returnResult().getResponseBody();
    //     assertThat(bookings).containsExactlyElementsOf(expected);
    // }

    private void post(Booking booking) {
        response = client.post().uri("/api/bookings")
            .body(booking)
            .exchange()
            .expectBody(String.class)
            .returnResult();
    }
}
