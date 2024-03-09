package pwydmuch.model;

public interface Observable {
	 void addObserver(Observer ob);
	 void notifyObservers();
}
