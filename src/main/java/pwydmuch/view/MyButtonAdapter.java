package pwydmuch.view;

import pwydmuch.model.Board;
import pwydmuch.model.FieldDto;

import javax.swing.*;

import java.awt.*;

import static pwydmuch.view.ImageLoader.*;

public class MyButtonAdapter extends JButton {
    private final int row;
    private final int column;
    public MyButtonAdapter(FieldDto field) {
        this.row = field.row();
        this.column = field.column();
        setPreferredSize(new Dimension(30, 30));
    }

    public void flagButton(FieldDto field) {
        setIcon(switch (field.fieldState()) {
            case FLAG -> FLAG_ICON;
            case QUESTION_MARK -> QUESTION_MARK_ICON;
            case REVEALED -> showMinesAroundNr(field.minesAround());
            case NOT_MARKED -> null;
        });
    }

    private ImageIcon showMinesAroundNr(Integer minesAroundNumber) {
        setEnabled(false);
        return switch (minesAroundNumber) {
            case 0 -> null;
            default -> {
                setDisabledIcon(NUMBERS_ICONS.get(minesAroundNumber));
                yield NUMBERS_ICONS.get(minesAroundNumber);
            }
        };
    }


    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
