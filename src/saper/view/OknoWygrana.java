package saper.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import saper.model.MaszynaLosujaca;



	public class OknoWygrana extends JFrame implements Serializable,ActionListener {
		
	
		private int czas;
		private JButton jb2;
		private JButton jb1;
		private JFrame jf;
		private int ileMin;
		private int kolumny;
		private int wiersze;
		
		OknoWygrana(Saper sap){
			
			this.kolumny = sap.getKolumny();
			this.wiersze = sap.getWiersze();
			this.ileMin = sap.getIleMin();
			this.czas = sap.getCzas();
			this.jf = sap.getJf();		
			jb1 = new JButton("Zakoncz");
			jb2 = new JButton("Zagraj ponownie");
		}

		public void wyswietlOkno() {
				
				setTitle("Saper");
				setVisible(true);
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				setSize(250, 200);
				setLocation(jf.getX(),jf.getY()+jf.getHeight()/4);

				JPanel jPan = new JPanel();
				JPanel jPan2 = new JPanel();
				JPanel jPan3 = new JPanel();
				JLabel jl = new JLabel("Gratulacje Wygrana");
				jPan.add(jl);			
				JLabel jl2 = new JLabel("czas: ");
				JLabel jakiCzas = new JLabel(String.valueOf(czas)+"s");			
				jPan2.add(jl2);			
				jPan2.add(jakiCzas);			
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
				Saper sap1 = new Saper(wiersze,kolumny,ileMin);
				sap1.doDziela();
				Saper.zapisz(sap1);
				System.exit(0);
			}
			
			if(e.getSource()==jb2) {
				new Saper(wiersze,kolumny,ileMin).doDziela();
				jf.dispose();
				this.dispose();
			}
		}

	}



