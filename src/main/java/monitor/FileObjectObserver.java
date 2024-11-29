package monitor;

import controller.MainApplication;
import model.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.Date;

public class FileObjectObserver implements Observer {
    String file = "monitor.txt";


    @Override
    public void update(Log log) throws SQLException, IOException {

        try (FileOutputStream fout = new FileOutputStream(file, true);
             ObjectOutputStream out = new ObjectOutputStream(fout)) {
            out.writeObject(log);
            out.write('\n');
            out.flush();
            System.out.println("saved log to monitor.txt : " + log.toString());
        } catch (IOException e) {
            MainApplication.logData(new Log(0,"WARN",e.getMessage(),new Date().toString()));
        }
    }

}