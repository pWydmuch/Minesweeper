package pwydmuch.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pwydmuch.domain.Board;
import pwydmuch.domain.dtos.FieldState;
import pwydmuch.domain.dtos.GameStatus;
import pwydmuch.domain.dtos.Point;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class GameStateServiceTest {

    private GameStateService gameStateService;
    private Path tempDbPath;

    @BeforeEach
    void setUp() throws Exception {
        tempDbPath = Files.createTempFile("test", ".db");
        gameStateService = new GameStateService("jdbc:sqlite:" + tempDbPath.toString());
    }

    @Test
    void shouldSaveAndLoadGameState() {
        // Given
        Set<Point> minePoints = new HashSet<>();
        minePoints.add(new Point(0, 0));
        minePoints.add(new Point(1, 1));
        Board board = new Board(10, 10, minePoints);
        board.clickLeft(2, 2); // Reveal some fields
        board.clickRight(0, 0); // Flag a mine

        // When
        gameStateService.saveGameState(board, 100);
        Board loadedBoard = gameStateService.loadLastGameState();

        // Then
        assertThat(loadedBoard).isNotNull();
        assertThat(loadedBoard.getGameConfig().rows()).isEqualTo(10);
        assertThat(loadedBoard.getGameConfig().columns()).isEqualTo(10);
        assertThat(loadedBoard.getGameConfig().minesNumber()).isEqualTo(2);
        assertThat(loadedBoard.getGameState().gameStatus()).isEqualTo(GameStatus.IN_PROGRESS);
        assertThat(loadedBoard.getGameState().remainingFlagsToSet()).isEqualTo(1);
    }

    @Test
    void shouldReturnNullWhenNoGameStateExists() {
        // When
        Board loadedBoard = gameStateService.loadLastGameState();

        // Then
        assertThat(loadedBoard).isNull();
    }

    @Test
    void shouldSaveOnlyLastGameState() {
        // Given
        Set<Point> minePoints1 = new HashSet<>();
        minePoints1.add(new Point(0, 0));
        Board board1 = new Board(10, 10, minePoints1);

        Set<Point> minePoints2 = new HashSet<>();
        minePoints2.add(new Point(1, 1));
        minePoints2.add(new Point(2, 2));
        Board board2 = new Board(15, 15, minePoints2);

        // When
        gameStateService.saveGameState(board1, 100);
        gameStateService.saveGameState(board2, 200);
        Board loadedBoard = gameStateService.loadLastGameState();

        // Then
        assertThat(loadedBoard).isNotNull();
        assertThat(loadedBoard.getGameConfig().rows()).isEqualTo(15);
        assertThat(loadedBoard.getGameConfig().columns()).isEqualTo(15);
        assertThat(loadedBoard.getGameConfig().minesNumber()).isEqualTo(2);
    }

    @Test
    void shouldClearDatabaseAfterDontSave() {
        // Given
        Set<Point> minePoints = new HashSet<>();
        minePoints.add(new Point(0, 0));
        Board board = new Board(10, 10, minePoints);
        gameStateService.saveGameState(board, 100);

        // When
        gameStateService.clearSavedState();
        Board loadedBoard = gameStateService.loadLastGameState();

        // Then
        assertThat(loadedBoard).isNull();
    }
} 