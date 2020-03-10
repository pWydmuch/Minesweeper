package view;

import model.Draw;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

public class FailureView extends JFrame implements View {

    private static final String CLOSE ="Close";
    private static final String PLAY_AGAIN ="Play again";
    private static final String BEGINNER ="Beginner";
	private int columns;
	private int minesNumber;
	private int rows;
	private Draw draw;
	private JButton jb3 ;
	private JButton jb2;
	private JButton jb1;
	private JFrame jf;
	
	
	FailureView(MainView sap){
		this.columns = sap.getColumns();
		this.rows = sap.getRows();
		this.minesNumber = sap.getMinesNumber();
		this.jf = sap.getJf();			
		this.draw = sap.getDraw();
		jb1 = new JButton("Close");
		jb2 = new JButton("Play the same game");
		jb3 = new JButton("Play again");
	}

	@Override
	public void showView() {
			
			setTitle("MainView");
			setVisible(true);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setSize(columns *35+70, 200);
			setLocation(jf.getX(),jf.getY()+jf.getHeight()/4);

			JPanel jPan = new JPanel();
			jPan.setLayout(new BoxLayout(jPan,BoxLayout.Y_AXIS));
			add(jPan);
			
			JLabel jl = new JLabel("Unfortunately you have failed");
			
			JPanel jPan3 = new JPanel();
			jPan3.add(jl);
			jPan.add(jPan3);
			JPanel jPan2 = new JPanel();
			jb1.addActionListener(this);
			jb2.addActionListener(this);
			jb3.addActionListener(this);
			jPan2.add(jb1);			
			jPan2.add(jb2);			
			jPan2.add(jb3);
			
			jPan.add(jPan2);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == jb1) System.exit(0);
		if(e.getSource() == jb2) {
			new MainView(rows, columns, minesNumber, draw).go();
			jf.dispose();
			this.dispose();
		}
		if(e.getSource()==jb3) {
			new MainView(rows, columns, minesNumber).go();
			jf.dispose();
			this.dispose();
		}
	}

}
