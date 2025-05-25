package pwydmuch.view;

import pwydmuch.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Optional;

import static pwydmuch.view.ImageLoader.*;


public class MainView implements WindowListener, View {

    private final static int BUTTON_WIDTH = 35;
    private final static int BUTTON_HEIGHT = 35;
    private final static String OPTIONS_LABEL = "Options";
    private final static String CLOSE_LABEL = "Close";
    private final static String NEW_GAME_LABEL = "New Game";
    private final Board board;
    private Timer timer;    // SPROBUJ USTAWIC TEN WATEK JAKO DEMON
    private MyButtonAdapter[][] gameBoardAdapter;
    private final RightMouseButton rightMouseButton;
    private final LeftMouseButton leftMouseButton;
    private final JFrame frame;
    private JPanel jp;
    private final GameConfig gameConfig;
    private int time;
    //    private int remainingFlagsToSet;
    private final JLabel timeLabel;
    private final JLabel minesLeftLabel;


    public MainView(Board board) {
        this.board = board;
        this.gameConfig = board.getGameConfig();
        this.frame = new JFrame();
        this.minesLeftLabel = new JLabel(String.valueOf(gameConfig.minesNumber()));
        this.leftMouseButton = new LeftMouseButton();
        this.rightMouseButton = new RightMouseButton();
        this.timeLabel = new JLabel("0");
        this.gameBoardAdapter = MyButtonAdapter.translate(board);
        refreshGrid();
        showView();
        frame.validate(); /* jak sie wlacza to od razu jest plansza nie trzeba przesiagac? */
    }

    private void refreshGrid() {
        if (jp != null) {
            frame.remove(jp);
        }
        setButtons();
        frame.add(jp);
    }

    public int getTime() {
        return time;
    }

    public JFrame getFrame() {
        return frame;
    }

    private void setButtons() {
        JPanel jp = new JPanel();
        jp.setLayout(new GridBagLayout());
        var gc = new GridBagConstraints();
        for (var i = 0; i < gameBoardAdapter.length; i++) {
            for (var j = 0; j < gameBoardAdapter[i].length; j++) {
                gameBoardAdapter[i][j].setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
                switch (gameBoardAdapter[i][j].getUnderlying().getState()) {
                    case NOT_MARKED, QUESTION_MARK -> {
                        gameBoardAdapter[i][j].addMouseListener(rightMouseButton);
                        gameBoardAdapter[i][j].addMouseListener(leftMouseButton);
                    }
                    case FLAG -> gameBoardAdapter[i][j].addMouseListener(rightMouseButton);
                }
                gc.gridx = j;
                gc.gridy = i;
                jp.add(gameBoardAdapter[i][j], gc);
            }
        }
        this.jp = jp;
    }

    @Override
    public void showView() {
        frame.setTitle("MainView");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(this);
        frame.setSize(gameConfig.columns() * BUTTON_WIDTH + 70, gameConfig.rows() * BUTTON_HEIGHT + 150);
        addMenuBar();
        var panel = new JPanel();
        var panel2 = new JPanel();
        var panel3 = new JPanel();
        panel.setLayout(new BorderLayout());
        var hourglassIconButton = createFieldWithImage(HOURGLASS_ICON);
        panel2.add(hourglassIconButton);
        panel2.add(timeLabel);
        var bombIconButton = createFieldWithImage(BOMB_ICON);
        panel3.add(minesLeftLabel);
        panel3.add(bombIconButton);
        panel.add(panel2, BorderLayout.WEST);
        panel.add(panel3, BorderLayout.EAST);
        frame.add(panel, BorderLayout.SOUTH);
    }

    private static JButton createFieldWithImage(ImageIcon imageIcon) {
        var button = new JButton();
        button.setPreferredSize(new Dimension(BUTTON_WIDTH + 5, BUTTON_HEIGHT + 5));
        button.setIcon(imageIcon);
        button.setDisabledIcon(imageIcon);
        button.setEnabled(false);
        return button;
    }

    private void addMenuBar() {
        var jMB = new JMenuBar();
        var game = new JMenu("Game");
        var newGame = new JMenuItem(NEW_GAME_LABEL);
        game.add(newGame);
        newGame.addActionListener(this);
        var options = new JMenuItem(OPTIONS_LABEL);
        game.add(options);
        options.addActionListener(this);
        var closing = new JMenuItem(CLOSE_LABEL);
        game.add(closing);
        closing.addActionListener(this);
        jMB.add(game);
        frame.setJMenuBar(jMB);
        frame.add(jp);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var i = (JMenuItem) e.getSource();
        switch (i.getText()) {
            case OPTIONS_LABEL -> new ChangeView(frame).showView();
            case NEW_GAME_LABEL -> {
                var board = new Board(gameConfig);
                new MainView(board);
                frame.dispose();
            }
            case CLOSE_LABEL -> new CloseView(this, gameConfig.columns()).showView();
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        new CloseView(this, gameConfig.columns()).showView();
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    private Optional<MyButtonAdapter> getButtonClicked(MouseEvent e) {
        return Arrays.stream(gameBoardAdapter)
                .flatMap(Arrays::stream)
                .filter(b -> e.getSource() == b)
                .findAny();
    }

    class RightMouseButton implements MouseListener, Serializable {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
                getButtonClicked(e).ifPresent(button -> {
                    RightClickResponse response = board.clickRight(button.getUnderlying());
                    if (response.gameStatus().equals(GameStatus.SUCCESS)) {
                        timer.stop();
                        timer.setDelay(Integer.MAX_VALUE);
                        new SuccessView(MainView.this, gameConfig).showView();
                        return;
                    }
                    button.flagButton();
                    minesLeftLabel.setText(String.valueOf(response.remainingFlagsToSet()));
                });
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

    }

    class LeftMouseButton implements MouseListener, Serializable {
        transient private boolean timerAlreadyTurnedOn = false;

        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                startTimer(); //TODO seems like timer should be in model -> shouldn't it?
                getButtonClicked(e).ifPresent(button -> {
                    LeftClickResponse response = board.clickLeft(button.getUnderlying());
                    if (response.gameStatus().equals(GameStatus.GAME_OVER)) {
                        response.minePoints().forEach(p -> {
                            gameBoardAdapter[p.x()][p.y()].setIcon(BOMB_ICON);
                            gameBoardAdapter[p.x()][p.y()].setBackground(Color.RED);
                        });
                        new FailureView(board, gameConfig, MainView.this.frame).showView();
                    } else {
                        button.removeMouseListener(leftMouseButton);
                        button.removeMouseListener(rightMouseButton);
                        gameBoardAdapter = MyButtonAdapter.translate(board);
                        refreshGrid();
                    }
                });
            }
        }

        private void startTimer() {
            if (!timerAlreadyTurnedOn) {
                timer = new Timer(1000, __ -> {
                    //timeLabel.setText("");
                    time++;
                    timeLabel.setText(String.valueOf(time));
                });
                timer.start();
                timerAlreadyTurnedOn = true;
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }
    }
}

