package pwydmuch.model;


import javax.swing.*;
import java.io.Serializable;
import java.util.Random;

public class Draw implements Serializable {
    private final Random generator;
    private boolean isRepeat;
    private final int minesNumber;
    private final int[] x;
    private final int[] y;

    public Draw(int minesNumber, int rows, int columns) {
        this.minesNumber = minesNumber;
        generator = new Random();
        isRepeat = false;
        x = new int[minesNumber];
        y = new int[minesNumber];
        makeDraw(rows, columns);
    }

    public int getMinesNumber() {
        return minesNumber;
    }

    public int getX(int i) {
        return x[i];
    }

    public int getY(int i) {
        return y[i];
    }

    public boolean isSuccess(MyButton[][] jb, ImageIcon flag) {
        int flagsNumbers = 0;
        for (int i = 0; i < jb.length; i++) {
            for (int j = 0; j < jb[i].length; j++) {
                if (jb[i][j].getIcon() == flag && checkIfBomb(i, j))
                    flagsNumbers++;
            }
        }
        return flagsNumbers == minesNumber && checkOthersDisabled(jb);
    }

    private void check(int i, int number1, int number2) {
        if (i != 0) {
            for (int j = 0; j < i; j++) {
                if (x[j] == number1 && y[j] == number2) {
                    isRepeat = true;
                    return;
                }
            }
        }
    }

    private void makeDraw(int rows, int columns) {
        for (int i = 0; i < minesNumber; i++) {
            do {
                x[i] = generator.nextInt(rows);
                y[i] = generator.nextInt(columns);
                check(i, x[i], y[i]);
                if (!isRepeat) break;
                isRepeat = false;
            } while (true);
        }
    }

    private boolean checkIfBomb(int a, int b) {
        for (int i = 0; i < minesNumber; i++) {
            if (x[i] == a && y[i] == b) return true;
        }
        return false;
    }

    private boolean checkOthersDisabled(MyButton[][] jb) {
        for (int i = 0; i < jb.length; i++) {
            for (int j = 0; j < jb[i].length; j++) {
                if (!checkIfBomb(i, j) && jb[i][j].isEnabled()) {
                    return false;
                }
            }
        }
        return true;
    }
}

