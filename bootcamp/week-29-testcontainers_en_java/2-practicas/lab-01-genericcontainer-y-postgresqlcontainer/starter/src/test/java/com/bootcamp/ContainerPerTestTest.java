package com.bootcamp;

// PASO 3: un campo sin static arranca un contenedor por test
// import static org.assertj.core.api.Assertions.assertThat;

// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.SQLException;
// import java.sql.Statement;
// import org.junit.jupiter.api.RepeatedTest;
// import org.testcontainers.junit.jupiter.Container;
// import org.testcontainers.junit.jupiter.Testcontainers;
// import org.testcontainers.postgresql.PostgreSQLContainer;

// @Testcontainers
// class ContainerPerTestTest {

    // @Container
    // PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18.7-alpine");

    // @RepeatedTest(3)
    // void shouldStartWithAnEmptyDatabase() throws SQLException {
        // System.out.println("contenedor " + postgres.getContainerId().substring(0, 12));

        // try (Connection connection = DriverManager.getConnection(
                 // postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
             // Statement statement = connection.createStatement()) {
            // statement.execute("CREATE TABLE visits (id int)");
            // statement.execute("INSERT INTO visits VALUES (1)");
            // var result = statement.executeQuery("SELECT count(*) FROM visits");
            // result.next();
            // assertThat(result.getInt(1)).isEqualTo(1);
        // }
    // }
// }
