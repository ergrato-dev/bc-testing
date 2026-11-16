package com.bootcamp;

// PASO 1
// import static org.assertj.core.api.Assertions.assertThat;

// import java.time.Clock;
// import java.time.Instant;
// import java.time.LocalDate;
// import java.time.ZoneOffset;
// PASO 2
// import java.time.Duration;
// import java.util.List;
// import org.junit.jupiter.api.BeforeEach;
// import org.springframework.core.ParameterizedTypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
// PASO 1
// import org.springframework.test.context.bean.override.convention.TestBean;
import org.springframework.test.web.servlet.client.RestTestClient;

/** La aplicación entera en un puerto aleatorio. RestTestClient le habla por HTTP de verdad. */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
class LoanApiTest {

    // PASO 1: el bean clock de la aplicación, sustituido por un reloj que controla el test
    // private static final Instant MARCH_20 = Instant.parse("2026-03-20T10:00:00Z");
    // private static final MutableClock CLOCK = new MutableClock(MARCH_20, ZoneOffset.UTC);

    // @TestBean
    // private Clock clock;

    // static Clock clock() {
        // return CLOCK;
    // }

    @Autowired
    private RestTestClient client;

    // PASO 2: el reloj es un bean compartido; cada test lo devuelve al 20 de marzo
    // @BeforeEach
    // void resetClock() {
        // CLOCK.set(MARCH_20);
    // }

    @Test
    void shouldRespond404WhenTheLoanDoesNotExist() {
        client.get().uri("/api/loans/L-404")
            .exchange()
            .expectStatus().isNotFound()
            .expectBody().jsonPath("$.detail").isEqualTo("loan not found: L-404");
    }

    // PASO 1: el vencimiento sale del reloj del test
    // @Test
    // void shouldSetTheDueDateTwoWeeksFromToday() {
        // Loan loan = openLoan("978-0307474728", "M-7");

        // assertThat(loan.dueDate()).isEqualTo(LocalDate.of(2026, 4, 3));
    // }

    // PASO 2: el tiempo avanza cuando el test lo dice
    // @Test
    // void shouldListALoanAsOverdueTheDayAfterItsDueDate() {
        // Loan loan = openLoan("978-0141439518", "M-8");

        // CLOCK.advance(Duration.ofDays(15));

        // List<Loan> overdue = client.get().uri("/api/loans/overdue")
            // .exchange()
            // .expectStatus().isOk()
            // .expectBody(new ParameterizedTypeReference<List<Loan>>() { })
            // .returnResult().getResponseBody();
        // assertThat(overdue).extracting(Loan::id).contains(loan.id());
    // }

    // PASO 1: abre un préstamo con un POST y devuelve el cuerpo de la respuesta
    // private Loan openLoan(String isbn, String memberId) {
        // return client.post().uri("/api/loans")
            // .body(new LoanRequest(isbn, memberId))
            // .exchange()
            // .expectStatus().isCreated()
            // .expectBody(Loan.class)
            // .returnResult().getResponseBody();
    // }
}
