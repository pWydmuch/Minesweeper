package pwydmuch.model;

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

    Board(int rows, int columns, Set<Point> minePoints) {
        checkIfCorrect(rows, columns, minePoints);
        this.gameBoard = new Field[rows][columns];
        this.minePoints = minePoints;
        this.remainingFlagsToSet = minePoints.size();
        createGameBoard();
        addButtonsFeatures();
    }

    public RightClickResponse clickRight(int row, int column) {
        var fieldClicked = gameBoard[row][column];
        switch (fieldClicked.changeState(remainingFlagsToSet - 1 >= 0)) {
            case FLAG -> --remainingFlagsToSet;
            case QUESTION_MARK -> ++remainingFlagsToSet;
        }
        if (isSuccess()) gameStatus = GameStatus.SUCCESS;
        return new RightClickResponse(gameStatus, FieldDto.fromField(fieldClicked), remainingFlagsToSet);
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
                .map(FieldDto::fromField)
                .toList();
    }

    public GameConfig getGameConfig() {
        return new GameConfig(
                gameBoard.length,
                gameBoard[0].length,
                minePoints.size()
        );
    }

    private boolean isSuccess() {
        return minePoints.stream()
                .allMatch(point -> gameBoard[point.x()][point.y()].isFlagged());
    }


    private void addButtonsFeatures() {
        getFieldsStream().forEach(b -> {
            addObservers(b);
            countMinesAround(b);
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
        browseBoardAroundButton(field, countBombs);
        field.setMinesAround(minesAroundNumber.get());
    }

    private void addObservers(Field field) {
        browseBoardAroundButton(field, (r, c) -> field.addObserver(gameBoard[r][c]));
    }

    private void browseBoardAroundButton(Field field, BiConsumer<Integer, Integer> fun) {
        var row = field.getRow();
        var column = field.getColumn();
        for (var r = row - 1; r <= row + 1; r++) {
            for (var c = column - 1; c <= column + 1; c++) {
                if (isCellWithinBoardLimits(r, c) && isNotTheSameCell(row, column, r, c)) {
                    fun.accept(r, c);
                }
            }
        }
    }

    private static boolean isNotTheSameCell(int row, int column, int r, int c) {
        return !(r == row && c == column);
    }

    private boolean isCellWithinBoardLimits(int r, int c) {
        return r >= 0 && c >= 0 && c < gameBoard[0].length && r < gameBoard.length;
    }

}
