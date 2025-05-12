package pwydmuch.view;

import pwydmuch.model.Draw;
import pwydmuch.model.MyButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;

import static pwydmuch.view.ImageLoader.*;


public class MainView implements WindowListener, View {

    private final static int BUTTON_WIDTH = 35;
    private final static int BUTTON_HEIGHT = 35;
    private Timer timer;    // SPROBUJ USTAWIC TEN WATEK JAKO DEMON
    private MyButton[][] gameBoard;
    private Draw draw;
    private final RightMouseButton rightMouseButton;
    private final LeftMouseButton leftMouseButton;
    private final JFrame frame;
    private final JPanel jp;
    private final int columns;
    private final int rows;
    private final int minesNumber;
    private int time;
    private int flagsNumber;
    private final JLabel timeLabel;
    private final JLabel minesLeftLabel;

    private MainView(int rows, int columns, int minesNumber) {
        this.rows = rows;
        this.columns = columns;
        this.minesNumber = minesNumber;
//		draw = new Draw(minesNumber);
//		draw.makeDraw( rows, columns);
//		buttons = new MyButton[rows][columns];
        frame = new JFrame();
        jp = new JPanel();
        flagsNumber = minesNumber;
        minesLeftLabel = new JLabel(String.valueOf(flagsNumber));
        leftMouseButton = new LeftMouseButton();
        rightMouseButton = new RightMouseButton();
        timeLabel = new JLabel("0");
    }

    public MainView(MyButton[][] gameBoard, Draw draw) {
        this(gameBoard.length, gameBoard[0].length, draw.getMinesNumber());
        this.draw = draw;
        this.gameBoard = gameBoard;
    }

    public int getRows() {
        return rows;
    }

    public int getMinesNumber() {
        return minesNumber;
    }

    public int getTime() {
        return time;
    }

    public int getColumns() {
        return columns;
    }

    public JFrame getFrame() {
        return frame;
    }

    public Draw getDraw() {
        return draw;
    }

    public void go() {
        setButtons();
        addButtonsFeatures();
        showView();
        frame.validate(); // jak sie wlacza to od razu jest plansza nie trzeba przesiagac?
    }

    private void setButtons() {
        jp.setLayout(new GridBagLayout());
        var gc = new GridBagConstraints();
        for (var i = 0; i < gameBoard.length; i++) {
            for (var j = 0; j < gameBoard[i].length; j++) {
                gameBoard[i][j] = new MyButton();
                gameBoard[i][j].setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
                gameBoard[i][j].addMouseListener(rightMouseButton);
                gameBoard[i][j].addMouseListener(leftMouseButton);
                gc.gridx = j;
                gc.gridy = i;
                jp.add(gameBoard[i][j], gc);
            }
        }
    }

    private void addButtonsFeatures() {
        for (var i = 0; i < gameBoard.length; i++) {
            for (var j = 0; j < gameBoard[i].length; j++) {
                gameBoard[i][j].addObservers(i, j, gameBoard);
                gameBoard[i][j].countMinesAround(i, j, draw, gameBoard);
            }
        }
    }

    @Override
    public void showView() {
        frame.setTitle("MainView");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(this);
        frame.setSize(columns * BUTTON_WIDTH + 70, rows * BUTTON_HEIGHT + 150);
        addMenuBar();
        var panel = new JPanel();
        var panel2 = new JPanel();
        var panel3 = new JPanel();
        panel.setLayout(new BorderLayout());
        var hourglassIconButton = createFieldWithImage(hourglass);
        panel2.add(hourglassIconButton);
        panel2.add(timeLabel);
        var bombIconButton = createFieldWithImage(bomb);
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
            var myButtons = new MyButton[rows][columns];
            var draw = new Draw(minesNumber, rows, columns);
            new MainView(myButtons, draw).go();
            frame.dispose();
        }
        if (i.getText().equals("Close"))
            new CloseView(this).showView();
    }

    @Override
    public void windowClosing(WindowEvent e) {
        new CloseView(this).showView();

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
                for (var button : gameBoard) {
                    for (var aButton : button) {
                        if (e.getSource() == aButton) {
                            aButton.changeState();
                            switch (aButton.getState()) {
                                case FLAG -> {
                                    aButton.setIcon(flag);
                                    flagsNumber--;
                                    minesLeftLabel.setText(String.valueOf(flagsNumber));
                                    aButton.removeMouseListener(leftMouseButton);
                                    return;
                                }
                                case QUESTION_MARK -> {
                                    aButton.setIcon(questionMark);
                                    flagsNumber++;
                                    minesLeftLabel.setText(String.valueOf(flagsNumber));
                                    aButton.addMouseListener(leftMouseButton);
                                    return;
                                }
                                case EMPTY -> {
                                    aButton.setIcon(null);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
            if (draw.isSuccess(gameBoard, flag) && flagsNumber == 0) {
                timer.stop();
                timer.setDelay(Integer.MAX_VALUE);
                new SuccessView(MainView.this).showView();

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
        transient private int timesTimerTurnedOn = 0;

        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                if (timesTimerTurnedOn == 0) {
                    timer = new Timer(1000, e2 -> {
                        //timeLabel.setText("");
                        time++;
                        timeLabel.setText(String.valueOf(time));
                    });
                    timer.start();
                    timesTimerTurnedOn++;
                }
                var minePoints = draw.getMinePoints();
                var isMineHit = minePoints.stream()
                        .anyMatch(p -> e.getSource() == gameBoard[p.x()][p.y()]);
                if (isMineHit) {
                    minePoints.forEach(p -> {
                        gameBoard[p.x()][p.y()].setIcon(bomb);
                        gameBoard[p.x()][p.y()].setBackground(Color.RED);
                    });
                    new FailureView(MainView.this).showView();
                } else {
                    for (MyButton[] button : gameBoard) {
                        for (MyButton aButton : button) {
                            if (e.getSource() == aButton) {
                                aButton.removeMouseListener(leftMouseButton);
                                aButton.removeMouseListener(rightMouseButton);
                                aButton.update();
                                return;
                            }
                        }
                    }
                }
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

