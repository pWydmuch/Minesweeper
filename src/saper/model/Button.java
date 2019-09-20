package saper.model;

import saper.view.MainView;

import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.*;

public class Button extends JButton implements Observer, Serializable, Subject   {
	
    private int minesAroundNumber;
	private int clicksNumber;
	private MainView main;
	private ArrayList<Observer> observers;
//-----------------------------------------------------		
	public Button(MainView mainView){
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
	public void countMinesAround(int row, int column, int minesNumber , Draw draw, Button[][] buttons) {
		
		for (int i = 0; i < minesNumber; i++) {
			if(row - 1 >= 0)
				if(buttons[row-1][column]== buttons[draw.getX(i)][draw.getY(i)]) minesAroundNumber++;
			
			if(column-1 >= 0)
				if(buttons[row][column-1]== buttons[draw.getX(i)][draw.getY(i)]) minesAroundNumber++;
			
			if(column+1 < buttons[0].length)
				if(buttons[row][column+1]== buttons[draw.getX(i)][draw.getY(i)]) minesAroundNumber++;
			
			if(row+1 < buttons.length)
				if(buttons[row+1][column]== buttons[draw.getX(i)][draw.getY(i)]) minesAroundNumber++;
			
			if(row-1 >= 0 && column-1 >= 0)
				if(buttons[row-1][column-1]== buttons[draw.getX(i)][draw.getY(i)]) minesAroundNumber++;
			
			if(row-1 >= 0 && column+1 < buttons[0].length)
				if(buttons[row-1][column+1]== buttons[draw.getX(i)][draw.getY(i)]) minesAroundNumber++;
			
			if(row+1 < buttons.length && column-1 >= 0)
				if(buttons[row+1][column-1]== buttons[draw.getX(i)][draw.getY(i)]) minesAroundNumber++;
			
			if(row+1 < buttons.length && column+1 < buttons[0].length)
				if(buttons[row+1][column+1]== buttons[draw.getX(i)][draw.getY(i)]) minesAroundNumber++;
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
	public void addObservers(int row, int column , Button[][] jb) {
		
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


