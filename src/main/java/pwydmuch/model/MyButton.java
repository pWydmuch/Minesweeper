package pwydmuch.model;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyButton extends JButton implements Observer, Serializable, Observable {

    private final static int IMAGE_WIDTH = 20;
    private final static int IMAGE_HEIGHT = 20;
    private final static String IMAGES_PATH = "src/main/resources/images/";
    private final static String NUMBERS_PATH = "src/main/resources/numbers/";
    private int minesAroundNumber;
    private int clicksNumber;
    private static final Map<Integer, ImageIcon> map;
    public static ImageIcon questionMark;
    public static ImageIcon flag;
    public static ImageIcon bomb;
    public static ImageIcon hourglass;
    private final ArrayList<Observer> observers;

    static {
        map = new HashMap<>();
        loadImages();
        loadNumbers();
    }

    public MyButton() {
        observers = new ArrayList<>();
    }

    private static void loadImages() {
        try {
            questionMark = loadImage("question-mark.png");
            flag = loadImage("flag.png");
            bomb = loadImage("bomb.png");
            hourglass = loadImage("hourglass.png");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static ImageIcon loadImage(String imageName) {
        return new ImageIcon(new ImageIcon(IMAGES_PATH + imageName)
                .getImage()
                .getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH));
    }

    private static void loadNumbers() {
        for (int i = 1; i <= 8; i++) {
            map.put(i, new ImageIcon(NUMBERS_PATH + i + ".png"));
            Image image = map.get(i).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            map.remove(i);
            map.put(i, new ImageIcon(image));
        }
    }

    public void setIconOfMines() {
        setIcon(map.get(getMinesAroundNumber()));
        setDisabledIcon(map.get(getMinesAroundNumber()));
    }

    public int getMinesAroundNumber() {
        return minesAroundNumber;
    }

    public int getClicksNumber() {
        return clicksNumber;
    }

    public void incrementClickNumber() {
        clicksNumber++;
    }

    public void countMinesAround(int row, int column, Draw draw, MyButton[][] myButtons) {
        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = column - 1; c <= column + 1; c++) {
                if (r >= 0 && c >= 0 && c < myButtons[0].length && r < myButtons.length) {
                    minesAroundNumber += draw.checkIfBomb(r, c) ? 1 : 0;
                }
            }
        }
    }

    @Override
    public void addObserver(Observer ob) {
        observers.add(ob);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    @Override
    public void removeObserver(Observer ob) {
    }

    public void addObservers(int row, int column, MyButton[][] jb) {
        if (row - 1 >= 0) addObserver(jb[row - 1][column]);
        if (column - 1 >= 0) addObserver(jb[row][column - 1]);
        if (column + 1 < jb[0].length) addObserver(jb[row][column + 1]);
        if (row + 1 < jb.length) addObserver(jb[row + 1][column]);
        if (row - 1 >= 0 && column - 1 >= 0) addObserver(jb[row - 1][column - 1]);
        if (row - 1 >= 0 && column + 1 < jb[0].length) addObserver(jb[row - 1][column + 1]);
        if (row + 1 < jb.length && column - 1 >= 0) addObserver(jb[row + 1][column - 1]);
        if (row + 1 < jb.length && column + 1 < jb[0].length) addObserver(jb[row + 1][column + 1]);
    }

    @Override
    public void update() {
        if (isEnabled()) {
            if (getIcon() != flag && getIcon() != questionMark) {
                setEnabled(false);
                if (minesAroundNumber == 0) {
                    notifyObservers();
                } else {
                    setIcon(map.get(minesAroundNumber));
                    setDisabledIcon(map.get(minesAroundNumber));
                }
            }
        }
    }
}


