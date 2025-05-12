package pwydmuch.model;

import javax.swing.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;

import static pwydmuch.view.ImageLoader.*;

public class MyButton extends JButton implements Observer, Serializable, Observable {
    public enum State {
        EMPTY, FLAG, QUESTION_MARK
    }

    private State state = State.EMPTY;
    private int minesAroundNumber;
    private final Set<Observer> observers;

    public MyButton() {
        observers = new HashSet<>();
    }

    public void changeState() {
        state = switch (state) {
            case EMPTY -> State.FLAG;
            case FLAG -> State.QUESTION_MARK;
            case QUESTION_MARK -> State.EMPTY;
        };
    }

    public State getState() {
        return state;
    }

    public void countMinesAround(int row, int column, Draw draw, MyButton[][] gameBoard) {
        BiConsumer<Integer, Integer> countBombs = (Integer r, Integer c) -> minesAroundNumber += draw.checkIfMine(r, c) ? 1 : 0;
        browseBoard(row, column, gameBoard, countBombs);
    }

    public void addObservers(int row, int column, MyButton[][] gameBoard) {
        BiConsumer<Integer, Integer> addObservers = (Integer r, Integer c) -> addObserver(gameBoard[r][c]);
        browseBoard(row, column, gameBoard, addObservers);
    }

    private void browseBoard(int row, int column, MyButton[][] gameBoard, BiConsumer<Integer, Integer> fun) {
        for (var r = row - 1; r <= row + 1; r++) {
            for (var c = column - 1; c <= column + 1; c++) {
                if (r >= 0 && c >= 0 && c < gameBoard[0].length && r < gameBoard.length) {
                    fun.accept(r, c);
                }
            }
        }
    }

    @Override
    public void addObserver(Observer ob) {
        observers.add(ob);
    }

    @Override
    public void update() {
        if (isEnabled()) {
            if (state == State.EMPTY) {
                setEnabled(false);
                if (minesAroundNumber == 0) {
                    setIcon(null);
                    notifyObservers();
                } else {
                    setIcon(numberImages.get(minesAroundNumber));
                    setDisabledIcon(numberImages.get(minesAroundNumber));
                }
            }
        }
    }

    @Override
    public void notifyObservers() {
        observers.forEach(Observer::update);
    }
}


