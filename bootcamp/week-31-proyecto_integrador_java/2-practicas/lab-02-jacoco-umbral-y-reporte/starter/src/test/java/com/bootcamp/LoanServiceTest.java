package com.bootcamp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    BookRepository books;

    @InjectMocks
    LoanService service;

    @Test
    void checkoutTakesOneCopyFromStock() {
        when(books.findByIsbn("978-1")).thenReturn(Optional.of(new Book("978-1", "Rayuela", 2)));

        Book lent = service.checkout("978-1");

        assertThat(lent.stock()).isEqualTo(1);
        verify(books).save(new Book("978-1", "Rayuela", 1));
    }

    @Test
    void checkoutWithoutStockFails() {
        when(books.findByIsbn("978-1")).thenReturn(Optional.of(new Book("978-1", "Rayuela", 0)));

        assertThatIllegalStateException()
                .isThrownBy(() -> service.checkout("978-1"))
                .withMessage("no stock: 978-1");
        verify(books, never()).save(any());
    }
}
