package pwydmuch.view;

import pwydmuch.model.Draw;
import pwydmuch.model.MyButton;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class FailureView extends JFrame implements View {

    private static final String CLOSE ="Close";
    private static final String PLAY_AGAIN ="Play again";
    private static final String BEGINNER ="Beginner";
	private int columns;
	private int minesNumber;
	private int rows;
	private Draw draw;
	private JButton playNewGameButton;
	private JButton playSameGameButton;
	private JButton closeButton;
	private JFrame frame;
	
	
	FailureView(MainView mainView){
		this.columns = mainView.getColumns();
		this.rows = mainView.getRows();
		this.minesNumber = mainView.getMinesNumber();
		this.frame = mainView.getFrame();
		this.draw = mainView.getDraw();
		closeButton = new JButton("Close");
		playSameGameButton = new JButton("Play the same game");
		playNewGameButton = new JButton("Play again");
	}

	@Override
	public void showView() {
			
			setTitle("MainView");
			setVisible(true);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setSize(columns *35+70, 200);
			setLocation(frame.getX(), frame.getY()+ frame.getHeight()/4);

			JPanel jPan = new JPanel();
			jPan.setLayout(new BoxLayout(jPan,BoxLayout.Y_AXIS));
			add(jPan);
			
			JLabel failureLabel = new JLabel("Unfortunately you have failed");
			
			JPanel jPan3 = new JPanel();
			jPan3.add(failureLabel);
			jPan.add(jPan3);
			JPanel jPan2 = new JPanel();
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

		if(e.getSource() == closeButton) System.exit(0);

		MyButton[][] myButtons = new MyButton[rows][columns];
		Draw newDraw = new Draw(minesNumber,rows,columns);
//		MainView mainView = new MainView(myButtons,draw);
		if(e.getSource() == playSameGameButton) {
			new MainView(myButtons, draw).go();
		}
		if(e.getSource()== playNewGameButton) {
			new MainView(myButtons,newDraw).go();
		}
		frame.dispose();
		this.dispose();
	}

}
