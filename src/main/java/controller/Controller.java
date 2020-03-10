package controller;

import model.Draw;
import model.MyButton;
import view.MainView;
import view.View;

import java.io.*;

public class Controller implements Serializable {

    private MyButton[][] buttons;
    private Draw draw;
    private View view;

    public Controller(MyButton[][] buttons, Draw draw, View view) {
        this.buttons = buttons;
        this.draw = draw;
        this.view = view;
    }

    public void save() {
        Runtime.getRuntime().addShutdownHook(new Thread(()-> {
            try(ObjectOutputStream so1
                        = new ObjectOutputStream(new FileOutputStream("game-state.ser"))) {
                so1.writeObject(this);
            }catch(Exception e) {}}));
    }

    public void start(){

    }

}
