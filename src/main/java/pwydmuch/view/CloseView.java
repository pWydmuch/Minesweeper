package pwydmuch.view;

import pwydmuch.Minesweeper;
import pwydmuch.service.GameStateService;

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
    private final JButton cancelButton;
    private final MainView mainView;
    private final GameStateService gameStateService;

    CloseView(MainView mainView, int columns) {
        this.columns = columns;
        this.frameX = mainView.getFrame().getX();
        this.frameY = mainView.getFrame().getY();
        this.frameHeight = mainView.getFrame().getHeight();
        this.mainView = mainView;
        this.gameStateService = new GameStateService();
        saveButton = new JButton(SAVE);
        dontSaveButton = new JButton(DONT_SAVE);
        cancelButton = new JButton(CANCEL);
    }

    @Override
    public void showView() {
        setTitle("MainView");
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(columns * 35 + 70, 200);
        setLocation(frameX, frameY + frameHeight / 4);
        var jPan = new JPanel();
        var jPan2 = new JPanel();
        var jPan3 = new JPanel();
        jPan.setLayout(new BoxLayout(jPan, BoxLayout.Y_AXIS));
        add(jPan);
        var questionLabel = new JLabel("What do you want to do?");
        jPan3.add(questionLabel);
        jPan.add(jPan3);
        saveButton.addActionListener(this);
        dontSaveButton.addActionListener(this);
        cancelButton.addActionListener(this);
        jPan2.add(saveButton);
        jPan2.add(dontSaveButton);
        jPan2.add(cancelButton);
        jPan.add(jPan2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            gameStateService.saveGameState(mainView.getBoard(), mainView.getTime());
            mainView.getFrame().dispose();
            System.exit(0);
        } else if (e.getSource() == dontSaveButton) {
            gameStateService.clearSavedState();
            mainView.getFrame().dispose();
            System.exit(0);
        } else if (e.getSource() == cancelButton) {
            this.dispose();
        }
    }

}
