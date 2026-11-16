package com.bootcamp;

// PASO 3: descomenta el test entero (imports incluidos).
//
// import static org.assertj.core.api.Assertions.assertThat;
//
// import java.time.Clock;
// import java.time.LocalDate;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
//
// @ExtendWith(FixedClockExtension.class)
// class ReturnReminderTest {
//
//     @Test
//     @DisplayName("should say due today on the due date")
//     void shouldSayDueTodayOnTheDueDate(Clock clock) {
//         LoanRecord loan = new LoanRecord("9780000000001", LocalDate.of(2026, 3, 16));
//
//         assertThat(new ReturnReminder(clock).messageFor(loan)).isEqualTo("due today");
//     }
//
//     @Test
//     @DisplayName("should say due tomorrow the day before")
//     void shouldSayDueTomorrowTheDayBefore(Clock clock) {
//         LoanRecord loan = new LoanRecord("9780000000001", LocalDate.of(2026, 3, 17));
//
//         assertThat(new ReturnReminder(clock).messageFor(loan)).isEqualTo("due tomorrow");
//     }
//
//     @Test
//     @DisplayName("should count the overdue days")
//     void shouldCountTheOverdueDays(Clock clock) {
//         LoanRecord loan = new LoanRecord("9780000000001", LocalDate.of(2026, 3, 13));
//
//         assertThat(new ReturnReminder(clock).messageFor(loan)).isEqualTo("overdue by 3 day(s)");
//     }
// }
