package com.bootcamp;

import static org.assertj.core.api.Assertions.assertThat;
// PASO 2: descomenta este import.
// import static org.assertj.core.api.Assertions.assertThatThrownBy;
// PASO 4: descomenta este import.
// import static org.junit.jupiter.params.provider.Arguments.argumentSet;

// PASO 4: descomenta este import.
// import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
// PASO 0: borra este import junto con los cuatro tests repetidos.
import org.junit.jupiter.api.Test;
// PASO 1: descomenta este import.
// import org.junit.jupiter.params.ParameterizedTest;
// PASO 4: descomenta este import.
// import org.junit.jupiter.params.provider.Arguments;
// PASO 1: descomenta este import.
// import org.junit.jupiter.params.provider.CsvSource;
// PASO 3: descomenta este import.
// import org.junit.jupiter.params.provider.EnumSource;
// PASO 4: descomenta este import.
// import org.junit.jupiter.params.provider.MethodSource;
// PASO 2: descomenta este import.
// import org.junit.jupiter.params.provider.ValueSource;

class LateFeeCalculatorTest {

    private final LateFeeCalculator calculator = new LateFeeCalculator();

    // ------------------------------------------------------------------
    // PASO 0: cuatro tests que solo cambian en los datos. Léelos, ejecútalos
    // y bórralos cuando llegues al PASO 1.
    // ------------------------------------------------------------------

    @Test
    @DisplayName("should not charge a standard member returning on time")
    void shouldNotChargeAStandardMemberReturningOnTime() {
        assertThat(calculator.feeFor(0, Membership.STANDARD)).isEqualTo(0);
    }

    @Test
    @DisplayName("should not charge a standard member on the last grace day")
    void shouldNotChargeAStandardMemberOnTheLastGraceDay() {
        assertThat(calculator.feeFor(2, Membership.STANDARD)).isEqualTo(0);
    }

    @Test
    @DisplayName("should charge a standard member the first day after grace")
    void shouldChargeAStandardMemberTheFirstDayAfterGrace() {
        assertThat(calculator.feeFor(3, Membership.STANDARD)).isEqualTo(500);
    }

    @Test
    @DisplayName("should cap the fee of a standard member")
    void shouldCapTheFeeOfAStandardMember() {
        assertThat(calculator.feeFor(30, Membership.STANDARD)).isEqualTo(10000);
    }

    // ------------------------------------------------------------------
    // PASO 1: un solo test con una fila por caso.
    // ------------------------------------------------------------------

    // @ParameterizedTest(name = "[{index}] {0} days late, fee {1}", quoteTextArguments = false)
    // @CsvSource({
    //     "0,  0",
    //     "2,  0",
    //     "3,  500",
    //     "10, 4000",
    //     "22, 10000",
    //     "30, 10000"
    // })
    // @DisplayName("should charge standard members by days late")
    // void shouldChargeStandardMembersByDaysLate(int daysLate, int expectedFee) {
    //     assertThat(calculator.feeFor(daysLate, Membership.STANDARD)).isEqualTo(expectedFee);
    // }

    // ------------------------------------------------------------------
    // PASO 2: un solo argumento por caso con @ValueSource.
    // ------------------------------------------------------------------

    // @ParameterizedTest
    // @ValueSource(ints = {-1, -30})
    // @DisplayName("should reject negative days late")
    // void shouldRejectNegativeDaysLate(int daysLate) {
    //     assertThatThrownBy(() -> calculator.feeFor(daysLate, Membership.STANDARD))
    //         .isInstanceOf(IllegalArgumentException.class)
    //         .hasMessageContaining("must not be negative");
    // }

    // ------------------------------------------------------------------
    // PASO 3: una ejecución por cada valor del enum.
    // ------------------------------------------------------------------

    // @ParameterizedTest
    // @EnumSource(Membership.class)
    // @DisplayName("should not charge any membership during the grace period")
    // void shouldNotChargeAnyMembershipDuringTheGracePeriod(Membership membership) {
    //     assertThat(calculator.feeFor(LateFeeCalculator.GRACE_DAYS, membership)).isZero();
    // }

    // ------------------------------------------------------------------
    // PASO 4: casos con nombre, construidos en un método.
    // ------------------------------------------------------------------

    // @ParameterizedTest
    // @MethodSource("discountedCases")
    // @DisplayName("should charge half to discounted memberships")
    // void shouldChargeHalfToDiscountedMemberships(int daysLate, Membership membership, int expectedFee) {
    //     assertThat(calculator.feeFor(daysLate, membership)).isEqualTo(expectedFee);
    // }

    // static Stream<Arguments> discountedCases() {
    //     return Stream.of(
    //         argumentSet("student, first day after grace", 3, Membership.STUDENT, 250),
    //         argumentSet("senior, ten days late", 10, Membership.SENIOR, 2000),
    //         argumentSet("student, capped fee", 40, Membership.STUDENT, 5000)
    //     );
    // }
}
