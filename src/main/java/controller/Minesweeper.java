package controller;

import view.MainView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Minesweeper {

    private static final long serialVersionUID = 1161474195722226307L;



    public static void save(MainView sap) {

        Runtime.getRuntime().addShutdownHook(new Thread(()-> {
            try(ObjectOutputStream so1
                        = new ObjectOutputStream(new FileOutputStream("game-state.ser"))) {
                so1.writeObject(sap);
            }catch(Exception e) {}}));

    }

    public static void main(String[] args) {

        try(ObjectInputStream so =
                    new ObjectInputStream(new FileInputStream("game-state.ser"))) {
            MainView sap = (MainView) so.readObject();
            sap.getJf().setVisible(true);
        }catch(Exception e) {
            new MainView(13,13,25).go();
        }
    }
}
