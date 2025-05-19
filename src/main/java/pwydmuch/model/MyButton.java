package pwydmuch.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class MyButton implements Observer, Serializable, Observable {

    public MyButton(int row, int column, boolean containMine) {
        this.row = row;
        this.column = column;
        this.containMine = containMine;
        observers = new HashSet<>();
    }

    public boolean containMine() {
        return containMine;
    }

    public enum State {
        NOT_MARKED, FLAG, QUESTION_MARK, REVEALED
    }

    private State state = State.NOT_MARKED;
    private int minesAroundNumber;

    private final boolean containMine;
    private final Set<Observer> observers;

    private final int row;
    private final int column;

    private boolean isFlagged;

    public void setMinesAround(int i) {
        minesAroundNumber = i;
    }

    public void changeState() {
        state = switch (state) {
            case NOT_MARKED -> State.FLAG;
            case FLAG -> State.QUESTION_MARK;
            case QUESTION_MARK -> State.NOT_MARKED;
            case REVEALED -> throw new RuntimeException();
        };
    }


    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public State getState() {
        return state;
    }

    public int getMinesAroundNumber() {
        return minesAroundNumber;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }

    @Override
    public void addObserver(Observer ob) {
        observers.add(ob);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(Observer::update);
    }

    @Override
    public void update() {
        if (state == State.NOT_MARKED) {
            state = State.REVEALED;
            if (minesAroundNumber == 0) {
                notifyObservers();
            }
        }
    }

}


