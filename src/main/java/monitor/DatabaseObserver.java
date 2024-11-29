package monitor;

import database.LogDAOImpl;
import model.Log;

import java.sql.SQLException;


public class DatabaseObserver implements Observer {
    LogDAOImpl dbLog;
    @Override
    public void update(Log log) throws SQLException {
        dbLog = new LogDAOImpl();
        dbLog.create(log);
        System.out.println("Save to database :"+ log.toString());

    }
}
