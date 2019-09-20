package model;


import javax.swing.*;
import java.io.Serializable;
import java.util.Random;

public class Draw implements Serializable {
	private Random generator;
	private boolean isRepeat;
	private int minesNumber;
	private int[] x;
	private int[] y;
	
//---------------------------------------------------------------------------	
	public Draw(int minesNumber) {
		
		this.minesNumber = minesNumber;
		generator = new Random();
		isRepeat = false;
		x = new int[minesNumber];
		y = new int[minesNumber];
	}
//------------------------------------------------------------------------------	
	public int getX (int i) {
		return x[i];
	}	
	public int getY (int i) {
		return y[i];
	}
//-------------------------------------------------------------------------------------	
	private void check(int i, int number1, int number2) {

		if (i != 0) {
			for (int j = 0; j < i; j++) {
				if (x[j] == number1 && y[j] == number2) {
					isRepeat = true;
					return;				}}}
	}

	public void makeDraw(int ile, int rows, int columns) {
		for (int i = 0; i < ile; i++) {
			do {
				x[i] = generator.nextInt(rows);
				y[i] = generator.nextInt(columns);
				check(i, x[i], y[i]);
				if (!isRepeat) break;
				isRepeat = false;
			} while (true);
			isRepeat = false; 		}}
	
	public boolean isSuccess(MyButton[][] jb, ImageIcon flag) {
		int flagsNumbers = 0;
		for (int i = 0; i < jb.length; i++) {
			for (int j = 0; j < jb[i].length; j++) {
				if(jb[i][j].getIcon()==flag && checkIfBomb(i, j))
					 flagsNumbers++;
			}
	}
	return flagsNumbers == minesNumber && checkOthersDisabled(jb);
//		if(liczFlagi == minesNumber && checkOthersDisabled(jb)) return true;
//		else return false;
	}
	
	private boolean checkIfBomb(int a, int b) {
		for(int i = 0; i< minesNumber; i++) {
			if(x[i] == a && y[i]== b) return true;
		}
		return false;
	}
	
	private boolean checkOthersDisabled(MyButton[][] jb) {
		for (int i = 0; i < jb.length; i++) {
			for (int j = 0; j < jb[i].length; j++) {	
				if(!checkIfBomb(i, j) && jb[i][j].isEnabled())
				return false;
			}
		}
		return true;
	}
}

