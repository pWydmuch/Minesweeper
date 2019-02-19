package saper.subject;
import saper.obserwator.Obserwator;

public interface Subject {
	
	 void addObserver(Obserwator ob);
	 void removeObserver(Obserwator ob);
	 void notifyObservers();
	
}
