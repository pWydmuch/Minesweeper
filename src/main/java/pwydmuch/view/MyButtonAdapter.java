package pwydmuch.view;

import pwydmuch.model.Board;
import pwydmuch.model.MyButton;
import pwydmuch.model.Observer;

import javax.swing.*;

import java.awt.*;

import static pwydmuch.view.ImageLoader.BOMB_ICON;
import static pwydmuch.view.ImageLoader.NUMBERS_ICONS;

public class MyButtonAdapter extends JButton {
    private final MyButton myButton;

    public MyButtonAdapter(MyButton myButton) {
        this.myButton = myButton;
    }

    public static MyButtonAdapter[][] translate(Board board) {
        MyButtonAdapter[][] boardAdapters = createArray(board);
        board.getButtonStream().forEach(b -> {
            boardAdapters[b.getRow()][b.getColumn()] = new MyButtonAdapter(b);
            if (b.getState().equals(MyButton.State.REVEALED)) {
                boardAdapters[b.getRow()][b.getColumn()].reveal();
            }
        });
        return boardAdapters;
    }

    private void reveal() {
        setEnabled(false);
        int minesAroundNumber = myButton.getMinesAroundNumber();
        if (minesAroundNumber == 0) {
            setIcon(null);
        } else {
            setIcon(NUMBERS_ICONS.get(minesAroundNumber));
            setDisabledIcon(NUMBERS_ICONS.get(minesAroundNumber));
        }
    }

    public MyButton getUnderlying() {
        return myButton;
    }

    private static MyButtonAdapter[][] createArray(Board board) {
        int rows = board.getGameConfig().rows();
        int columns = board.getGameConfig().columns();
        return new MyButtonAdapter[rows][columns];
    }
}
