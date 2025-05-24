package modelo;

import java.util.ArrayList;
import java.util.List;

public class Observable<T> {
	 private final List<Observer<T>> observers = new ArrayList<>();

	    public synchronized void addObserver(Observer<T> observer) {
	        observers.add(observer);
	    }

	    public synchronized void removeObserver(Observer<T> observer) {
	        observers.remove(observer);
	    }

	    public synchronized void notifyObservers(T data) {
	        List<Observer<T>> copy = new ArrayList<>(observers);
	        for (Observer<T> observer : copy) {
	            observer.update(data);
	        }
	    }
}