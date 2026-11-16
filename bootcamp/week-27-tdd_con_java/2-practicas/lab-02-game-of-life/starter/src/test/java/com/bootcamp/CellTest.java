package com.bootcamp;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CellTest {

    // PASO 1: una célula viva con menos de dos vecinas muere
    // @ParameterizedTest
    // @ValueSource(ints = {0, 1})
    // void shouldDieWithFewerThanTwoLiveNeighbours(int liveNeighbours) {
    //     assertThat(Cell.ALIVE.next(liveNeighbours)).isEqualTo(Cell.DEAD);
    // }

    // PASO 2: con dos o tres vecinas sobrevive
    // @ParameterizedTest
    // @ValueSource(ints = {2, 3})
    // void shouldSurviveWithTwoOrThreeLiveNeighbours(int liveNeighbours) {
    //     assertThat(Cell.ALIVE.next(liveNeighbours)).isEqualTo(Cell.ALIVE);
    // }

    // PASO 3: con más de tres vecinas muere
    // @ParameterizedTest
    // @ValueSource(ints = {4, 5, 6, 7, 8})
    // void shouldDieWithMoreThanThreeLiveNeighbours(int liveNeighbours) {
    //     assertThat(Cell.ALIVE.next(liveNeighbours)).isEqualTo(Cell.DEAD);
    // }

    // PASO 4: una célula muerta con tres vecinas nace
    // @Test
    // void shouldComeAliveWithExactlyThreeLiveNeighbours() {
    //     assertThat(Cell.DEAD.next(3)).isEqualTo(Cell.ALIVE);
    // }

    // PASO 5: con cualquier otro número sigue muerta
    // @ParameterizedTest
    // @ValueSource(ints = {0, 1, 2, 4, 5, 6, 7, 8})
    // void shouldStayDeadWithAnyOtherNumberOfLiveNeighbours(int liveNeighbours) {
    //     assertThat(Cell.DEAD.next(liveNeighbours)).isEqualTo(Cell.DEAD);
    // }
}
