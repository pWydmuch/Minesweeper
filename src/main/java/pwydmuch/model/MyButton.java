package pwydmuch.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class MyButton implements Observer, Serializable, Observable {

    public int getMinesAroundNumber() {
        return minesAroundNumber;
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

    public MyButton(int row, int column, boolean containMine) {
        this.row = row;
        this.column = column;
        this.containMine = containMine;
        this.observers = new HashSet<>();
    }

    //TODO -> state pattern for button?? It doesn't look good right now
    public State changeState(boolean predicate) {
        return switch (state) {
            case NOT_MARKED -> {
                if (predicate) {
                    isFlagged = true;
                    state = State.FLAG;
                }
                yield state;
            }
            case FLAG -> {
                isFlagged = false;
                state = State.QUESTION_MARK;
                yield state;
            }
            case QUESTION_MARK -> {
                state = State.NOT_MARKED;
                yield state;
            }
            case REVEALED -> state;
        };
    }

    //TODO -> state pattern for button??
    @Override
    public void update() {
        if (state == State.NOT_MARKED && !containMine) {
            state = State.REVEALED;
            if (minesAroundNumber == 0) {
                notifyObservers();
            }
        }
    }

    public void setMinesAround(int i) {
        minesAroundNumber = i;
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

    public boolean containMine() {
        return containMine;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    @Override
    public void addObserver(Observer ob) {
        observers.add(ob);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(Observer::update);
    }


}