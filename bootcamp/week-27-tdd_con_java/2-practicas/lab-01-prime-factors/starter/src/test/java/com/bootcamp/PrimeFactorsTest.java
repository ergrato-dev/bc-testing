package com.bootcamp;

import static org.assertj.core.api.Assertions.assertThat;
// PASO 9
// import static org.assertj.core.api.Assertions.assertThatThrownBy;

// PASO 8
// import java.util.List;
import org.junit.jupiter.api.Test;
// PASO 8
// import org.junit.jupiter.params.ParameterizedTest;
// import org.junit.jupiter.params.provider.Arguments;
// import org.junit.jupiter.params.provider.MethodSource;
// PASO 9
// import org.junit.jupiter.params.provider.ValueSource;

class PrimeFactorsTest {

    // PASO 1: uno no tiene factores
    // @Test
    // void shouldReturnNoFactorsForOne() {
    //     assertThat(PrimeFactors.of(1)).isEmpty();
    // }

    // PASO 2: el primer primo
    // @Test
    // void shouldFactorTwo() {
    //     assertThat(PrimeFactors.of(2)).containsExactly(2);
    // }

    // PASO 3: otro primo
    // @Test
    // void shouldFactorThree() {
    //     assertThat(PrimeFactors.of(3)).containsExactly(3);
    // }

    // PASO 4: un factor repetido
    // @Test
    // void shouldFactorFour() {
    //     assertThat(PrimeFactors.of(4)).containsExactly(2, 2);
    // }

    // PASO 5: dos factores distintos
    // @Test
    // void shouldFactorSix() {
    //     assertThat(PrimeFactors.of(6)).containsExactly(2, 3);
    // }

    // PASO 6: tres factores repetidos
    // @Test
    // void shouldFactorEight() {
    //     assertThat(PrimeFactors.of(8)).containsExactly(2, 2, 2);
    // }

    // PASO 7: un divisor que no es 2
    // @Test
    // void shouldFactorNine() {
    //     assertThat(PrimeFactors.of(9)).containsExactly(3, 3);
    // }

    // PASO 8: números más grandes (test de confirmación)
    // static List<Arguments> biggerNumbers() {
    //     return List.of(
    //         Arguments.of(101, List.of(101)),
    //         Arguments.of(1024, List.of(2, 2, 2, 2, 2, 2, 2, 2, 2, 2)),
    //         Arguments.of(30030, List.of(2, 3, 5, 7, 11, 13)),
    //         Arguments.of(9_699_690, List.of(2, 3, 5, 7, 11, 13, 17, 19)));
    // }

    // @ParameterizedTest(name = "{0} = {1}")
    // @MethodSource("biggerNumbers")
    // void shouldFactorBiggerNumbers(int n, List<Integer> expected) {
    //     assertThat(PrimeFactors.of(n)).isEqualTo(expected);
    // }

    // PASO 9: entradas no válidas
    // @ParameterizedTest
    // @ValueSource(ints = {0, -6})
    // void shouldRejectNumbersBelowOne(int n) {
    //     assertThatThrownBy(() -> PrimeFactors.of(n))
    //         .isInstanceOf(IllegalArgumentException.class)
    //         .hasMessage("n must be 1 or greater: " + n);
    // }

}
