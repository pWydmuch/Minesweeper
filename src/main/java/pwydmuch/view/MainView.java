package pwydmuch.view;

import pwydmuch.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
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
        this.gameBoardAdapter = translate(board.getGameState().fieldDtos());
        showView();
        frame.validate(); /* jak sie wlacza to od razu jest plansza nie trzeba przesiagac? */
    }


    int getTime() {
        return time;
    }

    JFrame getFrame() {
        return frame;
    }

    private MyButtonAdapter[][] translate(List<FieldDto> board) {
        MyButtonAdapter[][] boardAdapters = createArray(board);
        board.forEach(b -> {
            int i = b.row();
            int j = b.column();
            boardAdapters[i][j] = new MyButtonAdapter(b);
            boardAdapters[i][j].flagButton(b);
            switch (b.fieldState()) {
                case NOT_MARKED, QUESTION_MARK -> {
                    boardAdapters[i][j].addMouseListener(rightMouseButton);
                    boardAdapters[i][j].addMouseListener(leftMouseButton);
                }
                case FLAG -> boardAdapters[i][j].addMouseListener(rightMouseButton);
            }
        });
        refreshGrid(boardAdapters);
        return boardAdapters;
    }

    private void refreshGrid(MyButtonAdapter[][] boardAdapters) {
        if (jp != null) {
            frame.remove(jp);
        }
        setButtons(boardAdapters);
        frame.add(jp);
    }

    private static MyButtonAdapter[][] createArray(List<FieldDto> board) {
        int rows = board.stream().max(Comparator.comparing(FieldDto::row)).map(f -> f.row() + 1).get();
        int columns = board.stream().max(Comparator.comparing(FieldDto::column)).map(f -> f.column() + 1).get();
        return new MyButtonAdapter[rows][columns];
    }

    private void setButtons(MyButtonAdapter[][] boardAdapters) {
        JPanel jp = new JPanel();
        jp.setLayout(new GridBagLayout());
        var gc = new GridBagConstraints();
        for (var i = 0; i < boardAdapters.length; i++) {
            for (var j = 0; j < boardAdapters[i].length; j++) {
                gc.gridx = j;
                gc.gridy = i;
                jp.add(boardAdapters[i][j], gc);
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

    public void setVisible() {
        frame.setVisible(true);
    }

    class RightMouseButton implements MouseListener, Serializable {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
                getButtonClicked(e).ifPresent(button -> {
                    RightClickResponse response = board.clickRight(button.getRow(), button.getColumn());
                    if (response.gameStatus().equals(GameStatus.SUCCESS)) {
                        timer.stop();
                        timer.setDelay(Integer.MAX_VALUE);
                        new SuccessView(MainView.this, gameConfig).showView();
                        return;
                    }
                    switch (response.field().fieldState()) {
                        case FLAG -> button.removeMouseListener(leftMouseButton);
                        case NOT_MARKED -> button.addMouseListener(leftMouseButton);
                    }
                    button.flagButton(response.field());
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
                    LeftClickResponse response = board.clickLeft(button.getRow(), button.getColumn());
                    if (response.gameStatus().equals(GameStatus.GAME_OVER)) {
                        response.minePoints().forEach(p -> {
                            gameBoardAdapter[p.x()][p.y()].setIcon(BOMB_ICON);
                            gameBoardAdapter[p.x()][p.y()].setBackground(Color.RED);
                        });
                        new FailureView(board, gameConfig, MainView.this.frame).showView();
                    } else {
                        button.removeMouseListener(leftMouseButton);
                        button.removeMouseListener(rightMouseButton);
                        gameBoardAdapter = translate(response.fields());
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

