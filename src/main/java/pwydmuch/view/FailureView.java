package pwydmuch.view;

import pwydmuch.domain.Board;
import pwydmuch.domain.dtos.GameConfig;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class FailureView extends JFrame implements View {

    private final GameConfig gameConfig;
    private final JButton playNewGameButton;
    private final JButton playSameGameButton;
    private final JButton closeButton;
    private final JFrame frame;
    private final Board board;


    FailureView(Board board, GameConfig gameConfig, JFrame frame) {
        this.board = board;
        this.gameConfig = gameConfig;
        this.frame = frame;
        this.closeButton = new JButton("Close");
        this.playSameGameButton = new JButton("Play the same game");
        this.playNewGameButton = new JButton("Play again");
    }

    @Override
    public void showView() {
        setTitle("MainView");
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(gameConfig.columns() * 35 + 70, 200);
        setLocation(frame.getX(), frame.getY() + frame.getHeight() / 4);

        var jPan = new JPanel();
        jPan.setLayout(new BoxLayout(jPan, BoxLayout.Y_AXIS));
        add(jPan);

        var failureLabel = new JLabel("Unfortunately you have failed");

        var jPan3 = new JPanel();
        jPan3.add(failureLabel);
        jPan.add(jPan3);
        var jPan2 = new JPanel();
        closeButton.addActionListener(this);
        playSameGameButton.addActionListener(this);
        playNewGameButton.addActionListener(this);
        jPan2.add(closeButton);
        jPan2.add(playSameGameButton);
        jPan2.add(playNewGameButton);

        jPan.add(jPan2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == closeButton) System.exit(0);
        if (e.getSource() == playSameGameButton) new MainView(board.onceAgain());
        if (e.getSource() == playNewGameButton) new MainView(new Board(gameConfig));
        frame.dispose();
        this.dispose();
    }
}
