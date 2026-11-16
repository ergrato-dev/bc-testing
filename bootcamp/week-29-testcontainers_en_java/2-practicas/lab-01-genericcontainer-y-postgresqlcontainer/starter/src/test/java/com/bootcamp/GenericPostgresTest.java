package com.bootcamp;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
class GenericPostgresTest {

    @Container
    static GenericContainer<?> postgres = new GenericContainer<>(DockerImageName.parse("postgres:18.7-alpine"))
        .withEnv("POSTGRES_USER", "lab")
        .withEnv("POSTGRES_PASSWORD", "lab")
        .withEnv("POSTGRES_DB", "lab")
        .withExposedPorts(5432)
        .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*\\s", 2));

    @Test
    void shouldAnswerWithTheVersionOfTheImage() throws SQLException {
        try (Connection connection = DriverManager.getConnection(jdbcUrl(), "lab", "lab");
             ResultSet result = connection.createStatement().executeQuery("SHOW server_version")) {
            result.next();
            assertThat(result.getString(1)).isEqualTo("18.7");
        }
    }

    // PASO 1: el puerto del host es aleatorio
    // @Test
    // void shouldPublishPostgresOnARandomHostPort() {
        // int hostPort = postgres.getMappedPort(5432);
        // System.out.println("5432 en el contenedor -> " + postgres.getHost() + ":" + hostPort);

        // assertThat(hostPort).isNotEqualTo(5432);
    // }

    private static String jdbcUrl() {
        return "jdbc:postgresql://" + postgres.getHost() + ":" + postgres.getMappedPort(5432) + "/lab";
    }
}
