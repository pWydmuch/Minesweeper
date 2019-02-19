package saper.model;
import saper.model.Obserwator;

public interface Subject {
	
	 void addObserver(Obserwator ob);
	 void removeObserver(Obserwator ob);
	 void notifyObservers();
	
}
