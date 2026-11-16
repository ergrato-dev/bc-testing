package com.bootcamp;

import static org.assertj.core.api.Assertions.assertThat;
// PASO 5
// import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

/** Slice JSON: solo Jackson, con la configuración spring.jackson.* de la aplicación. */
@JsonTest
class LoanJsonTest {

    private static final Loan OPEN = new Loan("L-1", "978-0307474728", "M-7",
        LocalDate.of(2026, 3, 20), null, BigDecimal.ZERO, Loan.Status.OPEN, "cliente habitual");

    private static final Loan RETURNED = new Loan("L-2", "978-0307474728", "M-7",
        LocalDate.of(2026, 3, 20), LocalDate.of(2026, 3, 23), new BigDecimal("1.50"), Loan.Status.RETURNED, null);

    @Autowired
    private JacksonTester<Loan> json;

    @Test
    void shouldWriteTheIsbn() throws Exception {
        assertThat(json.write(OPEN)).extractingJsonPathStringValue("$.isbn").isEqualTo("978-0307474728");
    }

    // PASO 1: las fechas salen como texto ISO, no como números
    // @Test
    // void shouldWriteTheDueDateAsAnIsoDate() throws Exception {
        // assertThat(json.write(OPEN)).extractingJsonPathStringValue("$.dueDate").isEqualTo("2026-03-20");
    // }

    // PASO 2: @JsonProperty cambia el nombre; @JsonIgnore deja fuera el campo
    // @Test
    // void shouldRenameTheMemberAndHideTheLibrarianNote() throws Exception {
        // assertThat(json.write(OPEN))
            // .hasJsonPathStringValue("$.member")
            // .doesNotHaveJsonPath("$.memberId")
            // .doesNotHaveJsonPath("$.librarianNote");
    // }

    // PASO 3: la configuración de application.properties también se aplica en el slice
    // @Test
    // void shouldOmitTheReturnDateOfAnOpenLoan() throws Exception {
        // assertThat(json.write(OPEN)).doesNotHaveJsonPath("$.returnedOn");
    // }

    // PASO 4: el JSON entero, comparado con un archivo
    // @Test
    // void shouldMatchTheContractOfAReturnedLoan() throws Exception {
        // assertThat(json.write(RETURNED)).isEqualToJson("returned-loan.json");
    // }

    // PASO 5: leer JSON: campos desconocidos y valores que no existen
    // @Test
    // void shouldReadALoanAndIgnoreUnknownFields() throws Exception {
        // Loan loan = json.parseObject("""
            // {"id": "L-2", "isbn": "978-0307474728", "member": "M-7", "dueDate": "2026-03-20",
             // "returnedOn": "2026-03-23", "lateFee": 1.50, "status": "RETURNED", "branch": "Centro"}
            // """);

        // assertThat(loan).isEqualTo(RETURNED);
    // }

    // @Test
    // void shouldRejectAStatusThatDoesNotExist() {
        // assertThatThrownBy(() -> json.parseObject("""
            // {"id": "L-3", "status": "LOST"}
            // """))
            // .hasMessageContaining("from String \"LOST\": not one of the values accepted");
    // }
}
