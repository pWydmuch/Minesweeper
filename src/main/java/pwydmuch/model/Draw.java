package pwydmuch.model;


import javax.swing.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Draw implements Serializable {
    private static final Random GENERATOR = new Random();
    private final int minesNumber;
    private final Set<Point> minePoints;

    public Draw(int minesNumber, int rows, int columns) {
        this.minePoints = HashSet.newHashSet(minesNumber);
        this.minesNumber = minesNumber;
        makeDraw(rows, columns, minesNumber);
    }

    public Set<Point> getMinePoints() {
        return minePoints;
    }

    public int getMinesNumber() {
        return minesNumber;
    }

    public boolean isSuccess(MyButton[][] jb, ImageIcon flag) {
        var flagsNumbers = 0;
        for (var i = 0; i < jb.length; i++) {
            for (var j = 0; j < jb[i].length; j++) {
                if (jb[i][j].getIcon() == flag && checkIfMine(i, j))
                    flagsNumbers++;
            }
        }
        return flagsNumbers == minesNumber && checkOthersDisabled(jb);
    }

    private void makeDraw(int rows, int columns, int minesNumber) {
        while (minePoints.size() != minesNumber) {
            var xMineCoordinate = GENERATOR.nextInt(rows);
            var yMineCoordinate = GENERATOR.nextInt(columns);
            var point = new Point(xMineCoordinate, yMineCoordinate);
            minePoints.add(point);
        }
    }

    private boolean checkOthersDisabled(MyButton[][] jb) {
        for (var i = 0; i < jb.length; i++) {
            for (var j = 0; j < jb[i].length; j++) {
                if (!checkIfMine(i, j) && jb[i][j].isEnabled()) {
                    return false;
                }
            }
        }
        return true;
    }

    boolean checkIfMine(int x, int y) {
        return minePoints.contains(new Point(x, y));
    }
}

