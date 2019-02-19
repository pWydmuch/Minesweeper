package saper.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import saper.model.MaszynaLosujaca;

public class OknoPrzegrana extends JFrame implements Serializable,ActionListener {
	
	private int kolumny;
	private int ileMin;	
	private int wiersze;
	private MaszynaLosujaca losowanie;
	private JButton jb3 ;
	private JButton jb2;
	private JButton jb1;
	private JFrame jf;
	
	
	OknoPrzegrana( OknoGlowne sap){
		this.kolumny = sap.getKolumny();
		this.wiersze = sap.getWiersze();
		this.ileMin = sap.getIleMin();
		this.jf = sap.getJf();			
		this.losowanie = sap.getLosowanie();
		jb1 = new JButton("Zakoncz");
		jb2 = new JButton("Uruchom ponownie te gre");
		jb3 = new JButton("Zagraj ponownie");
	}

	public void wyswietlOkno() {
			
			setTitle("OknoGlowne");
			setVisible(true);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setSize(kolumny*35+70, 200);
			setLocation(jf.getX(),jf.getY()+jf.getHeight()/4);

			JPanel jPan = new JPanel();
			jPan.setLayout(new BoxLayout(jPan,BoxLayout.Y_AXIS));
			add(jPan);
			
			JLabel jl = new JLabel("Niestety ta gra konczy sie twoim niepowodzeniem");
			
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
			new OknoGlowne(wiersze,kolumny,ileMin,losowanie).doDziela();
			jf.dispose();
			this.dispose();
		}
		if(e.getSource()==jb3) {
			new OknoGlowne(wiersze,kolumny,ileMin).doDziela();
			jf.dispose();
			this.dispose();
		}
	}

}
