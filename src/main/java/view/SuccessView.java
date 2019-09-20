package view;


import controller.Minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;


public class SuccessView extends JFrame implements Serializable,ActionListener {

    private static final String CLOSE ="Close";
    private static final String PLAY_AGAIN ="Play again";
		private int time;
		private JButton jb2;
		private JButton jb1;
		private JFrame jf;
		private int minesNumber;
		private int columns;
		private int rows;
		
		SuccessView(MainView sap){
			
			this.columns = sap.getColumns();
			this.rows = sap.getRows();
			this.minesNumber = sap.getMinesNumber();
			this.time = sap.getTime();
			this.jf = sap.getJf();		
			jb1 = new JButton(CLOSE);
			jb2 = new JButton(PLAY_AGAIN);
		}

		public void showView() {
				
				setTitle("MainView");
				setVisible(true);
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				setSize(250, 200);
				setLocation(jf.getX(),jf.getY()+jf.getHeight()/4);

				JPanel jPan = new JPanel();
				JPanel jPan2 = new JPanel();
				JPanel jPan3 = new JPanel();
				JLabel jl = new JLabel("Congratulations you've won");
				jPan.add(jl);			
				JLabel jl2 = new JLabel("Time: ");
				JLabel timeLabel = new JLabel(time +"s");
				jPan2.add(jl2);			
				jPan2.add(timeLabel);
				jPan3.add(jb1);
				jPan3.add(jb2);				
				jPan.add(jPan2);			
				add(jPan, BorderLayout.NORTH);
				add(jPan2, BorderLayout.CENTER);
				add(jPan3, BorderLayout.SOUTH);			
				jb1.addActionListener(this);
				jb2.addActionListener(this);
			
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == jb1) {
				MainView sap1 = new MainView(rows, columns, minesNumber);
				sap1.go();
				Minesweeper.save(sap1);
				System.exit(0);
			}
			
			if(e.getSource()==jb2) {
				new MainView(rows, columns, minesNumber).go();
				jf.dispose();
				this.dispose();
			}
		}

	}



