package com.bootcamp;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.testcontainers.postgresql.PostgreSQLContainer;

/** Arranca PostgreSQL en un contenedor: va en Failsafe. */
@JdbcTest
@Import({BookRepository.class, BookRepositoryIT.Postgres.class})
class BookRepositoryIT {

    @TestConfiguration(proxyBeanMethods = false)
    static class Postgres {

        @Bean
        @ServiceConnection
        PostgreSQLContainer postgres() {
            return new PostgreSQLContainer("postgres:18.7-alpine");
        }
    }

    @Autowired
    BookRepository books;

    @Test
    void savesAndFindsABook() {
        books.save(new Book("978-1", "Rayuela", 2));

        assertThat(books.findByIsbn("978-1")).contains(new Book("978-1", "Rayuela", 2));
    }

    @Test
    void availableSkipsBooksWithoutStock() {
        books.save(new Book("978-1", "Rayuela", 2));
        books.save(new Book("978-2", "Ficciones", 0));

        assertThat(books.available()).extracting(Book::title).containsExactly("Rayuela");
    }
}
