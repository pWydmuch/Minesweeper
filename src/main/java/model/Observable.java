package model;

public interface Observable {
	
	 void addObserver(Observer ob);
	 void removeObserver(Observer ob);
	 void notifyObservers();
	
}
