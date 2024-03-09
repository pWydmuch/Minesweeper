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

    public static void save(MainView sap) {
        Runtime.getRuntime().addShutdownHook(new Thread(()-> {
            try(ObjectOutputStream so1
                        = new ObjectOutputStream(Files.newOutputStream(Path.of("game-state.ser")))) {
                so1.writeObject(sap);
            }catch(Exception ignored) {}}));

    }

    public static void main(String[] args) {
        try(ObjectInputStream so =
                    new ObjectInputStream(Files.newInputStream(Path.of("game-state.ser")))) {
            MainView sap = (MainView) so.readObject();
            sap.getFrame().setVisible(true);
        }catch(Exception e) {
            MyButton[][] myButtons = new MyButton[13][13];
            Draw draw = new Draw(25,myButtons.length, myButtons[0].length);
            new MainView(myButtons,draw).go();
        }
    }
}