module blogApp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jdk.httpserver;
    requires okhttp3;


    exports controller;
    opens controller to javafx.fxml;
}