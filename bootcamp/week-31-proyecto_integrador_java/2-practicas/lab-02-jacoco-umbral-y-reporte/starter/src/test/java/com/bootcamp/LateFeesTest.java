package com.bootcamp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class LateFeesTest {

    @ParameterizedTest
    @CsvSource({"0, 0", "1, 50", "3, 150"})
    void chargesPerDayLate(int daysLate, int expected) {
        assertThat(LateFees.lateFee(daysLate)).isEqualTo(expected);
    }

    @Test
    void rejectsNegativeDays() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> LateFees.lateFee(-1))
                .withMessage("days late must not be negative");
    }
}
