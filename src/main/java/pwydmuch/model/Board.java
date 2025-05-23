package pwydmuch.model;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class Board {
    private final MyButton[][] gameBoard;

    private final GameConfig gameConfig;

    private final Set<Point> minePoints;

    private int remainingFlagsToSet;

    private GameStatus gameStatus = GameStatus.IN_PROGRESS;

    public Board(int rows, int columns, int minesNumber) {
        this(new GameConfig(rows, columns, minesNumber));
    }

    public Board(GameConfig gameConfig) {
        this(gameConfig, MinePointsGenerator.makeDraw(gameConfig));
    }

    public Board onceAgain() {
        return new Board(gameConfig, minePoints);
    }

    private Board(GameConfig gameConfig, Set<Point> minePoints) {
        this.gameBoard = new MyButton[gameConfig.rows()][gameConfig.columns()];
        this.gameConfig = gameConfig;
        this.minePoints = minePoints;
        this.remainingFlagsToSet = gameConfig.minesNumber();
        createGameBoard();
        addButtonsFeatures();
    }

    public RightClickResponse clickRight(MyButton underlying) {
        switch (underlying.changeState(remainingFlagsToSet - 1 >= 0)) {
            case FLAG -> --remainingFlagsToSet;
            case QUESTION_MARK -> ++remainingFlagsToSet;
        }
        if (isSuccess()) gameStatus = GameStatus.SUCCESS;
        return new RightClickResponse(gameStatus, underlying.getState(), remainingFlagsToSet);
    }

    public LeftClickResponse clickLeft(MyButton underlying) {
        if (underlying.containMine()) gameStatus = GameStatus.GAME_OVER;
        if (isSuccess()) gameStatus = GameStatus.SUCCESS;
        underlying.update();
        return new LeftClickResponse(gameStatus, minePoints);
    }

    public boolean isSuccess() {
        var flaggedButtonsWithMines = getButtonStream()
                .filter(b -> b.isFlagged() && b.containMine())
                .count();
        return flaggedButtonsWithMines == gameConfig.minesNumber() && allButtonsWithoutMinesRevealed();
    }

    public Stream<MyButton> getButtonStream() {
        return Arrays.stream(gameBoard).flatMap(Arrays::stream);
    }

    private void addButtonsFeatures() {
        getButtonStream().forEach(b -> {
            addObservers(b);
            countMinesAround(b);
        });
    }

    private boolean allButtonsWithoutMinesRevealed() {
        return getButtonStream()
                .filter(b -> !b.containMine())
                .allMatch(b -> b.getState() == MyButton.State.REVEALED);
    }

    private void createGameBoard() {
        for (var i = 0; i < gameConfig.rows(); i++) {
            for (var j = 0; j < gameConfig.columns(); j++) {
                gameBoard[i][j] = new MyButton(i, j, minePoints.contains(new Point(i, j)));
            }
        }
    }

    private void countMinesAround(MyButton b) {
        var minesAroundNumber = new AtomicInteger(0);
        BiConsumer<Integer, Integer> countBombs = (r, c) ->
                minesAroundNumber.addAndGet(b.containMine() ? 1 : 0);
        browseBoardAroundButton(b, countBombs);
        b.setMinesAround(minesAroundNumber.get());
    }

    private void addObservers(MyButton b) {
        browseBoardAroundButton(b, (r, c) -> b.addObserver(gameBoard[r][c]));
    }

    private void browseBoardAroundButton(MyButton b, BiConsumer<Integer, Integer> fun) {
        var row = b.getRow();
        var column = b.getColumn();
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

    public GameConfig getGameConfig() {
        return gameConfig;
    }

}
