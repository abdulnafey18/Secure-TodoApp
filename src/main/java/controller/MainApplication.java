package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Log;
import model.LogginUser;
import model.User;
import monitor.*;
import monitor.Misc.Cabbage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

public class MainApplication extends Application {
    static Subject logSubject = null;

    @Override
    public void start(Stage stage) throws IOException {
        // Initialize application logic
        Cabbage.setSprouts();
        setUpObserver();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/view/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Todo App");
        stage.setScene(scene);
        stage.show();

        // Add close request handler
        stage.setOnCloseRequest(event -> {
            event.consume();
            logOut(stage);
        });
    }

    public void logOut(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You are about to Exit");
        alert.setContentText("");
        if (alert.showAndWait().get() == ButtonType.OK) {
            System.out.println("Exit program ");
            LogginUser.setUser(new User());
            stage.close();
        }
    }

    public static void navigateTo(AnchorPane pane, String page) throws IOException {
        Stage stage = (Stage) pane.getScene().getWindow();
        Parent root = FXMLLoader.load(MainApplication.class.getResource(page));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void setUpObserver() {
        Observer observer1 = new FileObserver();
        Observer observer2 = new FileObjectObserver();
        Observer observer3 = new DatabaseObserver();
        logSubject = new LogSubject(new Log(0, "INFO", "log message 1", new Date().toString()));
        logSubject.subscribe(observer1);
        logSubject.subscribe(observer2);
        logSubject.subscribe(observer3);
    }

    public static void logData(Log log) {
        try {
            logSubject.notifyObservers(log);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        try {
            // Start the Jetty server in a separate thread
            new Thread(() -> {
                try {
                    server.ServerInitializer.startServer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            // Launch the JavaFX application
            launch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}