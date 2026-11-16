package com.bootcamp;

// PASO 2: el módulo PostgreSQLContainer hace lo mismo con menos código
// import static org.assertj.core.api.Assertions.assertThat;

// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.ResultSet;
// import java.sql.SQLException;
// import org.junit.jupiter.api.Test;
// import org.testcontainers.junit.jupiter.Container;
// import org.testcontainers.junit.jupiter.Testcontainers;
// import org.testcontainers.postgresql.PostgreSQLContainer;

// @Testcontainers
// class ModulePostgresTest {

    // @Container
    // static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18.7-alpine");

    // @Test
    // void shouldGiveTheJdbcUrlAndTheCredentials() throws SQLException {
        // System.out.println(postgres.getJdbcUrl() + " " + postgres.getUsername() + "/" + postgres.getPassword());

        // try (Connection connection = DriverManager.getConnection(
                 // postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
             // ResultSet result = connection.createStatement().executeQuery("SELECT current_database()")) {
            // result.next();
            // assertThat(result.getString(1)).isEqualTo(postgres.getDatabaseName());
        // }
    // }
// }
