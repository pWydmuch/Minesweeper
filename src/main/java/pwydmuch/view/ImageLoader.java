package pwydmuch.view;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ImageLoader {

    public static final Map<Integer, ImageIcon> NUMBERS_ICONS;
    public static final ImageIcon QUESTION_MARK_ICON;
    public static final ImageIcon FLAG_ICON;
    public static final ImageIcon BOMB_ICON;
    public static final ImageIcon HOURGLASS_ICON;
    private static final int IMAGE_WIDTH = 20;
    private static final int IMAGE_HEIGHT = 20;
    private static final String IMAGES_PATH = "src/main/resources/images/";
    private static final String NUMBERS_PATH = "src/main/resources/numbers/";

    static {
        NUMBERS_ICONS = loadNumbers();
        QUESTION_MARK_ICON = loadImage("question-mark.png");
        FLAG_ICON = loadImage("flag.png");
        BOMB_ICON = loadImage("bomb.png");
        HOURGLASS_ICON = loadImage("hourglass.png");
    }

    private static ImageIcon loadImage(String imageName) {
        return getImageIcon(IMAGES_PATH + imageName);
    }

    private static Map<Integer, ImageIcon> loadNumbers() {
        return IntStream.rangeClosed(1, 8).mapToObj(i ->
                Map.entry(i, getImageIcon(NUMBERS_PATH + i + ".png")
        )).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static ImageIcon getImageIcon(String imagePath) {
        return new ImageIcon(new ImageIcon(imagePath)
                .getImage()
                .getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH)
        );
    }

}
