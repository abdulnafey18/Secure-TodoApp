package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import misc.Cabbage;


import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Cabbage.setSprouts();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/view/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Todo App");
        stage.setScene(scene);
        stage.show();
    }

    public static void navigateTo(AnchorPane pane, String page) throws IOException {
        // String dest = "/view/login-view.fxml";
        Stage stage = (Stage) pane.getScene().getWindow();
        Parent root = FXMLLoader.load(MainApplication.class.getResource(page));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}