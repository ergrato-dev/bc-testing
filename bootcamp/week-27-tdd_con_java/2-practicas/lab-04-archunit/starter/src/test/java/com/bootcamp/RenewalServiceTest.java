package com.bootcamp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bootcamp.domain.Loan;
import com.bootcamp.infrastructure.InMemoryLoanRepository;
import com.bootcamp.service.RenewalService;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RenewalServiceTest {

    // Un reloj parado en el 16 de marzo de 2026: LocalDate.now(clock) siempre devuelve ese día
    private static final Clock MARCH_16 = Clock.fixed(
        LocalDate.of(2026, 3, 16).atStartOfDay().toInstant(ZoneOffset.UTC), ZoneOffset.UTC);

    private InMemoryLoanRepository loans;
    private RenewalService service;

    @BeforeEach
    void setUp() {
        loans = new InMemoryLoanRepository();
        service = new RenewalService(loans, MARCH_16);
    }

    @Test
    void shouldExtendTheDueDateByTwoWeeks() {
        loans.save(new Loan("L-1", "M-7", LocalDate.of(2026, 3, 20), 0));

        Loan renewed = service.renew("L-1");

        assertThat(renewed.dueDate()).isEqualTo(LocalDate.of(2026, 4, 3));
        assertThat(loans.findById("L-1")).contains(renewed);
    }

    @Test
    void shouldRejectALoanThatIsAlreadyOverdue() {
        loans.save(new Loan("L-2", "M-7", LocalDate.of(2026, 3, 15), 0));

        assertThatThrownBy(() -> service.renew("L-2"))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("loan is overdue: L-2");
    }
}
