package saper.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class OknoZamkniecie extends JFrame implements Serializable, ActionListener {
	
	private int kolumny;
	private int xRamki;
	private int yRamki;
	private int hRamki;
	private JButton jb1;
	private JButton jb2;
	private JButton jb3;
	private Saper sap;
	
	OknoZamkniecie(Saper sap){
		this.kolumny = sap.getKolumny();
		this.xRamki = sap.getJf().getX();
		this.yRamki = sap.getJf().getY();
		this.hRamki = sap.getJf().getHeight();
		this.sap = sap;
		 jb1 = new JButton("Zapisz");
		 jb2 = new JButton("Nie zapisuj");
		 jb3 = new JButton("Anuluj");
	}

	public void wyswietlOkno() {
	
		setTitle("Saper");
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(kolumny*35+70, 200);
		setLocation(xRamki, yRamki+hRamki/4);
		//jf.setEnabled(false);
		JPanel jPan = new JPanel();
		JPanel jPan2 = new JPanel();
		JPanel jPan3 = new JPanel();
		jPan.setLayout(new BoxLayout(jPan, BoxLayout.Y_AXIS));
		add(jPan);
		
		JLabel jl = new JLabel("Co chcesz zrobic z trwaj¹c¹ gr¹?");
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
			Saper.zapisz(sap);
			System.exit(0);
		}
		if(e.getSource()==jb2) {
			Saper sap1 = new Saper(sap.getWiersze(),sap.getKolumny(),sap.getIleMin());
			sap1.doDziela();
			Saper.zapisz(sap1);
			System.exit(0);
		}
			this.dispose();
				
	}

}
