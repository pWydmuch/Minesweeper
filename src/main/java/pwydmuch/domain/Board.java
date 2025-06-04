package pwydmuch.domain;

import pwydmuch.domain.dtos.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class Board {
    private final Field[][] gameBoard;

    private final Set<Point> minePoints;

    private int remainingFlagsToSet;

    private GameStatus gameStatus = GameStatus.IN_PROGRESS;

    public Board(int rows, int columns, int minesNumber) {
        this(new GameConfig(rows, columns, minesNumber));
    }

    public Board(GameConfig gameConfig) {
        this(gameConfig.rows(), gameConfig.columns(), MinePointsGenerator.makeDraw(gameConfig));
    }

    public Board onceAgain() {
        return new Board(gameBoard.length, gameBoard[0].length, minePoints);
    }

    public Board(int rows, int columns, Set<Point> minePoints) {
        checkIfCorrect(rows, columns, minePoints);
        this.gameBoard = new Field[rows][columns];
        this.minePoints = minePoints;
        this.remainingFlagsToSet = minePoints.size();
        createGameBoard();
        addFieldsFeatures();
    }

    public RightClickResponse clickRight(int row, int column) {
        var fieldClicked = gameBoard[row][column];
        switch (fieldClicked.changeState(remainingFlagsToSet - 1 >= 0)) {
            case FLAG -> --remainingFlagsToSet;
            case QUESTION_MARK -> ++remainingFlagsToSet;
        }
        if (isSuccess()) gameStatus = GameStatus.SUCCESS;
        return new RightClickResponse(gameStatus, fieldClicked.toFieldDto(), remainingFlagsToSet);
    }

    public LeftClickResponse clickLeft(int row, int column) {
        var buttonClicked = gameBoard[row][column];
        if (buttonClicked.containMine()) gameStatus = GameStatus.GAME_OVER;
        if (isSuccess()) gameStatus = GameStatus.SUCCESS;
        buttonClicked.update();
        return new LeftClickResponse(gameStatus, getFieldDtos(), minePoints);
    }

    public GameState getGameState() {
        return new GameState(
                getFieldDtos(),
                minePoints,
                gameStatus,
                remainingFlagsToSet
        );
    }

    public GameConfig getGameConfig() {
        return new GameConfig(
                gameBoard.length,
                gameBoard[0].length,
                minePoints.size()
        );
    }

    private static void checkIfCorrect(int rows, int columns, Set<Point> minePoints) {
        if (rows <= 0 || columns <= 0) {
            throw new IllegalArgumentException("Rows and columns must be greater than zero");
        }
        if (columns * rows < minePoints.size()) {
            throw new IllegalArgumentException("Too many mines for given board size");
        }
    }

    private List<FieldDto> getFieldDtos() {
        return Arrays.stream(gameBoard)
                .flatMap(Arrays::stream)
                .map(Field::toFieldDto)
                .toList();
    }

    private boolean isSuccess() {
        return minePoints.stream()
                .allMatch(point -> gameBoard[point.x()][point.y()].isFlagged());
    }


    private void addFieldsFeatures() {
        getFieldsStream().forEach(field -> {
            addObservers(field);
            countMinesAround(field);
        });
    }

    private Stream<Field> getFieldsStream() {
        return Arrays.stream(gameBoard).flatMap(Arrays::stream);
    }

    private void createGameBoard() {
        for (var i = 0; i < gameBoard.length; i++) {
            for (var j = 0; j < gameBoard[0].length; j++) {
                gameBoard[i][j] = new Field(i, j, minePoints.contains(new Point(i, j)));
            }
        }
    }

    private void countMinesAround(Field field) {
        var minesAroundNumber = new AtomicInteger(0);
        BiConsumer<Integer, Integer> countBombs = (r, c) ->
                minesAroundNumber.addAndGet(gameBoard[r][c].containMine() ? 1 : 0);
        browseBoardAroundField(field, countBombs);
        field.setMinesAround(minesAroundNumber.get());
    }

    private void addObservers(Field field) {
        browseBoardAroundField(field, (r, c) -> field.addObserver(gameBoard[r][c]));
    }

    private static final int[][] NEIGHBOR_OFFSETS = {
            {-1, -1}, {-1, 0}, {-1, 1},
            { 0, -1},          { 0, 1},
            { 1, -1}, { 1, 0}, { 1, 1}
    };

    private void browseBoardAroundField(Field field, BiConsumer<Integer, Integer> fun) {
        for (int[] offset : NEIGHBOR_OFFSETS) {
            int r = field.getRow() + offset[0];
            int c = field.getColumn() + offset[1];
            if (isCellWithinBoardLimits(r, c)) {
                fun.accept(r, c);
            }
        }
    }

    private boolean isCellWithinBoardLimits(int r, int c) {
        return r >= 0 && r < gameBoard.length && c >= 0 && c < gameBoard[0].length;
    }
}
