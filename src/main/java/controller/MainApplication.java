package controller;

import monitor.Misc.Cabbage;
import monitor.LogSubject;
import monitor.DatabaseObserver;
import monitor.FileObserver;
import model.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

@SpringBootApplication
public class MainApplication {

    private static LogSubject logSubject;

    public static void main(String[] args) {
        // Set up observers and initial database state
        setUpObservers();
        initializeDatabase();

        // Log application startup
        logData(new Log("INFO", "Application started successfully", new Date().toString()));

        // Run the Spring Boot application
        SpringApplication.run(MainApplication.class, args);
    }

    private static void initializeDatabase() {
        try {
            Cabbage.setSprouts(); // Initializes the database and default users
        } catch (Exception e) {
            logData(new Log("ERROR", "Database initialization failed: " + e.getMessage(), new Date().toString()));
        }
    }

    private static void setUpObservers() {
        logSubject = new LogSubject(new Log("INFO", "LogSubject Initialized", new Date().toString()));

        // Add observers for database and file logging
        logSubject.subscribe(new DatabaseObserver());
        logSubject.subscribe(new FileObserver());
    }

    public static void logData(Log log) {
        try {
            logSubject.notifyObservers(log);
        } catch (Exception e) {
            System.err.println("Error logging data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}