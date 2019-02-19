package saper.model;



import java.io.Serializable;
import java.util.Random;

import javax.swing.ImageIcon;

public class MaszynaLosujaca implements Serializable {
	private Random generator;
	private boolean czyPowtorka;
	private int ileMin;
	private int[] x;
	private int[] y;
	
//---------------------------------------------------------------------------	
	public MaszynaLosujaca(int ileMin) {
		
		this.ileMin = ileMin;
		generator = new Random();
		czyPowtorka = false;
		x = new int[ileMin];
		y = new int[ileMin];
	}
//------------------------------------------------------------------------------	
	public int getX (int i) {
		return x[i];
	}	
	public int getY (int i) {
		return y[i];
	}
//-------------------------------------------------------------------------------------	
	private void sprawdz(int i, int liczba1, int liczba2) {

		if (i != 0) {
			for (int j = 0; j < i; j++) {
				if (x[j] == liczba1 && y[j] == liczba2) {
					czyPowtorka = true;
					return;				}}}
	}

	public void losuj(int ile, int wiersze, int kolumny) {
		for (int i = 0; i < ile; i++) {
			do {
				x[i] = generator.nextInt(wiersze);
				y[i] = generator.nextInt(kolumny);
				sprawdz(i, x[i], y[i]);
				if (!czyPowtorka) break;					
				czyPowtorka = false;
			} while (true);
			czyPowtorka = false; 		}}
	
	public boolean sprawdzCzyWygrana(Przycisk[][] jb,  ImageIcon flaga) {
		int liczFlagi = 0;
		for (int i = 0; i < jb.length; i++) {
			for (int j = 0; j < jb[i].length; j++) {
				if(jb[i][j].getIcon()==flaga && sprawdzCzyJestBomba(i, j)) 
					 liczFlagi++;
			}
	}
	return liczFlagi == ileMin && sprawdzCzyInneSaDisabled(jb);
//		if(liczFlagi == ileMin && sprawdzCzyInneSaDisabled(jb)) return true;
//		else return false;
	}
	
	private boolean sprawdzCzyJestBomba(int a, int b) {
		for(int i=0; i<ileMin; i++) {
			if(x[i] == a && y[i]== b) return true;
		}
		return false;
	}
	
	private boolean sprawdzCzyInneSaDisabled(Przycisk[][] jb) {
		for (int i = 0; i < jb.length; i++) {
			for (int j = 0; j < jb[i].length; j++) {	
				if(!sprawdzCzyJestBomba(i, j) && jb[i][j].isEnabled())
				return false;
			}
		}
		return true;
	}
}

