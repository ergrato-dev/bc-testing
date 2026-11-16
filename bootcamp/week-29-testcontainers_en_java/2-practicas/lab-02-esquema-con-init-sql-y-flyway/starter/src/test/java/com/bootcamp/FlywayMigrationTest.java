package com.bootcamp;

// PASO 2
// import static org.assertj.core.api.Assertions.assertThat;

// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.ResultSet;
// import java.sql.SQLException;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.MigrateResult;
import org.junit.jupiter.api.BeforeAll;
// PASO 2
// import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

@Testcontainers
class FlywayMigrationTest {

    @Container
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18.7-alpine");

    private static MigrateResult migration;

    @BeforeAll
    static void migrate() {
        migration = Flyway.configure()
            .dataSource(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
            .load()
            .migrate();
    }

    // PASO 2: el esquema lo crean las migraciones de producción
    // @Test
    // void shouldApplyBothMigrations() {
        // assertThat(migration.migrationsExecuted).isEqualTo(2);
        // assertThat(migration.targetSchemaVersion).isEqualTo("2");
    // }

    // @Test
    // void shouldStampTheSaleTimeWithTheDefaultOfV2() throws SQLException {
        // new TicketRepository(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
            // .save("D1", 850);

        // try (Connection connection = DriverManager.getConnection(
                 // postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
             // ResultSet result = connection.createStatement()
                 // .executeQuery("SELECT sold_at FROM tickets WHERE seat = 'D1'")) {
            // result.next();
            // assertThat(result.getTimestamp("sold_at")).isNotNull();
        // }
    // }
    // PASO 3: la tabla de historial de Flyway
    // @Test
    // void shouldRecordEachMigrationInTheHistoryTable() throws SQLException {
        // try (Connection connection = DriverManager.getConnection(
                 // postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
             // ResultSet result = connection.createStatement().executeQuery(
                 // "SELECT version, description, success FROM flyway_schema_history ORDER BY installed_rank")) {
            // StringBuilder history = new StringBuilder();
            // while (result.next()) {
                // history.append(result.getString("version")).append(" ")
                    // .append(result.getString("description")).append(" ")
                    // .append(result.getBoolean("success")).append("\n");
            // }
            // System.out.print(history);

            // assertThat(history.toString()).isEqualTo("1 create tickets true\n2 add sold at true\n");
        // }
    // }
}
