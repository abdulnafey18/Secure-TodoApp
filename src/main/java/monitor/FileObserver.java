package monitor;

import controller.MainApplication;
import model.Log;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class FileObserver implements Observer {
    String file = "log-file.txt";

    @Override
    public void update(Log log) throws IOException {
        FileWriter fileWriter = null;
        try {
            System.out.println("write log to log-file.txt  "+log.toString());
            fileWriter = new FileWriter(file,true);
            System.out.println(log.toString());
            fileWriter.write(log.toString());
            fileWriter.write('\n');
            fileWriter.flush();
            fileWriter.close();


        } catch (Exception e) {
            MainApplication.logData(new Log(0,"WARN",e.getMessage(),new Date().toString()));
            try {
                if (fileWriter != null) {
                    fileWriter.flush();
                    fileWriter.close();

                }
            } catch (IOException ex) {
                MainApplication.logData(new Log(0,"WARN",ex.getMessage(),new Date().toString()));
                e.printStackTrace();
            }
            e.printStackTrace();
        }finally {

            System.out.println("logs Written to log-file.txt");
            try {
                throw new MyCustomException("problem here");
            } catch (MyCustomException e) {
                System.out.println(e.getMessage());
            }
        }

    }

}