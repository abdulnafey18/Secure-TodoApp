package monitor;

import model.Log;

import java.io.IOException;
import java.sql.SQLException;

public interface Subject {
    public void subscribe(Observer observer);
    public void unSubscribe(Observer observer);
    public void notifyObservers(Log log) throws SQLException, IOException;
}
