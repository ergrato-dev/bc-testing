package com.bootcamp;

// PASO 4: descomenta el test entero (imports incluidos).
//
// import static org.assertj.core.api.Assertions.assertThat;
//
// import java.time.Clock;
// import java.time.LocalDate;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.RegisterExtension;
//
// class ReturnReminderAtYearEndTest {
//
//     @RegisterExtension
//     static final FixedClockExtension NEW_YEARS_EVE = FixedClockExtension.on(LocalDate.of(2026, 12, 31));
//
//     @Test
//     @DisplayName("should count the days across the new year")
//     void shouldCountTheDaysAcrossTheNewYear(Clock clock) {
//         LoanRecord loan = new LoanRecord("9780000000001", LocalDate.of(2027, 1, 7));
//
//         assertThat(new ReturnReminder(clock).messageFor(loan)).isEqualTo("due in 7 days");
//     }
// }
