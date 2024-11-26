module blogApp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jdk.httpserver;
    requires okhttp3;
    requires jbcrypt;


    exports controller;
    opens controller to javafx.fxml;
}