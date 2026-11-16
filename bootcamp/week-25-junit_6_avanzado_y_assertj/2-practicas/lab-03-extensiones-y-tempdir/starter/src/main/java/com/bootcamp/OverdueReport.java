package com.bootcamp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

/** Escribe en un CSV los préstamos vencidos a fecha de hoy. */
public final class OverdueReport {

    private final Clock clock;

    public OverdueReport(Clock clock) {
        this.clock = clock;
    }

    public void writeTo(Path file, List<LoanRecord> loans) throws IOException {
        LocalDate today = LocalDate.now(clock);
        List<String> lines = loans.stream()
            .filter(loan -> loan.dueDate().isBefore(today))
            .map(loan -> loan.isbn() + "," + loan.dueDate())
            .toList();
        Path parent = file.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.write(file, lines);
    }
}
