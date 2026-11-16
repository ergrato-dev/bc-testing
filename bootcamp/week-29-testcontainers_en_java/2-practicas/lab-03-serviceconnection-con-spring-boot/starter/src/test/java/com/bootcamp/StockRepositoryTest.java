package com.bootcamp;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

@JdbcTest
@Import(StockRepository.class)
@Testcontainers
class StockRepositoryTest {

    @Container
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18.7-alpine");

    @DynamicPropertySource
    static void datasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private StockRepository stock;

    @Test
    void shouldAddUpTheQuantityWhenTheSkuAlreadyExists() {
        stock.add("SKU-1", 5);
        stock.add("SKU-1", 3);

        assertThat(stock.quantity("SKU-1")).contains(8);
    }

    // PASO 1: la reserva y el rollback de cada test
    // @Test
    // void shouldReserveWhenThereIsEnoughStock() {
        // stock.add("SKU-1", 5);

        // assertThat(stock.reserve("SKU-1", 5)).isTrue();
        // assertThat(stock.quantity("SKU-1")).contains(0);
    // }

    // @Test
    // void shouldRefuseToReserveMoreThanTheStockAndLeaveItUntouched() {
        // stock.add("SKU-1", 2);

        // assertThat(stock.reserve("SKU-1", 3)).isFalse();
        // assertThat(stock.quantity("SKU-1")).contains(2);
    // }
}
