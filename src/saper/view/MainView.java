package saper.view;
import saper.model.Button;
import saper.model.Draw;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class MainView implements ActionListener,WindowListener, Serializable  {


	private Map<Integer,ImageIcon> map;
	private Timer timer;    // SPROBUJ USTAWIC TEN WATEK JAKO DEMON
	private ImageIcon questionMark;
	private ImageIcon flag;
	private ImageIcon bomb;
	private ImageIcon hourglass;
	private JMenuItem newGame, options, closing;
	private Button[][] buttons;
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
	

			
//-------------------------------------------------------------------------------------
	public MainView(int rows, int columns, int minesNumber) {
		
		
		this.rows = rows;
		this.columns = columns;
		this.minesNumber = minesNumber;
		draw = new Draw(minesNumber);
		draw.makeDraw(minesNumber, rows, columns);
		buttons = new Button[rows][columns];
		jf = new JFrame();
		jp = new JPanel();
		flagsNumber = minesNumber;
		minesLeftLabel = new JLabel(String.valueOf(flagsNumber));
		map = new HashMap<Integer,ImageIcon>();
		leftMouseButton = new LeftMouseButton();
		rightMouseButton = new RightMouseButton();
		timeLabel = new JLabel("0");
		
		try {
		questionMark = new ImageIcon(new ImageIcon("src/resources/images/question-mark.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		flag = new ImageIcon(new ImageIcon("src/resources/images/flag.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		bomb = new ImageIcon(new ImageIcon("src/resources/images/bomb.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		hourglass = new ImageIcon(new ImageIcon("src/resources/images/hourglass.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		}catch(Exception ex) {
			ex.printStackTrace();
			
		}}
	
	public MainView(int rows, int columns, int minesNumber, Draw draw) {
		this(rows, columns, minesNumber);
		this.draw = draw;
		
	}
//-----------------------------------------------------------------------	
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
	public Map<Integer,ImageIcon> getMap() {
		return map;
	}
	public ImageIcon getFlag(){
		return flag;
	}
	public ImageIcon getQuestionMark(){
		return questionMark;
	}
	public Draw getDraw() {
		return draw;
	}

//-------------------------------------------------------------------------------------------
	public void showBombsPositions() {
		for (int i = 0; i < minesNumber; i++) {
			buttons[draw.getX(i)][draw.getY(i)].setBackground(Color.GREEN);			
		}
	}
//---------------------------------------------------------------------------	

//------------------------------------------------------------------------------------------------------

//-------------------------------------------------------------------------------------
	public void go() {
		fillMap();
		setButtons();
		addButtonsFeatures();
		setFrame();
		jf.validate(); // jak sie wlacza to od razu jest plansza nie trzeba przesiagac?
		//showBombsPositions();

	}
//---------------------------------------------------------------------------------------------
	private void fillMap() {
		for(int i = 1; i<=8;i++) {
			map.put(i,new ImageIcon("src/resource/numbers/"+i+".png"));
			Image image = map.get(i).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
			map.remove(i);
			map.put(i, new ImageIcon(image));
		}	
	}
//----------------------------------------------------------------------------------------------------		
	private void setFrame() {
		jf.setTitle("MainView");
		jf.setVisible(true);
		jf.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		jf.addWindowListener(this);
		jf.setSize(columns *35+70, rows *35+150);
		//jf.setLocationRelativeTo(null);
		jf.setIconImage(new ImageIcon("src/resources/images/bomb.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		addManuBar();
		JPanel jp = new JPanel();
		JPanel jp2 = new JPanel();
		JPanel jp3 = new JPanel();
		jp.setLayout(new BorderLayout());
		JButton d = new JButton();
		d.setPreferredSize(new Dimension(25, 25));
		d.setIcon(hourglass);
		d.setDisabledIcon(hourglass);
		d.setEnabled(false);
		jp2.add(d);
		jp2.add(timeLabel);
		JButton p = new JButton();
		p.setPreferredSize(new Dimension(25, 25));
		p.setIcon(bomb);
		p.setDisabledIcon(bomb);
		p.setEnabled(false);
		jp3.add(minesLeftLabel);
		jp3.add(p);
		jp.add(jp2,BorderLayout.WEST);
		jp.add(jp3,BorderLayout.EAST);
		jf.add(jp,BorderLayout.SOUTH);
	}
	
 private void addManuBar() {
	JMenuBar jMB = new JMenuBar();
	JMenu gra = new JMenu("Game");
	newGame = new JMenuItem("New Game");
	gra.add(newGame);
	newGame.addActionListener(this);
    options = new JMenuItem("Options");
	gra.add(options);
	options.addActionListener(this);
	closing = new JMenuItem("Close");
	gra.add(closing);
	closing.addActionListener(this);
	jMB.add(gra);	
	jf.setJMenuBar(jMB);
	jf.add(jp);
}

 
//----------------------------------------------------------------------------------------------------		
	private void setButtons() {
		int x = 0;
		int y = 0;
		jp.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		for (int i = 0; i < buttons.length; i++) {
			for (int j = 0; j < buttons[i].length; j++) {				
				buttons[i][j] = new Button(this);
				buttons[i][j].setPreferredSize(new Dimension(35, 35));
				buttons[i][j].addMouseListener(rightMouseButton);
				buttons[i][j].addMouseListener(leftMouseButton);
				gc.gridx = x;
				gc.gridy = y;
				jp.add(buttons[i][j], gc);
				x++;
			}
			x = 0;
			y++;
		}	
	}
	private void addButtonsFeatures() {
		for (int i = 0; i < buttons.length; i++) {
			for (int j = 0; j < buttons[i].length; j++) {
				buttons[i][j].addObservers(i, j, buttons);
				buttons[i][j].countMinesAround(i, j, minesNumber, draw, buttons);  }}
	}

//--------------------------------------------------------------------------------
//--------------------------------------------------------------------------------	
	@Override public void actionPerformed(ActionEvent e) {
		JMenuItem i = (JMenuItem)e.getSource();
		
		if(i.getText().equals("Options"))
		new ChangeView(jf).showView();
		
		if(i.getText().equals("New Game")) {
			new MainView(rows, columns, minesNumber).go();
			jf.dispose();
		}
		if(i.getText().equals("Close"))
				new CloseView(this).wyswietlOkno();
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		new CloseView(this).wyswietlOkno();
		
	}	
	
	@Override public void windowOpened(WindowEvent e) {}
	@Override public void windowClosed(WindowEvent e) {}
	@Override public void windowIconified(WindowEvent e) {}
	@Override public void windowDeiconified(WindowEvent e) {}	
	@Override public void windowActivated(WindowEvent e) {}
	@Override public void windowDeactivated(WindowEvent e) {}
	//-----------------------------------------------------------------------------------------	
	//-----------------------------------------------------------------------------------------		
		class RightMouseButton implements MouseListener, Serializable{
		
		@Override public void mouseClicked(MouseEvent e) {
		
			if (SwingUtilities.isRightMouseButton(e)) {
				
				for (int i = 0; i < buttons.length; i++) {
					for (int j = 0; j < buttons[i].length; j++) {
						if (e.getSource() == buttons[i][j]) {
							buttons[i][j].incrementClickNumber();
							if (buttons[i][j].getClicksNumber() % 3 == 1) {
								buttons[i][j].setIcon(flag);
								flagsNumber--;
								minesLeftLabel.setText(String.valueOf(flagsNumber));
								buttons[i][j].removeMouseListener(leftMouseButton);
								return;
							}
							if (buttons[i][j].getClicksNumber() % 3 == 2) {
								buttons[i][j].setIcon(questionMark);
								flagsNumber++;
								minesLeftLabel.setText(String.valueOf(flagsNumber));
								buttons[i][j].addMouseListener(leftMouseButton);
								return;
							}
							if (buttons[i][j].getClicksNumber() % 3 == 0) {
								buttons[i][j].setIcon(null);							
								return;
							}}}}}
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
	//---------------------------------------------------------------------------------------------------	
		class LeftMouseButton implements MouseListener , Serializable {
			
		transient private int ileRazyTimerWlaczony = 0;
		
		@Override	public void mouseClicked(MouseEvent e) {
			
			
			
			if (SwingUtilities.isLeftMouseButton(e)) {
				if(ileRazyTimerWlaczony==0) {
				timer = new Timer(1000, e2 -> {
					//timeLabel.setText("");
				    time++;
				    timeLabel.setText(String.valueOf(time));});
					timer.start();
					ileRazyTimerWlaczony++;
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
				
				for (int i = 0; i < buttons.length; i++) {
					for (int j = 0; j < buttons[i].length; j++) {
						if(e.getSource() == buttons[i][j]) {
							if(buttons[i][j].getMinesAroundNumber()==0) {
								buttons[i][j].setIcon(null);
								buttons[i][j].setEnabled(false);
								buttons[i][j].removeMouseListener(rightMouseButton);
								buttons[i][j].removeMouseListener(leftMouseButton);
								buttons[i][j].notifyObservers();
								return;
							}
							else {

								buttons[i][j].setEnabled(false);
								buttons[i][j].setIcon(map.get(buttons[i][j].getMinesAroundNumber()));
								buttons[i][j].setDisabledIcon(map.get(buttons[i][j].getMinesAroundNumber()));
								buttons[i][j].removeMouseListener(leftMouseButton);
								buttons[i][j].removeMouseListener(rightMouseButton);
							return;
					}}}}}}
		
		
		@Override public void mouseReleased(MouseEvent e) {}	
		@Override public void mouseEntered(MouseEvent e) {}	
		@Override public void mouseExited(MouseEvent e) {}	
		@Override public void mousePressed(MouseEvent e) {}
		
		}	
	
}

