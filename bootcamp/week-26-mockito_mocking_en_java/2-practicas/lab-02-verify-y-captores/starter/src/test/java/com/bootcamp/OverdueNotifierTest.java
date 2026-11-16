package com.bootcamp;

import static org.assertj.core.api.Assertions.assertThat;
// PASO 1
// import static org.mockito.ArgumentMatchers.any;
// PASO 4
// import static org.mockito.Mockito.inOrder;
// PASO 1
// import static org.mockito.Mockito.never;
// PASO 1
// import static org.mockito.Mockito.times;
// PASO 1
// import static org.mockito.Mockito.verify;
// PASO 4
// import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
// PASO 3
// import org.mockito.ArgumentCaptor;
// PASO 3
// import org.mockito.Captor;
// PASO 4
// import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OverdueNotifierTest {

    private static final LocalDate TODAY = LocalDate.of(2026, 3, 16);
    private static final Clock CLOCK = Clock.fixed(TODAY.atStartOfDay().toInstant(ZoneOffset.UTC), ZoneOffset.UTC);

    private static final Loan DUNE = new Loan("L-1", "ana@example.com", "Dune", TODAY.minusDays(3));
    private static final Loan EMMA = new Loan("L-2", "luis@example.com", "Emma", TODAY.minusDays(1));

    @Mock
    private LoanRepository loans;

    @Mock
    private Notifier notifier;

    // PASO 3
    // @Captor
    // private ArgumentCaptor<Notice> notice;

    private OverdueNotifier overdueNotifier;

    @BeforeEach
    void createNotifier() {
        overdueNotifier = new OverdueNotifier(loans, notifier, CLOCK);
    }

    // ------------------------------------------------------------------
    // PASO 0: un test que solo mira el valor devuelto
    // ------------------------------------------------------------------

    @Test
    @DisplayName("should return the number of overdue loans")
    void shouldReturnTheNumberOfOverdueLoans() {
        when(loans.findOverdue(TODAY)).thenReturn(List.of(DUNE, EMMA));

        int sent = overdueNotifier.notifyOverdue();

        assertThat(sent).isEqualTo(2);
    }

    // ------------------------------------------------------------------
    // PASO 1: verify, times y never
    // ------------------------------------------------------------------

    // @Test
    // @DisplayName("should send one notice per overdue loan")
    // void shouldSendOneNoticePerOverdueLoan() {
    //     when(loans.findOverdue(TODAY)).thenReturn(List.of(DUNE, EMMA));
    //
    //     int sent = overdueNotifier.notifyOverdue();
    //
    //     assertThat(sent).isEqualTo(2);
    //     verify(notifier, times(2)).send(any(Notice.class));
    // }
    //
    // @Test
    // @DisplayName("should send nothing when no loan is overdue")
    // void shouldSendNothingWhenNoLoanIsOverdue() {
    //     when(loans.findOverdue(TODAY)).thenReturn(List.of());
    //
    //     overdueNotifier.notifyOverdue();
    //
    //     verify(notifier, never()).send(any());
    //     verify(loans, never()).markNotified(any(), any());
    // }

    // ------------------------------------------------------------------
    // PASO 2: verify con los argumentos exactos
    // ------------------------------------------------------------------

    // @Test
    // @DisplayName("should mark each loan as notified today")
    // void shouldMarkEachLoanAsNotifiedToday() {
    //     when(loans.findOverdue(TODAY)).thenReturn(List.of(DUNE, EMMA));
    //
    //     overdueNotifier.notifyOverdue();
    //
    //     verify(loans).markNotified("L-1", TODAY);
    //     verify(loans).markNotified("L-2", TODAY);
    // }

    // ------------------------------------------------------------------
    // PASO 3: capturar el aviso con ArgumentCaptor
    // ------------------------------------------------------------------

    // @Test
    // @DisplayName("should write the notice with the days late")
    // void shouldWriteTheNoticeWithTheDaysLate() {
    //     when(loans.findOverdue(TODAY)).thenReturn(List.of(DUNE));
    //
    //     overdueNotifier.notifyOverdue();
    //
    //     verify(notifier).send(notice.capture());
    //     assertThat(notice.getValue())
    //         .isEqualTo(new Notice("ana@example.com", "Overdue: Dune", "\"Dune\" is 3 day(s) late."));
    // }
    //
    // @Test
    // @DisplayName("should address each notice to the member of the loan")
    // void shouldAddressEachNoticeToTheMemberOfTheLoan() {
    //     when(loans.findOverdue(TODAY)).thenReturn(List.of(DUNE, EMMA));
    //
    //     overdueNotifier.notifyOverdue();
    //
    //     verify(notifier, times(2)).send(notice.capture());
    //     assertThat(notice.getAllValues())
    //         .extracting(Notice::to)
    //         .containsExactly("ana@example.com", "luis@example.com");
    // }

    // ------------------------------------------------------------------
    // PASO 4: el orden de las llamadas
    // ------------------------------------------------------------------

    // @Test
    // @DisplayName("should mark the loan only after sending the notice")
    // void shouldMarkTheLoanOnlyAfterSendingTheNotice() {
    //     when(loans.findOverdue(TODAY)).thenReturn(List.of(DUNE));
    //
    //     overdueNotifier.notifyOverdue();
    //
    //     InOrder inOrder = inOrder(notifier, loans);
    //     inOrder.verify(notifier).send(any());
    //     inOrder.verify(loans).markNotified("L-1", TODAY);
    //     verifyNoMoreInteractions(notifier);
    // }
}
