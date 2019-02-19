package saper.view;
import saper.model.Przycisk;
import saper.model.MaszynaLosujaca;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class Saper implements ActionListener,WindowListener, Serializable  {

	
	private static final long serialVersionUID = 1161474195722226307L;
	private Map<Integer,ImageIcon> map;
	private Timer tm;    // SPROBUJ USTAWIC TEN WATEK JAKO DEMON
	private ImageIcon znak;
	private ImageIcon flaga;
	private ImageIcon bomba;
	private ImageIcon klepsydra;
	private JMenuItem nowa,opcje,zakon;
	private Przycisk[][] jb;
	private MaszynaLosujaca losowanie;	
	private PrawyPrzycisk pp;
	private LewyPrzycisk lp;
	private JFrame jf;
	private JPanel jp;
	private int kolumny;
	private int wiersze;
	private int ileMin;
	private int czas;
	private int ileFlag;
	private JLabel zegar;
	private JLabel ileZostalo;
	

			
//-------------------------------------------------------------------------------------
	public Saper(int wiersze, int kolumny, int ileMin) {
		
		
		this.wiersze = wiersze;
		this.kolumny = kolumny;
		this.ileMin = ileMin;
		losowanie = new MaszynaLosujaca(ileMin);
		losowanie.losuj(ileMin, wiersze, kolumny); 	
		jb = new Przycisk[wiersze][kolumny];
		jf = new JFrame();
		jp = new JPanel();
		ileFlag = ileMin;
		ileZostalo = new JLabel(String.valueOf(ileFlag));
		map = new HashMap<Integer,ImageIcon>();
		lp = new LewyPrzycisk();
		pp = new PrawyPrzycisk();
		zegar = new JLabel("0");
		
		try {
		znak = new ImageIcon(new ImageIcon("src/obrazki/znak.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));   
		flaga = new ImageIcon(new ImageIcon("src/obrazki/flag.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		bomba = new ImageIcon(new ImageIcon("src/obrazki/bomb.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		klepsydra = new ImageIcon(new ImageIcon("src/obrazki/hourglass.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		}catch(Exception ex) {
			ex.printStackTrace();
			
		}}
	
	public Saper(int wiersze, int kolumny, int ileMin, MaszynaLosujaca losowanie) {
		this(wiersze, kolumny, ileMin);
		this.losowanie = losowanie;
		
	}
//-----------------------------------------------------------------------	
	public int getWiersze() {
		return wiersze;
	}
	public int getIleMin() {
		return ileMin;
	}
	
	public int getCzas() {
		return czas;
	}
	
	public int getKolumny() {
		return kolumny;
	}
	public JFrame getJf() {
		return jf;
	}
	public Map<Integer,ImageIcon> getMap() {
		return map;
	}
	public ImageIcon getFlaga(){
		return flaga;
	}
	public ImageIcon getZnak(){
		return znak;
	}
	public MaszynaLosujaca getLosowanie() {
		return losowanie;
	}

//-------------------------------------------------------------------------------------------
	public void pokazBomby() {
		for (int i = 0; i < ileMin; i++) {
			jb[losowanie.getX(i)][losowanie.getY(i)].setBackground(Color.GREEN);			
		}
	}
//---------------------------------------------------------------------------	
	
	
	public static void zapisz(Saper sap) {
	
		  /*Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
		        public void run() {
		        	
		        	try(ObjectOutputStream	so1 
		        			= new ObjectOutputStream(new FileOutputStream("Moje.ser"))) {	        		
						so1.writeObject(sap);						
						}catch(Exception e) {
							e.printStackTrace();
						}}
		    }));
		    */
		Runtime.getRuntime().addShutdownHook(new Thread(()-> {
			try(ObjectOutputStream	so1 
        			= new ObjectOutputStream(new FileOutputStream("Moje.ser"))) {	        		
				so1.writeObject(sap);						
				}catch(Exception e) {}}));
		
	}
//------------------------------------------------------------------------------------------------------
	public static void main(String[] args) {
	
		try(ObjectInputStream so =
				 new ObjectInputStream(new FileInputStream("Moje.ser"))) {		 
			 Saper	sap = (Saper) so.readObject();			
			 sap.jf.setVisible(true);		
			}catch(Exception e) {
				 new Saper(13,13,25).doDziela();				
			}
	}
//-------------------------------------------------------------------------------------
	public void doDziela() {	
		wypelnijMap();			
		ustawPrzyciski();
		nadajCechyPrzyciskom();
		ustawRamke();		
		jf.validate(); // jak sie wlacza to od razu jest plansza nie trzeba przesiagac?
		//pokazBomby();

	}
//---------------------------------------------------------------------------------------------
	private void wypelnijMap() {
		for(int i = 1; i<=8;i++) {
			map.put(i,new ImageIcon("src/cyfry/"+String.valueOf(i)+".png"));
			Image ima = map.get(i).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
			map.remove(i);
			map.put(i, new ImageIcon(ima));
		}	
	}
//----------------------------------------------------------------------------------------------------		
	private void ustawRamke() {
		jf.setTitle("Saper");
		jf.setVisible(true);
		jf.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		jf.addWindowListener(this);
		jf.setSize(kolumny*35+70, wiersze*35+150);
		//jf.setLocationRelativeTo(null);
		jf.setIconImage(new ImageIcon("src/obrazki/bomb.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		dodajManuBar();
		JPanel jp = new JPanel();
		JPanel jp2 = new JPanel();
		JPanel jp3 = new JPanel();
		jp.setLayout(new BorderLayout());
		JButton d = new JButton();
		d.setPreferredSize(new Dimension(25, 25));
		d.setIcon(klepsydra);
		d.setDisabledIcon(klepsydra);
		d.setEnabled(false);
		jp2.add(d);
		jp2.add(zegar);
		JButton p = new JButton();
		p.setPreferredSize(new Dimension(25, 25));
		p.setIcon(bomba);
		p.setDisabledIcon(bomba);
		p.setEnabled(false);
		jp3.add(ileZostalo);
		jp3.add(p);
		jp.add(jp2,BorderLayout.WEST);
		jp.add(jp3,BorderLayout.EAST);
		jf.add(jp,BorderLayout.SOUTH);
	}
	
 private void dodajManuBar() {
	JMenuBar jMB = new JMenuBar();
	JMenu gra = new JMenu("Gra");
	nowa = new JMenuItem("Nowa Gra");
	gra.add(nowa);
	nowa.addActionListener(this);	
    opcje = new JMenuItem("Opcje");
	gra.add(opcje);
	opcje.addActionListener(this);
	zakon = new JMenuItem("Zakoñcz");
	gra.add(zakon);
	zakon.addActionListener(this);	
	jMB.add(gra);	
	jf.setJMenuBar(jMB);
	jf.add(jp);
}

 
//----------------------------------------------------------------------------------------------------		
	private void ustawPrzyciski() {		
		int x = 0;
		int y = 0;
		jp.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		for (int i = 0; i < jb.length; i++) {
			for (int j = 0; j < jb[i].length; j++) {				
				jb[i][j] = new Przycisk(this);
				jb[i][j].setPreferredSize(new Dimension(35, 35));
				jb[i][j].addMouseListener(pp);
				jb[i][j].addMouseListener(lp);
				gc.gridx = x;
				gc.gridy = y;
				jp.add(jb[i][j], gc);
				x++;
			}
			x = 0;
			y++;
		}	
	}
	private void nadajCechyPrzyciskom() {
		for (int i = 0; i < jb.length; i++) {
			for (int j = 0; j < jb[i].length; j++) {
				jb[i][j].dodajObserwatorow(i, j, jb);
				jb[i][j].liczMinyWokol(i, j, ileMin, losowanie, jb);  }}        			
	}

//--------------------------------------------------------------------------------
//--------------------------------------------------------------------------------	
	@Override public void actionPerformed(ActionEvent e) {
		JMenuItem i = (JMenuItem)e.getSource();
		
		if(i.getText().equals("Opcje"))
		new OknoZmiany(jf).wyswietlOkno();
		
		if(i.getText().equals("Nowa Gra")) {
			new Saper(wiersze,kolumny,ileMin).doDziela();		
			jf.dispose();
		}
		if(i.getText().equals("Zakoñcz"))
				new OknoZamkniecie(this).wyswietlOkno();		
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		new OknoZamkniecie(this).wyswietlOkno();
		
	}	
	
	@Override public void windowOpened(WindowEvent e) {}
	@Override public void windowClosed(WindowEvent e) {}
	@Override public void windowIconified(WindowEvent e) {}
	@Override public void windowDeiconified(WindowEvent e) {}	
	@Override public void windowActivated(WindowEvent e) {}
	@Override public void windowDeactivated(WindowEvent e) {}
	//-----------------------------------------------------------------------------------------	
	//-----------------------------------------------------------------------------------------		
		class PrawyPrzycisk implements MouseListener, Serializable{
		
		@Override public void mouseClicked(MouseEvent e) {
		
			if (SwingUtilities.isRightMouseButton(e)) {
				
				for (int i = 0; i < jb.length; i++) {
					for (int j = 0; j < jb[i].length; j++) {
						if (e.getSource() == jb[i][j]) {
							jb[i][j].inkrementujIloscKlikniec();
							if (jb[i][j].getIlekliniec() % 3 == 1) {
								jb[i][j].setIcon(flaga);
								ileFlag--;
								ileZostalo.setText(String.valueOf(ileFlag));
								jb[i][j].removeMouseListener(lp);
								return;
							}
							if (jb[i][j].getIlekliniec() % 3 == 2) {
								jb[i][j].setIcon(znak);
								ileFlag++;
								ileZostalo.setText(String.valueOf(ileFlag));
								jb[i][j].addMouseListener(lp);
								return;
							}
							if (jb[i][j].getIlekliniec() % 3 == 0) {
								jb[i][j].setIcon(null);							
								return;
							}}}}}
			if(losowanie.sprawdzCzyWygrana(jb,flaga)&& ileFlag==0) {
				 tm.stop();
				 tm.setDelay(Integer.MAX_VALUE);
				new OknoWygrana(Saper.this).wyswietlOkno();
				
			}
		}
		
		@Override public void mouseReleased(MouseEvent e) {}	
		@Override public void mouseEntered(MouseEvent e) {}	
		@Override public void mouseExited(MouseEvent e) {}	
		@Override public void mousePressed(MouseEvent e) {}
		
		}
	//---------------------------------------------------------------------------------------------------	
		class LewyPrzycisk implements MouseListener , Serializable {
			
		transient private int ileRazyTimerWlaczony = 0;
		
		@Override	public void mouseClicked(MouseEvent e) {
			
			
			
			if (SwingUtilities.isLeftMouseButton(e)) {
				if(ileRazyTimerWlaczony==0) {
				tm = new Timer(1000, e2 -> {
					//zegar.setText("");
				    czas++;
				    zegar.setText(String.valueOf(czas));});
					tm.start();
					ileRazyTimerWlaczony++;
				}
					
					
				for (int j = 0; j < ileMin; j++) {
					
					if (e.getSource() == jb[losowanie.getX(j)][losowanie.getY(j)]) {
						for (int i = 0; i < ileMin; i++) {
						jb[losowanie.getX(i)][losowanie.getY(i)].setIcon(bomba);
						jb[losowanie.getX(i)][losowanie.getY(i)].setBackground(Color.RED);	
						
						}
					new OknoPrzegrana(Saper.this).wyswietlOkno();					
						return;
					}}
				
				for (int i = 0; i < jb.length; i++) {
					for (int j = 0; j < jb[i].length; j++) {
						if(e.getSource() == jb[i][j]) {
							if(jb[i][j].getIleMinWokol()==0) {
								jb[i][j].setIcon(null);
								jb[i][j].setEnabled(false);
								jb[i][j].removeMouseListener(pp);
								jb[i][j].removeMouseListener(lp);						
								jb[i][j].notifyObservers();
								return;
							}
							else {

								jb[i][j].setEnabled(false);
								jb[i][j].setIcon(map.get(jb[i][j].getIleMinWokol()));
								jb[i][j].setDisabledIcon(map.get(jb[i][j].getIleMinWokol()));
								jb[i][j].removeMouseListener(lp);
								jb[i][j].removeMouseListener(pp);
							return;
					}}}}}}
		
		
		@Override public void mouseReleased(MouseEvent e) {}	
		@Override public void mouseEntered(MouseEvent e) {}	
		@Override public void mouseExited(MouseEvent e) {}	
		@Override public void mousePressed(MouseEvent e) {}
		
		}	
	
}

