package view;

import controller.Minesweeper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

public class CloseView extends JFrame implements View{

    private static final String SAVE = "Save";
    private static final String DONT_SAVE = "Don't save";
    private static final String CANCEL = "Cancel";
	private int columns;
	private int frameX;
	private int frameY;
	private int frameHeight;
	private JButton jb1;
	private JButton jb2;
	private JButton jb3;
	private MainView sap;
	
	CloseView(MainView sap){
		this.columns = sap.getColumns();
		this.frameX = sap.getJf().getX();
		this.frameY = sap.getJf().getY();
		this.frameHeight = sap.getJf().getHeight();
		this.sap = sap;
		 jb1 = new JButton(SAVE);
		 jb2 = new JButton(DONT_SAVE);
		 jb3 = new JButton(CANCEL);
	}

	@Override
	public void showView() {
	
		setTitle("MainView");
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(columns *35+70, 200);
		setLocation(frameX, frameY + frameHeight /4);
		JPanel jPan = new JPanel();
		JPanel jPan2 = new JPanel();
		JPanel jPan3 = new JPanel();
		jPan.setLayout(new BoxLayout(jPan, BoxLayout.Y_AXIS));
		add(jPan);
		JLabel jl = new JLabel("What do you want to do?");
		jPan3.add(jl);
		jPan.add(jPan3);
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
		if(e.getSource() == jb1) {
			Minesweeper.save(sap);
			System.exit(0);
		}
		if(e.getSource()==jb2) {
			MainView sap1 = new MainView(sap.getRows(),sap.getColumns(),sap.getMinesNumber());
			sap1.go();
			Minesweeper.save(sap1);
			System.exit(0);
		}
			this.dispose();
				
	}

}
