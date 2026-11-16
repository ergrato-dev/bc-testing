package com.bootcamp;

import static org.assertj.core.api.Assertions.assertThat;
// PASO 1
// import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

@Testcontainers
class InitScriptTest {

    @Container
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18.7-alpine")
        .withInitScript("schema.sql");

    private TicketRepository tickets;

    @BeforeEach
    void setUp() {
        tickets = new TicketRepository(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
    }

    @Test
    void shouldFindATicketBySeatAfterSavingIt() throws SQLException {
        Ticket saved = tickets.save("A1", 850);

        assertThat(tickets.findBySeat("A1")).contains(saved);
    }

    // PASO 1: las reglas que pone la tabla
    // @Test
    // void shouldRejectAPriceOfZeroWithTheCheckConstraint() {
        // assertThatThrownBy(() -> tickets.save("B1", 0))
            // .isInstanceOf(SQLException.class)
            // .hasMessageContaining("tickets_price_cents_check")
            // .extracting(error -> ((SQLException) error).getSQLState())
            // .isEqualTo("23514");
    // }

    // @Test
    // void shouldRejectASecondTicketForTheSameSeat() throws SQLException {
        // tickets.save("C1", 850);

        // assertThatThrownBy(() -> tickets.save("C1", 900))
            // .isInstanceOf(SQLException.class)
            // .hasMessageContaining("tickets_seat_key")
            // .extracting(error -> ((SQLException) error).getSQLState())
            // .isEqualTo("23505");
    // }
}
