package pwydmuch.view;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class ImageLoader {

    private final static int IMAGE_WIDTH = 20;
    private final static int IMAGE_HEIGHT = 20;
    private final static String IMAGES_PATH = "src/main/resources/images/";
    private final static String NUMBERS_PATH = "src/main/resources/numbers/";
    public static final Map<Integer, ImageIcon> numberImages;
    public static ImageIcon questionMark;
    public static ImageIcon flag;
    public static ImageIcon bomb;
    public static ImageIcon hourglass;

    static {
        numberImages = new HashMap<>();
        loadImages();
        loadNumbers();
    }

    private static void loadImages() {
        questionMark = loadImage("question-mark.png");
        flag = loadImage("flag.png");
        bomb = loadImage("bomb.png");
        hourglass = loadImage("hourglass.png");
    }

    private static ImageIcon loadImage(String imageName) {
        return new ImageIcon(new ImageIcon(IMAGES_PATH + imageName)
                .getImage()
                .getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH));
    }

    private static void loadNumbers() {
        IntStream.rangeClosed(1, 8).forEach(i -> {
            var image = new ImageIcon(NUMBERS_PATH + i + ".png")
                    .getImage()
                    .getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            numberImages.put(i, new ImageIcon(image));
        });
    }
}
