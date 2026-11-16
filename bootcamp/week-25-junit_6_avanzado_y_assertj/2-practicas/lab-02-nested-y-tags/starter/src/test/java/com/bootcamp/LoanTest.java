package com.bootcamp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
// PASO 1: descomenta este import.
// import org.junit.jupiter.api.Nested;
// PASO 4: descomenta este import.
// import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@DisplayName("Loan")
class LoanTest {

    private static final LocalDate DUE = LocalDate.of(2026, 3, 16);

    private Loan loan;

    @BeforeEach
    void createLoan() {
        loan = new Loan(DUE);
    }

    // ------------------------------------------------------------------
    // PASO 0: tres tests que repiten el mismo Arrange y llevan el contexto
    // ("returned") en el nombre. Ejecútalos y bórralos en el PASO 3.
    // ------------------------------------------------------------------

    @Test
    @DisplayName("returned: should count three days late")
    void returnedShouldCountThreeDaysLate() {
        loan.returnOn(DUE.plusDays(3));

        assertThat(loan.daysLate()).isEqualTo(3);
    }

    @Test
    @DisplayName("returned: should reject a second return")
    void returnedShouldRejectASecondReturn() {
        loan.returnOn(DUE.plusDays(3));

        assertThatThrownBy(() -> loan.returnOn(DUE.plusDays(4)))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("loan already returned");
    }

    @Test
    @DisplayName("returned: should reject a renewal")
    void returnedShouldRejectARenewal() {
        loan.returnOn(DUE.plusDays(3));

        assertThatThrownBy(loan::renew)
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("cannot renew a returned loan");
    }

    // ------------------------------------------------------------------
    // PASO 1: el contexto "activo" como clase anidada.
    // ------------------------------------------------------------------

    // @Nested
    // @DisplayName("when active")
    // class WhenActive {
    //
    //     @Test
    //     @DisplayName("should not be overdue on the due date")
    //     void shouldNotBeOverdueOnTheDueDate() {
    //         assertThat(loan.isOverdue(DUE)).isFalse();
    //     }
    //
    //     @Test
    //     // PASO 4: descomenta la etiqueta.
    //     // @Tag("smoke")
    //     @DisplayName("should be overdue the day after the due date")
    //     void shouldBeOverdueTheDayAfterTheDueDate() {
    //         assertThat(loan.isOverdue(DUE.plusDays(1))).isTrue();
    //     }
    //
    //     @Test
    //     @DisplayName("should move the due date one week when renewed")
    //     void shouldMoveTheDueDateOneWeekWhenRenewed() {
    //         loan.renew();
    //
    //         assertThat(loan.dueDate()).isEqualTo(DUE.plusDays(7));
    //     }
    //
    //     // PASO 2: un contexto dentro de otro, con su propio @BeforeEach.
    //     // @Nested
    //     // @DisplayName("after two renewals")
    //     // class AfterTwoRenewals {
    //     //
    //     //     @BeforeEach
    //     //     void renewTwice() {
    //     //         loan.renew();
    //     //         loan.renew();
    //     //     }
    //     //
    //     //     @Test
    //     //     @DisplayName("should reject a third renewal")
    //     //     void shouldRejectAThirdRenewal() {
    //     //         assertThatThrownBy(loan::renew)
    //     //             .isInstanceOf(IllegalStateException.class)
    //     //             .hasMessage("renewal limit reached");
    //     //     }
    //     // }
    // }

    // ------------------------------------------------------------------
    // PASO 3: el Arrange repetido del PASO 0 pasa a un @BeforeEach.
    // ------------------------------------------------------------------

    // @Nested
    // @DisplayName("when returned three days late")
    // class WhenReturnedThreeDaysLate {
    //
    //     @BeforeEach
    //     void returnLate() {
    //         loan.returnOn(DUE.plusDays(3));
    //     }
    //
    //     @Test
    //     // PASO 4: descomenta la etiqueta.
    //     // @Tag("smoke")
    //     @DisplayName("should count three days late")
    //     void shouldCountThreeDaysLate() {
    //         assertThat(loan.daysLate()).isEqualTo(3);
    //     }
    //
    //     @Test
    //     @DisplayName("should never be overdue again")
    //     void shouldNeverBeOverdueAgain() {
    //         assertThat(loan.isOverdue(DUE.plusDays(30))).isFalse();
    //     }
    //
    //     @Test
    //     @DisplayName("should reject a second return")
    //     void shouldRejectASecondReturn() {
    //         assertThatThrownBy(() -> loan.returnOn(DUE.plusDays(4)))
    //             .isInstanceOf(IllegalStateException.class)
    //             .hasMessage("loan already returned");
    //     }
    //
    //     @Test
    //     @DisplayName("should reject a renewal")
    //     void shouldRejectARenewal() {
    //         assertThatThrownBy(loan::renew)
    //             .isInstanceOf(IllegalStateException.class)
    //             .hasMessage("cannot renew a returned loan");
    //     }
    // }
}
