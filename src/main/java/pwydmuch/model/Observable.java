package pwydmuch.model;

public interface Observable {
	 void addObserver(Observer ob);//TODO remove??
	 void notifyObservers();
}
