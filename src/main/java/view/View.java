package view;

import java.awt.event.ActionListener;
import java.io.Serializable;

public interface View extends ActionListener, Serializable {
    void showView();
}
