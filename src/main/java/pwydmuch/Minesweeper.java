package pwydmuch;

import pwydmuch.model.Draw;
import pwydmuch.model.MyButton;
import pwydmuch.view.MainView;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class Minesweeper implements Serializable {

    @Serial
    private static final long serialVersionUID = 1161474195722226307L;
    private static final Path SAVE_FILE = Path.of("game-state.ser");

    public static void save(MainView sap) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try (var file = new ObjectOutputStream(Files.newOutputStream(SAVE_FILE))) {
                file.writeObject(sap);
            } catch (Exception ignored) {
            }
        }));
    }

    public static void main(String[] args) {
        try (var file = new ObjectInputStream(Files.newInputStream(SAVE_FILE))) {
            var gameState = (MainView) file.readObject();
            gameState.getFrame().setVisible(true);
        } catch (Exception e) {
            var myButtons = new MyButton[13][13];
            var draw = new Draw(25, myButtons.length, myButtons[0].length);
            new MainView(myButtons, draw).go();
        }
    }
}
