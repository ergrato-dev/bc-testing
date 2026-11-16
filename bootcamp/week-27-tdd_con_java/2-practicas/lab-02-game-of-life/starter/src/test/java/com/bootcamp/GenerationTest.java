package com.bootcamp;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import org.junit.jupiter.api.Test;

class GenerationTest {

    // PASO 7: un tablero vacío sigue vacío
    // @Test
    // void shouldStayEmptyWhenNoCellIsAlive() {
    //     assertThat(new Generation(Set.of()).next().aliveCells()).isEmpty();
    // }

    // PASO 8: una célula sola muere
    // @Test
    // void shouldKillALonelyCell() {
    //     Generation lonely = new Generation(Set.of(new Position(0, 0)));

    //     assertThat(lonely.next().aliveCells()).isEmpty();
    // }

    // PASO 9: un bloque no cambia
    // @Test
    // void shouldKeepABlockUnchanged() {
    //     Set<Position> block = Set.of(
    //         new Position(0, 0), new Position(0, 1),
    //         new Position(1, 0), new Position(1, 1));

    //     assertThat(new Generation(block).next().aliveCells()).isEqualTo(block);
    // }

    // PASO 10: el parpadeador gira
    // @Test
    // void shouldTurnAHorizontalBlinkerIntoAVerticalOne() {
    //     Generation horizontal = new Generation(Set.of(
    //         new Position(1, 0), new Position(1, 1), new Position(1, 2)));

    //     assertThat(horizontal.next().aliveCells()).containsExactlyInAnyOrder(
    //         new Position(0, 1), new Position(1, 1), new Position(2, 1));
    // }

    // PASO 11: el parpadeador vuelve tras dos generaciones
    // @Test
    // void shouldBringTheBlinkerBackAfterTwoGenerations() {
    //     Set<Position> horizontal = Set.of(
    //         new Position(1, 0), new Position(1, 1), new Position(1, 2));

    //     assertThat(new Generation(horizontal).next().next().aliveCells()).isEqualTo(horizontal);
    // }
}
