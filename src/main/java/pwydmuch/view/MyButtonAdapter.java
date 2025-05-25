package pwydmuch.view;

import pwydmuch.model.Board;
import pwydmuch.model.MyButton;
import pwydmuch.model.Observer;

import javax.swing.*;

import java.awt.*;

import static pwydmuch.view.ImageLoader.*;

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
            boardAdapters[b.getRow()][b.getColumn()].flagButton();
        });
        return boardAdapters;
    }

    public void flagButton() {
        setIcon(switch (myButton.getState()) {
            case FLAG -> FLAG_ICON;
            case QUESTION_MARK -> QUESTION_MARK_ICON;
            case REVEALED -> showMinesAroundNr();
            case NOT_MARKED -> null;
        });
    }

    private void reveal() {
        setEnabled(false);
        showMinesAroundNr();
    }

    private ImageIcon showMinesAroundNr() {
        int minesAroundNumber = myButton.getMinesAroundNumber();
        return switch (minesAroundNumber) {
            case 0 -> null;
            default -> {
                setDisabledIcon(NUMBERS_ICONS.get(minesAroundNumber));
                yield NUMBERS_ICONS.get(minesAroundNumber);
            }
        };
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
