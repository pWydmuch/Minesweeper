package pwydmuch.view;

import pwydmuch.model.Board;
import pwydmuch.model.MyButton;
import pwydmuch.model.Observer;

import javax.swing.*;

import static pwydmuch.view.ImageLoader.NUMBERS_ICONS;

public class MyButtonAdapter extends JButton implements Observer {
    private final MyButton myButton;

    public MyButtonAdapter(MyButton myButton) {
        this.myButton = myButton;
    }

    public static MyButtonAdapter[][] translate(Board board) {
        MyButtonAdapter[][] boardAdapters = getMyButtonAdapters(board);
        board.getButtonStream().forEach(b -> {
            boardAdapters[b.getRow()][b.getColumn()] = new MyButtonAdapter(b);
            b.addObserver(boardAdapters[b.getRow()][b.getColumn()]);
        });
        return boardAdapters;
    }


    public MyButton getUnderlying() {
        return myButton;
    }

    @Override
    public void update() {
        if (isEnabled()) {
            setEnabled(false);
            myButton.update();
            if (myButton.getState() == MyButton.State.REVEALED) {
                int minesAroundNumber = myButton.getMinesAroundNumber();
                if (minesAroundNumber == 0) {
                    setIcon(null);
                } else {
                    setIcon(NUMBERS_ICONS.get(minesAroundNumber));
                    setDisabledIcon(NUMBERS_ICONS.get(minesAroundNumber));
                }
            }
        }
    }

    private static MyButtonAdapter[][] getMyButtonAdapters(Board board) {
        int rows = board.getGameConfig().rows();
        int columns = board.getGameConfig().columns();
        return new MyButtonAdapter[rows][columns];
    }
}
