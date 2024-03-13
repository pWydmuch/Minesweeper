package pwydmuch.view;


import pwydmuch.model.Draw;
import pwydmuch.model.MyButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;


public class SuccessView extends JFrame implements View {
    private static final String CLOSE = "Close";
    private static final String PLAY_AGAIN = "Play again";
    private static final int FRAME_WIDTH = 250;
    private static final int FRAME_HEIGHT = 200;
    private final int time;
    private final JButton closeButton;
    private final JButton playAgainButton;
    private final JFrame frame;
    private final int minesNumber;
    private final int columns;
    private final int rows;

    SuccessView(MainView mainView) {
        this.columns = mainView.getColumns();
        this.rows = mainView.getRows();
        this.minesNumber = mainView.getMinesNumber();
        this.time = mainView.getTime();
        this.frame = mainView.getFrame();
        playAgainButton = new JButton(CLOSE);
        closeButton = new JButton(PLAY_AGAIN);
    }

    @Override
    public void showView() {
        setTitle("MainView");
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setLocation(frame.getX(), frame.getY() + frame.getHeight() / 4);

        var jPan = new JPanel();
        var jPan2 = new JPanel();
        var jPan3 = new JPanel();
        var congratulationLabel = new JLabel("Congratulations you've won");
        jPan.add(congratulationLabel);
        var timeTextLabel = new JLabel("Time: ");
        var timeLabel = new JLabel(time + "s");
        jPan2.add(timeTextLabel);
        jPan2.add(timeLabel);
        jPan3.add(playAgainButton);
        jPan3.add(closeButton);
        jPan.add(jPan2);
        add(jPan, BorderLayout.NORTH);
        add(jPan2, BorderLayout.CENTER);
        add(jPan3, BorderLayout.SOUTH);
        playAgainButton.addActionListener(this);
        closeButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playAgainButton) {

            var myButtons = new MyButton[rows][columns];
            var newDraw = new Draw(minesNumber, rows, columns);
            var mainView = new MainView(myButtons, newDraw);
            mainView.go();
//            pwydmuch.Minesweeper.save(mainView);
            System.exit(0);
        }

        if (e.getSource() == closeButton) {
//            new MainView(rows, columns, minesNumber).go();
            frame.dispose();
            this.dispose();
        }
    }

}



