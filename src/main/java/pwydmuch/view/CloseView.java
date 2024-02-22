package pwydmuch.view;

import pwydmuch.Minesweeper;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CloseView extends JFrame implements View {

    private static final String SAVE = "Save";
    private static final String DONT_SAVE = "Don't save";
    private static final String CANCEL = "Cancel";
    private final int columns;
    private final int frameX;
    private final int frameY;
    private final int frameHeight;
    private final JButton saveButton;
    private final JButton dontSaveButton;
    private final JButton concellButton;
    private final MainView mainView;

    CloseView(MainView mainView) {
        this.columns = mainView.getColumns();
        this.frameX = mainView.getFrame().getX();
        this.frameY = mainView.getFrame().getY();
        this.frameHeight = mainView.getFrame().getHeight();
        this.mainView = mainView;
        saveButton = new JButton(SAVE);
        dontSaveButton = new JButton(DONT_SAVE);
        concellButton = new JButton(CANCEL);
    }

    @Override
    public void showView() {
        setTitle("MainView");
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(columns * 35 + 70, 200);
        setLocation(frameX, frameY + frameHeight / 4);
        JPanel jPan = new JPanel();
        JPanel jPan2 = new JPanel();
        JPanel jPan3 = new JPanel();
        jPan.setLayout(new BoxLayout(jPan, BoxLayout.Y_AXIS));
        add(jPan);
        JLabel questionLabel = new JLabel("What do you want to do?");
        jPan3.add(questionLabel);
        jPan.add(jPan3);
        saveButton.addActionListener(this);
        dontSaveButton.addActionListener(this);
        concellButton.addActionListener(this);
        jPan2.add(saveButton);
        jPan2.add(dontSaveButton);
        jPan2.add(concellButton);
        jPan.add(jPan2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            Minesweeper.save(mainView);
            System.exit(0);
        }
        if (e.getSource() == dontSaveButton) {
//            MainView sap1 = new MainView(mainView.getRows(), mainView.getColumns(), mainView.getMinesNumber());
//            sap1.go();
//            pwydmuch.Minesweeper.save(sap1);
            System.exit(0);
        }
        this.dispose();
    }

}
