package com.bootcamp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
// PASO 3
// import static org.mockito.Mockito.doReturn;
// PASO 1
// import static org.mockito.Mockito.mockStatic;
// PASO 1
// import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
// PASO 1
// import org.mockito.MockedStatic;
// PASO 2
// import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RenewalServiceTest {

    private static final LocalDate TODAY = LocalDate.of(2026, 3, 16);
    private static final Loan FRESH = new Loan("L-1", "M-001", TODAY.plusDays(3), 0);
    private static final Loan RENEWED_ONCE = new Loan("L-2", "M-001", TODAY.plusDays(3), 1);

    @Mock
    private LoanRepository loans;

    // ------------------------------------------------------------------
    // PASO 0 (se borra en el PASO 2): FeeTable como mock
    // ------------------------------------------------------------------

    @Mock
    private FeeTable fees;

    // ------------------------------------------------------------------
    // PASO 2: FeeTable como spy
    // ------------------------------------------------------------------

    // @Spy
    // private FeeTable fees;

    @InjectMocks
    private RenewalService service;

    @Test
    @DisplayName("should reject a loan that does not exist")
    void shouldRejectALoanThatDoesNotExist() {
        when(loans.findById("L-9")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.renew("L-9"))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("unknown loan: L-9");
    }

    @Test
    @DisplayName("should reject a third renewal")
    void shouldRejectAThirdRenewal() {
        when(loans.findById("L-3")).thenReturn(Optional.of(new Loan("L-3", "M-001", TODAY, 2)));

        assertThatThrownBy(() -> service.renew("L-3"))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("renewal limit reached: L-3");
    }

    // ------------------------------------------------------------------
    // PASO 1: la fecha de hoy con mockStatic
    // ------------------------------------------------------------------

    // @Test
    // @DisplayName("should extend the due date by two weeks")
    // void shouldExtendTheDueDateByTwoWeeks() {
    //     when(loans.findById("L-1")).thenReturn(Optional.of(FRESH));
    //
    //     try (MockedStatic<LegacyCalendar> calendar = mockStatic(LegacyCalendar.class)) {
    //         calendar.when(LegacyCalendar::today).thenReturn(TODAY);
    //
    //         Renewal renewal = service.renew("L-1");
    //
    //         assertThat(renewal.loan().dueDate()).isEqualTo(TODAY.plusDays(17));
    //         verify(loans).save(renewal.loan());
    //     }
    // }
    //
    // @Test
    // @DisplayName("should reject a loan that is already overdue")
    // void shouldRejectALoanThatIsAlreadyOverdue() {
    //     when(loans.findById("L-1")).thenReturn(Optional.of(FRESH));
    //
    //     try (MockedStatic<LegacyCalendar> calendar = mockStatic(LegacyCalendar.class)) {
    //         calendar.when(LegacyCalendar::today).thenReturn(TODAY.plusDays(4));
    //
    //         assertThatThrownBy(() -> service.renew("L-1"))
    //             .isInstanceOf(IllegalStateException.class)
    //             .hasMessage("loan is overdue: L-1");
    //     }
    // }

    // ------------------------------------------------------------------
    // PASO 2: el spy calcula la tarifa real
    // ------------------------------------------------------------------

    // @Test
    // @DisplayName("should charge the second renewal with the real fee table")
    // void shouldChargeTheSecondRenewalWithTheRealFeeTable() {
    //     when(loans.findById("L-2")).thenReturn(Optional.of(RENEWED_ONCE));
    //
    //     try (MockedStatic<LegacyCalendar> calendar = mockStatic(LegacyCalendar.class)) {
    //         calendar.when(LegacyCalendar::today).thenReturn(TODAY);
    //
    //         Renewal renewal = service.renew("L-2");
    //
    //         assertThat(renewal.fee()).isEqualTo(FeeTable.SECOND_RENEWAL_FEE);
    //         verify(fees).renewalFee(2);
    //     }
    // }

    // ------------------------------------------------------------------
    // PASO 3: doReturn para cambiar una respuesta del spy
    // ------------------------------------------------------------------

    // @Test
    // @DisplayName("should charge the fee the spy was told to return")
    // void shouldChargeTheFeeTheSpyWasToldToReturn() {
    //     when(loans.findById("L-2")).thenReturn(Optional.of(RENEWED_ONCE));
    //     doReturn(150).when(fees).renewalFee(2);
    //
    //     try (MockedStatic<LegacyCalendar> calendar = mockStatic(LegacyCalendar.class)) {
    //         calendar.when(LegacyCalendar::today).thenReturn(TODAY);
    //
    //         assertThat(service.renew("L-2").fee()).isEqualTo(150);
    //     }
    // }
}
