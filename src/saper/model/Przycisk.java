package saper.model;

import saper.subject.Subject;
import saper.obserwator.Obserwator;
import saper.view.Saper;
import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.*;

public class Przycisk extends JButton implements Obserwator, Serializable, Subject   {
	
    private int ileMinWokol;
	private int ileKlikniec;
	private Saper sap;
	private ArrayList<Obserwator> obserwatorzy;
//-----------------------------------------------------		
	public Przycisk(Saper sap){
		this.sap = sap;
		obserwatorzy = new ArrayList<Obserwator>();
	}
//------------------------------------------------------------------------------	
	public int getIleMinWokol() {
		return ileMinWokol;
	}
	
	public int getIlekliniec() {
		return ileKlikniec;
	}
	
	public void inkrementujIloscKlikniec() {
		ileKlikniec++;
		
	}
//-------------------------------------------------------------------------------------------------------------------	
	public void liczMinyWokol(int wiersz, int kolumna, int ileMin , MaszynaLosujaca losowanie, Przycisk[][] jb) {
		
		for (int i = 0; i < ileMin; i++) {
			if(wiersz - 1 >= 0) 
				if(jb[wiersz-1][kolumna]== jb[losowanie.getX(i)][losowanie.getY(i)]) ileMinWokol++;
			
			if(kolumna-1 >= 0) 
				if(jb[wiersz][kolumna-1]== jb[losowanie.getX(i)][losowanie.getY(i)]) ileMinWokol++;
			
			if(kolumna+1 < jb[0].length) 
				if(jb[wiersz][kolumna+1]== jb[losowanie.getX(i)][losowanie.getY(i)]) ileMinWokol++;
			
			if(wiersz+1 < jb.length) 
				if(jb[wiersz+1][kolumna]== jb[losowanie.getX(i)][losowanie.getY(i)]) ileMinWokol++;
			
			if(wiersz-1 >= 0 && kolumna-1 >= 0) 
				if(jb[wiersz-1][kolumna-1]== jb[losowanie.getX(i)][losowanie.getY(i)]) ileMinWokol++;
			
			if(wiersz-1 >= 0 && kolumna+1 < jb[0].length) 
				if(jb[wiersz-1][kolumna+1]== jb[losowanie.getX(i)][losowanie.getY(i)]) ileMinWokol++;
			
			if(wiersz+1 < jb.length && kolumna-1 >= 0) 
				if(jb[wiersz+1][kolumna-1]== jb[losowanie.getX(i)][losowanie.getY(i)]) ileMinWokol++;
			
			if(wiersz+1 < jb.length && kolumna+1 < jb[0].length) 
				if(jb[wiersz+1][kolumna+1]== jb[losowanie.getX(i)][losowanie.getY(i)]) ileMinWokol++;		
		}	
	}
//----------------------------------------------------------------------------------------------
	
	
	@Override public void addObserver(Obserwator ob) {
		obserwatorzy.add(ob);
	}
	
	@Override public void notifyObservers() {
		for(int i =0;i<obserwatorzy.size();i++) {
			obserwatorzy.get(i).update();
		}}		
	@Override public void removeObserver(Obserwator ob) {};
//-----------------------------------------------------------------------------	
	public void  dodajObserwatorow(int wiersz, int kolumna ,Przycisk[][] jb) {
		
			if(wiersz - 1 >= 0) 								addObserver(jb[wiersz-1][kolumna]);			
			if(kolumna-1 >= 0) 									addObserver(jb[wiersz][kolumna-1]);		
			if(kolumna+1 < jb[0].length) 						addObserver(jb[wiersz][kolumna+1]);
			if(wiersz+1 < jb.length) 							addObserver(jb[wiersz+1][kolumna]);				
			if(wiersz-1 >= 0 && kolumna-1 >= 0) 				addObserver(jb[wiersz-1][kolumna-1]);			
			if(wiersz-1 >= 0 && kolumna+1 < jb[0].length) 		addObserver(jb[wiersz-1][kolumna+1]);							
			if(wiersz+1 < jb.length && kolumna-1 >= 0) 			addObserver(jb[wiersz+1][kolumna-1]);							
			if(wiersz+1 < jb.length && kolumna+1 < jb[0].length)addObserver(jb[wiersz+1][kolumna+1]);					
	}
	
//-----------------------------------------------------------------------------------	
	@Override public void update() {
		if(isEnabled()==true ) {
			if(getIcon()!= sap.getFlaga() && getIcon()!=sap.getZnak()) {
			setEnabled(false);	
			if(ileMinWokol==0) {
				notifyObservers();	
			}
			else {
				setIcon(sap.getMap().get(ileMinWokol));
				setDisabledIcon(sap.getMap().get(ileMinWokol));	}}}}		
}


