package pwydmuch.model;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BoardTest {

    @Test
    void shouldThrowExceptionIfMinePointsSizeLowerThanBoardSize() {
        var board = new Board(new GameConfig(5, 5, 5));
        var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
        });
        Assertions.assertEquals("Mine points size must be equal to board size", exception.getMessage());
    }

}