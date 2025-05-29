package pwydmuch.model;


import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class BoardTest {

    @Test
    void shouldChangeToSuccessWhenAllMinePointsFlagged() {
        var minePoints = Set.of(new Point(2, 2));
        var board = new Board(5, 5, minePoints);
        var response = board.clickRight(2, 2);
        assertThat(response.field().fieldState()).isEqualTo(FieldState.FLAG);
        assertThat(response.gameStatus()).isEqualTo(GameStatus.SUCCESS);
    }

}