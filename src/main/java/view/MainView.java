package view;

import model.Draw;
import model.MyButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;

import static model.MyButton.bomb;
import static model.MyButton.hourglass;
import static model.MyButton.flag;
import static model.MyButton.questionMark;


public class MainView implements WindowListener, View {

	private final static int NUMBER_OF_BUTTON_STATES = 3;
	private final static int EMPTY_STATE = 0;
	private final static int FLAG_STATE = 1;
	private final static int QUESTION_STATE = 2;
	private final static int BUTTON_WIDTH = 35;
	private final static int BUTTON_HEIGHT = 35;
	private Timer timer;    // SPROBUJ USTAWIC TEN WATEK JAKO DEMON
	private JMenuItem newGame, options, closing;
	private MyButton[][] buttons;
	private Draw draw;
	private RightMouseButton rightMouseButton;
	private LeftMouseButton leftMouseButton;
	private JFrame jf;
	private JPanel jp;
	private int columns;
	private int rows;
	private int minesNumber;
	private int time;
	private int flagsNumber;
	private JLabel timeLabel;
	private JLabel minesLeftLabel;

	private MainView(int rows, int columns, int minesNumber) {
		this.rows = rows;
		this.columns = columns;
		this.minesNumber = minesNumber;
//		draw = new Draw(minesNumber);
//		draw.makeDraw( rows, columns);
//		buttons = new MyButton[rows][columns];
		jf = new JFrame();
		jp = new JPanel();
		flagsNumber = minesNumber;
		minesLeftLabel = new JLabel(String.valueOf(flagsNumber));
		leftMouseButton = new LeftMouseButton();
		rightMouseButton = new RightMouseButton();
		timeLabel = new JLabel("0");
		}

	public MainView(MyButton[][] myButtons,Draw draw){
		this(myButtons.length,myButtons[0].length,draw.getMinesNumber());
		this.draw = draw;
		this.buttons = myButtons;
	}

	public MainView(int rows, int columns, int minesNumber, Draw draw) {
		this(rows, columns, minesNumber);
		this.draw = draw;
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
	public JFrame getJf() {
		return jf;
	}
	public Draw getDraw() {
		return draw;
	}

	public void go() {
		setButtons();
		addButtonsFeatures();
		showView();
		jf.validate(); // jak sie wlacza to od razu jest plansza nie trzeba przesiagac?
	}

	private void setButtons() {
		int x = 0;
		int y = 0;
		jp.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		for (MyButton[] button : buttons) {
			for (MyButton aButton : button) {
//				buttons[i][j] = new MyButton();
				aButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
				aButton.addMouseListener(rightMouseButton);
				aButton.addMouseListener(leftMouseButton);
				gc.gridx = x;
				gc.gridy = y;
				jp.add(aButton, gc);
				x++;
			}
			x = 0;
			y++;
		}
	}

	public void setMouseListeners(MouseListener actionListener){
		for (MyButton[] button : buttons) {
			for (MyButton aButton : button) {
				aButton.addMouseListener(actionListener);
				aButton.addMouseListener(actionListener);
			}
		}
	}

	private void addButtonsFeatures() {
		for (int i = 0; i < buttons.length; i++) {
			for (int j = 0; j < buttons[i].length; j++) {
				buttons[i][j].addObservers(i, j, buttons);
				buttons[i][j].countMinesAround(i, j, minesNumber, draw, buttons);  }}
	}

	@Override
	public void showView() {
		jf.setTitle("MainView");
		jf.setVisible(true);
		jf.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		jf.addWindowListener(this);
		jf.setSize(columns *BUTTON_WIDTH+70, rows *BUTTON_HEIGHT+150);
//		jf.setIconImage(new ImageIcon(IMAGES_PATH +"bomb.png")
//				.getImage()
//				.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH));
		addMenuBar();
		JPanel jp = new JPanel();
		JPanel jp2 = new JPanel();
		JPanel jp3 = new JPanel();
		jp.setLayout(new BorderLayout());
		JButton hourglassIconButton = new JButton();
		hourglassIconButton.setPreferredSize(new Dimension(BUTTON_WIDTH+5, BUTTON_HEIGHT+5));
		hourglassIconButton.setIcon(hourglass);
		hourglassIconButton.setDisabledIcon(hourglass);
		hourglassIconButton.setEnabled(false);
		jp2.add(hourglassIconButton);
		jp2.add(timeLabel);
		JButton bombIconButton = new JButton();
		bombIconButton.setPreferredSize(new Dimension(BUTTON_WIDTH+5, BUTTON_HEIGHT+5));
		bombIconButton.setIcon(bomb);
		bombIconButton.setDisabledIcon(bomb);
		bombIconButton.setEnabled(false);
		jp3.add(minesLeftLabel);
		jp3.add(bombIconButton);
		jp.add(jp2,BorderLayout.WEST);
		jp.add(jp3,BorderLayout.EAST);
		jf.add(jp,BorderLayout.SOUTH);
	}
	
 private void addMenuBar() {
	JMenuBar jMB = new JMenuBar();
	JMenu game = new JMenu("Game");
	newGame = new JMenuItem("New Game");
	game.add(newGame);
	newGame.addActionListener(this);
    options = new JMenuItem("Options");
	game.add(options);
	options.addActionListener(this);
	closing = new JMenuItem("Close");
	game.add(closing);
	closing.addActionListener(this);
	jMB.add(game);
	jf.setJMenuBar(jMB);
	jf.add(jp);
}

	@Override public void actionPerformed(ActionEvent e) {
		JMenuItem i = (JMenuItem)e.getSource();
		
		if(i.getText().equals("Options"))
		new ChangeView(jf).showView();
		
		if(i.getText().equals("New Game")) {
			new MainView(rows, columns, minesNumber).go();
			jf.dispose();
		}
		if(i.getText().equals("Close"))
				new CloseView(this).showView();
	}
	@Override
	public void windowClosing(WindowEvent e) {
		new CloseView(this).showView();
		
	}	
	
	@Override public void windowOpened(WindowEvent e) {}
	@Override public void windowClosed(WindowEvent e) {}
	@Override public void windowIconified(WindowEvent e) {}
	@Override public void windowDeiconified(WindowEvent e) {}	
	@Override public void windowActivated(WindowEvent e) {}
	@Override public void windowDeactivated(WindowEvent e) {}

	class RightMouseButton implements MouseListener, Serializable{

		@Override public void mouseClicked(MouseEvent e) {

			if (SwingUtilities.isRightMouseButton(e)) {
				for (MyButton[] button : buttons) {
					for (MyButton aButton : button) {
						if (e.getSource() == aButton) {
							aButton.incrementClickNumber();
							if (aButton.getClicksNumber() % NUMBER_OF_BUTTON_STATES == FLAG_STATE) {
								aButton.setIcon(flag);
								flagsNumber--;
								minesLeftLabel.setText(String.valueOf(flagsNumber));
								aButton.removeMouseListener(leftMouseButton);
								return;
							}
							if (aButton.getClicksNumber() % NUMBER_OF_BUTTON_STATES == QUESTION_STATE) {
								aButton.setIcon(questionMark);
								flagsNumber++;
								minesLeftLabel.setText(String.valueOf(flagsNumber));
								aButton.addMouseListener(leftMouseButton);
								return;
							}
							if (aButton.getClicksNumber() % NUMBER_OF_BUTTON_STATES == EMPTY_STATE) {
								aButton.setIcon(null);
								return;
							}
						}
					}
				}
			}
			if(draw.isSuccess(buttons, flag)&& flagsNumber ==0) {
				 timer.stop();
				 timer.setDelay(Integer.MAX_VALUE);
				new SuccessView(MainView.this).showView();

			}
		}

		@Override public void mouseReleased(MouseEvent e) {}
		@Override public void mouseEntered(MouseEvent e) {}
		@Override public void mouseExited(MouseEvent e) {}
		@Override public void mousePressed(MouseEvent e) {}

		}
	class LeftMouseButton implements MouseListener , Serializable {
			
		transient private int timesTimerTurnedOn = 0;
		
		@Override	public void mouseClicked(MouseEvent e) {
			if (SwingUtilities.isLeftMouseButton(e)) {
				if(timesTimerTurnedOn ==0) {
				timer = new Timer(1000, e2 -> {
					//timeLabel.setText("");
				    time++;
				    timeLabel.setText(String.valueOf(time));});
					timer.start();
					timesTimerTurnedOn++;
				}
				for (int j = 0; j < minesNumber; j++) {
					
					if (e.getSource() == buttons[draw.getX(j)][draw.getY(j)]) {
						for (int i = 0; i < minesNumber; i++) {
						buttons[draw.getX(i)][draw.getY(i)].setIcon(bomb);
						buttons[draw.getX(i)][draw.getY(i)].setBackground(Color.RED);	
						
						}
					new FailureView(MainView.this).showView();
						return;
					}}

				for (MyButton[] button : buttons) {
					for (MyButton aButton : button) {
						if (e.getSource() == aButton) {
							if (aButton.getMinesAroundNumber() == 0) {
								aButton.setIcon(null);
								aButton.setEnabled(false);
								aButton.removeMouseListener(rightMouseButton);
								aButton.removeMouseListener(leftMouseButton);
								aButton.notifyObservers();
								return;
							} else {
								aButton.setEnabled(false);
								aButton.setIconOfMines();
								aButton.removeMouseListener(leftMouseButton);
								aButton.removeMouseListener(rightMouseButton);
								return;
							}
						}
					}
				}
			}}
		@Override public void mouseReleased(MouseEvent e) {}	
		@Override public void mouseEntered(MouseEvent e) {}	
		@Override public void mouseExited(MouseEvent e) {}	
		@Override public void mousePressed(MouseEvent e) {}
		
		}	
	
}

