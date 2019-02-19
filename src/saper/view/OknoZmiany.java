package saper.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.Serializable;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class OknoZmiany extends JFrame implements Serializable, ActionListener,ItemListener {
	
	private  JTextField tf1, tf2, tf3;
	private JPanel jPan,jPan2,jPan3,jPan4;
	private JCheckBox[] cb;
	private JButton jb2, jb;
	private JFrame jf;
	private JLabel jl;
	private int ileNowyMin;
	private int ileNowyWysokosc;
	private int ileNowySzerokosc;

	
	OknoZmiany( JFrame jf ){
		
		jb = new JButton("OK");
		jb2 = new JButton("Anuluj");
		tf1 = new JTextField("",32);
	    tf2 = new JTextField(32);
	    tf3 = new JTextField(32);
	    jPan4 = new JPanel();
	    jPan3 = new JPanel();
	    jPan2 = new JPanel();
	    jPan = new JPanel();
	    jl = new JLabel();
	    this.jf = jf;
	    cb = new JCheckBox[4];
	     cb[1] = new JCheckBox("Poczatkujacy");
		 cb[2] = new JCheckBox("Sredniozaawansowany");
		 cb[3] = new JCheckBox("Zaawansowany");	
		 cb[0] = new JCheckBox("Niestandardowy");
		
	}

	public void wyswietlOkno() {
		 
			setTitle("Saper");
			setVisible(true);
			setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			setSize(330, 250);
			setResizable(false);
			setLocation(jf.getX(),jf.getY()+jf.getHeight()/4);			
			jPan.setLayout(new BorderLayout());		
			ustawPanel2();
			ustawPanel3();	
			ustawPanel4();
			jPan.add(jPan2,BorderLayout.WEST);
			jPan.add(jPan3,BorderLayout.CENTER);
			jPan.add(jPan4,BorderLayout.SOUTH);
			add(jPan);
			for(JCheckBox c: cb) 
				c.addItemListener(this);			
	}
	private void ustawPanel2() {
		
		jPan2.setLayout(new BoxLayout(jPan2,BoxLayout.Y_AXIS));					
		jPan2.add(cb[1]);
		jPan2.add(new JLabel("-10 min"));
		jPan2.add(new JLabel("-Siatka 9x9 pol"));
		jPan2.add(cb[2]);
		jPan2.add(new JLabel("-40 min"));
		jPan2.add(new JLabel("-Siatka 16x16 pol"));
		jPan2.add(cb[3]);
		jPan2.add(new JLabel("-99 min"));
		jPan2.add(new JLabel("-Siatka 16x30 pol"));
	}
	
	private void ustawPanel3() {
		
		jPan3.setLayout(new BoxLayout(jPan3,BoxLayout.Y_AXIS));	
		tf1.setPreferredSize(new Dimension(10,10));
		tf1.setEditable(false);
		tf2.setEditable(false);
		tf3.setEditable(false);
		tf1.setFocusable(true);
		tf3.addActionListener(new DoNiestadardowego());
		jl.setVisible(false);
		jPan3.add(cb[0]);
		jPan3.add(new JLabel("Wysokosc (9-24)"));
		jPan3.add(tf1);
		jPan3.add(new JLabel("Szerokość (9-30)"));
		jPan3.add(tf2);
		jPan3.add(new JLabel("Miny (10-668)"));
		jPan3.add(tf3);	
		jPan3.add(jl);
	}
	
private void ustawPanel4() {
		
		jb = new JButton("OK");
		jb2 = new JButton("Anuluj");
		jb.addActionListener(this);
		jb2.addActionListener(this);
		jPan4.add(jb);
		jPan4.add(jb2);	
	}

 @Override
public void actionPerformed(ActionEvent e) {
	
	if(e.getSource()==jb) {
		if(ileNowyWysokosc>24|| ileNowyWysokosc<9 ) {
			jl.setVisible(true);
			jl.setText("Wysoko�� z poza zakresu");
		}
		else if(ileNowySzerokosc>30|| ileNowySzerokosc<9 ){
			jl.setVisible(true);
			jl.setText("Szeroko�� z poza zakresu");
		}
		else if(ileNowyMin<10|| ileNowyMin>668|| ileNowyMin>ileNowySzerokosc*ileNowyWysokosc){
			jl.setVisible(true);
			jl.setText("Ilo�� min z poza zakresu");
		}
		else {
		Saper sap = new Saper(ileNowyWysokosc,ileNowySzerokosc,ileNowyMin);
		sap.doDziela();
		Saper.zapisz(sap);
		jf.setVisible(false);
		jf =null;
		this.dispose();
		}
 }
	else 
		this.dispose();	
}	

	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getStateChange()==ItemEvent.SELECTED) 
			for(int i = 0;i<cb.length;i++)
			if(e.getSource()==cb[i]) {
				odznacz(i);
				ustawNowe(i);
				return;
			}		
	}
	private void odznacz(int index) {
		for(int i =0; i < cb.length; i++) {
			if(i==index) continue;
			cb[i].setSelected(false);
		}		
	}
	private void ustawNowe(int index) {
		
		if(index==0) {
			tf1.setFocusable(true);
			tf1.setEditable(true);
			tf2.setEditable(true);
			tf3.setEditable(true);
			
		}
		if(index == 1) {
			tf1.setEditable(false);
			tf2.setEditable(false);
			tf3.setEditable(false);
			ileNowyMin = 10;
			ileNowyWysokosc =9;
			ileNowySzerokosc = 9;
		}
		if(index == 2) {
			tf1.setEditable(false);
			tf2.setEditable(false);
			tf3.setEditable(false);
			ileNowyMin = 40;
			ileNowyWysokosc =16;
			ileNowySzerokosc = 16;
		}
		if(index == 3) {
			tf1.setEditable(false);
			tf2.setEditable(false);
			tf3.setEditable(false);
			ileNowyMin = 99;
			ileNowyWysokosc =16;
			ileNowySzerokosc = 30;
		}
	}
	class DoNiestadardowego implements ActionListener,Serializable{
	@Override public void actionPerformed(ActionEvent e) {
		
		try {
			
		 ileNowyWysokosc = Integer.parseInt(tf1.getText());
			 ileNowySzerokosc = Integer.parseInt(tf2.getText());
			 ileNowyMin = Integer.parseInt(tf3.getText());

		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		
		
	}
	}

}
