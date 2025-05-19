package pwydmuch.view;

import pwydmuch.model.Board;
import pwydmuch.model.GameConfig;
import pwydmuch.model.MyButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;

import static pwydmuch.view.ImageLoader.*;


public class MainView implements WindowListener, View {

    private final static int BUTTON_WIDTH = 35;
    private final static int BUTTON_HEIGHT = 35;
    private final Board board;
    private Timer timer;    // SPROBUJ USTAWIC TEN WATEK JAKO DEMON
    private final MyButtonAdapter[][] gameBoardAdapter;
    private final RightMouseButton rightMouseButton;
    private final LeftMouseButton leftMouseButton;
    private final JFrame frame;
    private final JPanel jp;
    private final GameConfig gameConfig;
    private int time;
    private int remainingFlagsToSet;
    private final JLabel timeLabel;
    private final JLabel minesLeftLabel;


    public MainView(Board board) {
        this.board = board;
        gameConfig = board.getGameConfig();
        frame = new JFrame();
        jp = new JPanel();
        remainingFlagsToSet = gameConfig.minesNumber();
        minesLeftLabel = new JLabel(String.valueOf(remainingFlagsToSet));
        leftMouseButton = new LeftMouseButton();
        rightMouseButton = new RightMouseButton();
        timeLabel = new JLabel("0");
        this.gameBoardAdapter = MyButtonAdapter.translate(board);
    }

    public int getTime() {
        return time;
    }

    public JFrame getFrame() {
        return frame;
    }


    public void go() {
        setButtons();
        showView();frame.validate(); /* jak sie wlacza to od razu jest plansza nie trzeba przesiagac? */}

    private void setButtons() {
        jp.setLayout(new GridBagLayout());
        var gc = new GridBagConstraints();
        for (var i = 0; i < gameBoardAdapter.length; i++) {
            for (var j = 0; j < gameBoardAdapter[i].length; j++) {
                gameBoardAdapter[i][j].setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
                gameBoardAdapter[i][j].addMouseListener(rightMouseButton);
                gameBoardAdapter[i][j].addMouseListener(leftMouseButton);
                gc.gridx = j;
                gc.gridy = i;
                jp.add(gameBoardAdapter[i][j], gc);
            }
        }
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
        var newGame = new JMenuItem("New Game");
        game.add(newGame);
        newGame.addActionListener(this);
        var options = new JMenuItem("Options");
        game.add(options);
        options.addActionListener(this);
        var closing = new JMenuItem("Close");
        game.add(closing);
        closing.addActionListener(this);
        jMB.add(game);
        frame.setJMenuBar(jMB);
        frame.add(jp);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var i = (JMenuItem) e.getSource();

        if (i.getText().equals("Options"))
            new ChangeView(frame).showView();

        if (i.getText().equals("New Game")) {
            var board = new Board(gameConfig);
            new MainView(board).go();
            frame.dispose();
        }
        if (i.getText().equals("Close"))
            new CloseView(this, gameConfig.columns()).showView();
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

    class RightMouseButton implements MouseListener, Serializable {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
                for (var buttonRowOrColumn : gameBoardAdapter) { //TODO check
                    for (var button : buttonRowOrColumn) {
                        if (e.getSource() == button) {
                            MyButton underlying = button.getUnderlying();
                            underlying.changeState();
                            switch (underlying.getState()) {
                                case FLAG -> {
                                    button.setIcon(FLAG_ICON);
                                    remainingFlagsToSet--;
                                    minesLeftLabel.setText(String.valueOf(remainingFlagsToSet));
                                    button.removeMouseListener(leftMouseButton);
                                }
                                case QUESTION_MARK -> {
                                    button.setIcon(QUESTION_MARK_ICON);
                                    remainingFlagsToSet++;
                                    minesLeftLabel.setText(String.valueOf(remainingFlagsToSet));
                                    button.addMouseListener(leftMouseButton);
                                }
                                case NOT_MARKED -> {
                                    button.setIcon(null);
                                }
                            }
                        }
                    }
                }
            }
            if (board.isSuccess() && remainingFlagsToSet == 0) {
                timer.stop();
                timer.setDelay(Integer.MAX_VALUE);
                new SuccessView(MainView.this, gameConfig).showView();
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
                startTimer();
                var minePoints = board.getMinePoints();
                var isMineHit = minePoints.stream().anyMatch(p -> e.getSource() == gameBoardAdapter[p.x()][p.y()]);
                if (isMineHit) {
                    minePoints.forEach(p -> {
                        gameBoardAdapter[p.x()][p.y()].setIcon(BOMB_ICON);
                        gameBoardAdapter[p.x()][p.y()].setBackground(Color.RED);
                    });
                    new FailureView(board, gameConfig, MainView.this.frame).showView();
                } else {
                    MyButtonAdapter buttonClicked = getButtonClicked(e);
                    buttonClicked.removeMouseListener(leftMouseButton);
                    buttonClicked.removeMouseListener(rightMouseButton);
                    buttonClicked.update();
                }

            }
        }

        private void startTimer() {
            if (!timerAlreadyTurnedOn) {
                timer = new Timer(1000, e2 -> {
                    //timeLabel.setText("");
                    time++;
                    timeLabel.setText(String.valueOf(time));
                });
                timer.start();
                timerAlreadyTurnedOn = true;
            }
        }

        private MyButtonAdapter getButtonClicked(MouseEvent e) {
            for (MyButtonAdapter[] rowOrColumn : gameBoardAdapter) {
                for (MyButtonAdapter button : rowOrColumn) {
                    if (e.getSource() == button) {
                        return button;
                    }
                }
            }
            throw new RuntimeException();
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

