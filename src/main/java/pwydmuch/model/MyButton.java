package pwydmuch.model;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.IntBinaryOperator;

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
    private final Set<Observer> observers;

    static {
        map = new HashMap<>();
        loadImages();
        loadNumbers();
    }

    public MyButton() {
        observers = new HashSet<>();
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

    public void countMinesAround(int row, int column, Draw draw, MyButton[][] gameBoard) {
        BiConsumer<Integer, Integer> countBombs = (Integer r, Integer c) -> minesAroundNumber += draw.checkIfBomb(r, c) ? 1 : 0;
        browseBoard(row, column, gameBoard, countBombs);
    }

    public void addObservers(int row, int column, MyButton[][] gameBoard) {
        BiConsumer<Integer, Integer> addObservers = (Integer r, Integer c) -> addObserver(gameBoard[r][c]);
        browseBoard(row, column, gameBoard, addObservers);
    }

    private void browseBoard(int row, int column, MyButton[][] gameBoard, BiConsumer<Integer, Integer> fun) {
        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = column - 1; c <= column + 1; c++) {
                if (r >= 0 && c >= 0 && c < gameBoard[0].length && r < gameBoard.length) {
                    fun.accept(r, c);
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
        observers.forEach(Observer::update);
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


