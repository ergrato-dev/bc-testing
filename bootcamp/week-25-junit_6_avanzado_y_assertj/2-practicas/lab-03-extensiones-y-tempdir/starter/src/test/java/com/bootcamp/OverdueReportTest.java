package com.bootcamp;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
// PASO 1: descomenta este import.
// import org.junit.jupiter.api.io.TempDir;

class OverdueReportTest {

    private static final Clock MARCH_16 = Clock.fixed(Instant.parse("2026-03-16T00:00:00Z"), ZoneOffset.UTC);

    private static final List<LoanRecord> LOANS = List.of(
        new LoanRecord("9780000000001", LocalDate.of(2026, 3, 10)),
        new LoanRecord("9780000000002", LocalDate.of(2026, 3, 16)),
        new LoanRecord("9780000000003", LocalDate.of(2026, 3, 15)));

    private final OverdueReport report = new OverdueReport(MARCH_16);

    // PASO 2: un directorio temporal por test, inyectado en un campo.
    // @TempDir
    // Path reportsDir;

    // ------------------------------------------------------------------
    // PASO 0: escribe en la carpeta desde la que ejecutas Maven y deja el
    // archivo ahí. Ejecútalo, busca el archivo y borra el test en el PASO 1.
    // ------------------------------------------------------------------

    @Test
    @DisplayName("should write only the overdue loans in the working directory")
    void shouldWriteOnlyTheOverdueLoansInTheWorkingDirectory() throws IOException {
        Path file = Path.of("overdue.csv");

        report.writeTo(file, LOANS);

        assertThat(Files.readAllLines(file))
            .containsExactly("9780000000001,2026-03-10", "9780000000003,2026-03-15");
    }

    // ------------------------------------------------------------------
    // PASO 1: el mismo test con un directorio temporal como parámetro.
    // ------------------------------------------------------------------

    // @Test
    // @DisplayName("should write only the overdue loans")
    // void shouldWriteOnlyTheOverdueLoans(@TempDir Path dir) throws IOException {
    //     Path file = dir.resolve("overdue.csv");
    //
    //     report.writeTo(file, LOANS);
    //
    //     assertThat(Files.readAllLines(file))
    //         .containsExactly("9780000000001,2026-03-10", "9780000000003,2026-03-15");
    // }

    // ------------------------------------------------------------------
    // PASO 2: usa el campo reportsDir.
    // ------------------------------------------------------------------

    // @Test
    // @DisplayName("should create the missing parent directories")
    // void shouldCreateTheMissingParentDirectories() throws IOException {
    //     Path file = reportsDir.resolve("2026/03/overdue.csv");
    //
    //     report.writeTo(file, LOANS);
    //
    //     assertThat(file).exists();
    // }
}
