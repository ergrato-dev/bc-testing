package com.bootcamp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReminderServiceTest {

    // PASO 1: las dependencias del servicio, el reloj y el primer test
    // // Hoy es 16 de marzo de 2026 para el servicio: los préstamos de "mañana" vencen el 17
    // private static final Clock MARCH_16 = Clock.fixed(
    //     LocalDate.of(2026, 3, 16).atStartOfDay().toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
    // private static final LocalDate MARCH_17 = LocalDate.of(2026, 3, 17);

    // private static final Loan ANA_LOAN = new Loan("L-1", "M-7", "Rayuela", MARCH_17);
    // private static final Loan LUIS_LOAN = new Loan("L-2", "M-8", "Ficciones", MARCH_17);

    // @Mock
    // private LoanRepository loans;

    // @Mock
    // private MemberDirectory members;

    // @Mock
    // private Mailer mailer;

    // @Captor
    // private ArgumentCaptor<Email> email;

    // private ReminderService service;

    // @BeforeEach
    // void setUp() {
    //     service = new ReminderService(loans, members, mailer, MARCH_16);
    // }

    // @Test
    // void shouldEmailTheMemberOfEachLoanDueTomorrow() {
    //     when(loans.findDueOn(MARCH_17)).thenReturn(List.of(ANA_LOAN));
    //     when(members.emailOf("M-7")).thenReturn(Optional.of("ana@example.com"));

    //     service.remindLoansDueTomorrow();

    //     verify(mailer).send(email.capture());
    //     assertThat(email.getValue().to()).isEqualTo("ana@example.com");
    // }

    // PASO 2: un socio sin correo no recibe nada
    // @Test
    // void shouldSkipMembersWithoutAnEmailAddress() {
    //     when(loans.findDueOn(MARCH_17)).thenReturn(List.of(ANA_LOAN));
    //     when(members.emailOf("M-7")).thenReturn(Optional.empty());

    //     service.remindLoansDueTomorrow();

    //     verify(mailer, never()).send(any());
    // }

    // PASO 5: el correo lleva los textos del recordatorio
    // @Test
    // void shouldUseTheReminderTexts() {
    //     when(loans.findDueOn(MARCH_17)).thenReturn(List.of(ANA_LOAN));
    //     when(members.emailOf("M-7")).thenReturn(Optional.of("ana@example.com"));

    //     service.remindLoansDueTomorrow();

    //     verify(mailer).send(email.capture());
    //     assertThat(email.getValue()).isEqualTo(new Email(
    //         "ana@example.com", ReminderText.subjectFor(ANA_LOAN), ReminderText.bodyFor(ANA_LOAN)));
    // }

    // PASO 6: un correo que falla no detiene los demás
    // @Test
    // void shouldKeepSendingWhenOneEmailFails() {
    //     when(loans.findDueOn(MARCH_17)).thenReturn(List.of(ANA_LOAN, LUIS_LOAN));
    //     when(members.emailOf("M-7")).thenReturn(Optional.of("ana@example.com"));
    //     when(members.emailOf("M-8")).thenReturn(Optional.of("luis@example.com"));
    //     doThrow(new MailerUnavailableException("timeout"))
    //         .when(mailer).send(new Email("ana@example.com", ReminderText.subjectFor(ANA_LOAN), ReminderText.bodyFor(ANA_LOAN)));

    //     service.remindLoansDueTomorrow();

    //     verify(mailer, times(2)).send(email.capture());
    //     assertThat(email.getAllValues()).extracting(Email::to)
    //         .containsExactly("ana@example.com", "luis@example.com");
    // }
}
