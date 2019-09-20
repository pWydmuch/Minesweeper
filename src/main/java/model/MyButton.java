package model;

import view.MainView;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;

public class MyButton extends JButton implements Observer, Serializable, Subject   {
	
    private int minesAroundNumber;
	private int clicksNumber;
	private MainView main;
	private ArrayList<Observer> observers;
//-----------------------------------------------------		
	public MyButton(MainView mainView){
		this.main = mainView;
		observers = new ArrayList<Observer>();
	}
//------------------------------------------------------------------------------	
	public int getMinesAroundNumber() {
		return minesAroundNumber;
	}
	
	public int getClicksNumber() {
		return clicksNumber;
	}
	
	public void incrementClickNumber() {
		clicksNumber++;
		
	}
//-------------------------------------------------------------------------------------------------------------------	
	public void countMinesAround(int row, int column, int minesNumber , Draw draw, MyButton[][] myButtons) {
		
		for (int i = 0; i < minesNumber; i++) {
			if(row - 1 >= 0)
				if(myButtons[row-1][column]== myButtons[draw.getX(i)][draw.getY(i)]) minesAroundNumber++;
			
			if(column-1 >= 0)
				if(myButtons[row][column-1]== myButtons[draw.getX(i)][draw.getY(i)]) minesAroundNumber++;
			
			if(column+1 < myButtons[0].length)
				if(myButtons[row][column+1]== myButtons[draw.getX(i)][draw.getY(i)]) minesAroundNumber++;
			
			if(row+1 < myButtons.length)
				if(myButtons[row+1][column]== myButtons[draw.getX(i)][draw.getY(i)]) minesAroundNumber++;
			
			if(row-1 >= 0 && column-1 >= 0)
				if(myButtons[row-1][column-1]== myButtons[draw.getX(i)][draw.getY(i)]) minesAroundNumber++;
			
			if(row-1 >= 0 && column+1 < myButtons[0].length)
				if(myButtons[row-1][column+1]== myButtons[draw.getX(i)][draw.getY(i)]) minesAroundNumber++;
			
			if(row+1 < myButtons.length && column-1 >= 0)
				if(myButtons[row+1][column-1]== myButtons[draw.getX(i)][draw.getY(i)]) minesAroundNumber++;
			
			if(row+1 < myButtons.length && column+1 < myButtons[0].length)
				if(myButtons[row+1][column+1]== myButtons[draw.getX(i)][draw.getY(i)]) minesAroundNumber++;
		}	
	}
//----------------------------------------------------------------------------------------------
	
	
	@Override public void addObserver(Observer ob) {
		observers.add(ob);
	}
	
	@Override public void notifyObservers() {
		for(int i = 0; i< observers.size(); i++) {
			observers.get(i).update();
		}}		
	@Override public void removeObserver(Observer ob) {};
//-----------------------------------------------------------------------------	
	public void addObservers(int row, int column , MyButton[][] jb) {
		
			if(row - 1 >= 0) 								addObserver(jb[row-1][column]);
			if(column-1 >= 0) 									addObserver(jb[row][column-1]);
			if(column+1 < jb[0].length) 						addObserver(jb[row][column+1]);
			if(row+1 < jb.length) 							addObserver(jb[row+1][column]);
			if(row-1 >= 0 && column-1 >= 0) 				addObserver(jb[row-1][column-1]);
			if(row-1 >= 0 && column+1 < jb[0].length) 		addObserver(jb[row-1][column+1]);
			if(row+1 < jb.length && column-1 >= 0) 			addObserver(jb[row+1][column-1]);
			if(row+1 < jb.length && column+1 < jb[0].length)addObserver(jb[row+1][column+1]);
	}
	
//-----------------------------------------------------------------------------------	
	@Override public void update() {
		if(isEnabled()==true ) {
			if(getIcon()!= main.getFlag() && getIcon()!= main.getQuestionMark()) {
			setEnabled(false);	
			if(minesAroundNumber ==0) {
				notifyObservers();	
			}
			else {
				setIcon(main.getMap().get(minesAroundNumber));
				setDisabledIcon(main.getMap().get(minesAroundNumber));	}}}}
}


