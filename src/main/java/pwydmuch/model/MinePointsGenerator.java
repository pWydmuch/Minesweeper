package pwydmuch.model;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MinePointsGenerator implements Serializable {
    private static final Random GENERATOR = new Random();

    static Set<Point> makeDraw(GameConfig config) {
        int minesNumber = config.minesNumber();
        Set<Point> minePoints = HashSet.newHashSet(minesNumber);
        while (minePoints.size() != minesNumber) {
            var xMineCoordinate = GENERATOR.nextInt(config.rows());
            var yMineCoordinate = GENERATOR.nextInt(config.columns());
            var point = new Point(xMineCoordinate, yMineCoordinate);
            minePoints.add(point);
        }
        return minePoints;
    }
}

