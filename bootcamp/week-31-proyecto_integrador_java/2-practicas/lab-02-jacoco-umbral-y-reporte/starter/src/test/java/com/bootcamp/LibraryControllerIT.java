package com.bootcamp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

/** Arranca Spring MVC (sin base de datos): va en Failsafe. */
@WebMvcTest(LibraryController.class)
class LibraryControllerIT {

    @Autowired
    MockMvcTester mvc;

    @MockitoBean
    LoanService loans;

    @Test
    void feeForThreeDaysLate() {
        assertThat(mvc.get().uri("/fees?daysLate=3"))
                .hasStatusOk()
                .bodyJson().extractingPath("$.fee").isEqualTo(150);
    }

    @Test
    void checkoutOfAnUnknownBookIsNotFound() {
        when(loans.checkout("978-9")).thenThrow(new NoSuchElementException("unknown book: 978-9"));

        assertThat(mvc.post().uri("/books/978-9/checkout")).hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    void checkoutWithoutStockIsConflict() {
        when(loans.checkout("978-1")).thenThrow(new IllegalStateException("no stock: 978-1"));

        assertThat(mvc.post().uri("/books/978-1/checkout")).hasStatus(HttpStatus.CONFLICT);
    }

    @Test
    void checkoutReturnsTheBook() {
        when(loans.checkout("978-1")).thenReturn(new Book("978-1", "Rayuela", 1));

        assertThat(mvc.post().uri("/books/978-1/checkout"))
                .hasStatusOk()
                .bodyJson().extractingPath("$.stock").isEqualTo(1);
    }
}
