package monitor;

import model.Log;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;


public class LogSubject implements Subject {
    private ArrayList<Observer> subscribers;

    public LogSubject(Log log){
        subscribers = new ArrayList<Observer>();
    }

    @Override
    public void subscribe(Observer observer) {
        subscribers.add(observer);

    }

    @Override
    public void unSubscribe(Observer observer) {
        subscribers.remove(observer);
    }

    @Override
    public void notifyObservers(Log log) throws SQLException, IOException {
        for(Observer subscriber:subscribers)
            subscriber.update(log);
    }
}