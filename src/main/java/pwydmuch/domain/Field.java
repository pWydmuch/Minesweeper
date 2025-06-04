package pwydmuch.domain;

import pwydmuch.domain.dtos.FieldDto;
import pwydmuch.domain.dtos.FieldState;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

class Field implements Serializable {

    private FieldState state = FieldState.NOT_MARKED;

    private int minesAroundNumber;

    private final boolean containMine;
    private final Set<Field> observers;

    private final int row;
    private final int column;

    private boolean isFlagged;

    Field(int row, int column, boolean containMine) {
        this.row = row;
        this.column = column;
        this.containMine = containMine;
        this.observers = new HashSet<>();
    }

    //TODO -> state pattern for button?? It doesn't look good right now
    FieldState changeState(boolean predicate) {
        return switch (state) {
            case NOT_MARKED -> {
                if (predicate) {
                    isFlagged = true;
                    state = FieldState.FLAG;
                }
                yield state;
            }
            case FLAG -> {
                isFlagged = false;
                state = FieldState.QUESTION_MARK;
                yield state;
            }
            case QUESTION_MARK -> {
                state = FieldState.NOT_MARKED;
                yield state;
            }
            case REVEALED -> state;
        };
    }

    //TODO -> state pattern for button??
    void update() {
        if (state == FieldState.NOT_MARKED && !containMine) {
            state = FieldState.REVEALED;
            if (minesAroundNumber == 0) {
                notifyObservers();
            }
        }
    }

    void setMinesAround(int i) {
        minesAroundNumber = i;
    }

    int getRow() {
        return row;
    }

    int getColumn() {
        return column;
    }

    FieldDto toFieldDto() {
        return new FieldDto(row, column, getMinesAround(), state);
    }
    private Integer getMinesAround() {
        return state == FieldState.REVEALED ? minesAroundNumber : null;
    }

    boolean containMine() {
        return containMine;
    }

    boolean isFlagged() {
        return isFlagged;
    }

    void addObserver(Field observer) {
        observers.add(observer);
    }

    private void notifyObservers() {
        observers.forEach(Field::update);
    }
}