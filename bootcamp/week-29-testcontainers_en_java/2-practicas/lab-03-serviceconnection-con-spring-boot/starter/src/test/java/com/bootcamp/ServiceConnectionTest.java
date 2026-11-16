package com.bootcamp;

// PASO 2: @ServiceConnection en lugar de @DynamicPropertySource
// import static org.assertj.core.api.Assertions.assertThat;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
// import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
// import org.springframework.context.annotation.Import;
// import org.testcontainers.junit.jupiter.Container;
// import org.testcontainers.junit.jupiter.Testcontainers;
// import org.testcontainers.postgresql.PostgreSQLContainer;

// @JdbcTest
// @Import(StockRepository.class)
// @Testcontainers
// class ServiceConnectionTest {

    // @Container
    // @ServiceConnection
    // static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18.7-alpine");

    // @Autowired
    // private StockRepository stock;

    // @Test
    // void shouldReturnEmptyForAnUnknownSku() {
        // assertThat(stock.quantity("SKU-404")).isEmpty();
    // }
// }
