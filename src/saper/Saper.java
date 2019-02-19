package saper;

import saper.view.OknoGlowne;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Saper {

    private static final long serialVersionUID = 1161474195722226307L;



    public static void zapisz(OknoGlowne sap) {

        Runtime.getRuntime().addShutdownHook(new Thread(()-> {
            try(ObjectOutputStream so1
                        = new ObjectOutputStream(new FileOutputStream("Moje.ser"))) {
                so1.writeObject(sap);
            }catch(Exception e) {}}));

    }

    public static void main(String[] args) {

        try(ObjectInputStream so =
                    new ObjectInputStream(new FileInputStream("Moje.ser"))) {
            OknoGlowne sap = (OknoGlowne) so.readObject();
            sap.getJf().setVisible(true);
        }catch(Exception e) {
            new OknoGlowne(13,13,25).doDziela();
        }
    }
}
