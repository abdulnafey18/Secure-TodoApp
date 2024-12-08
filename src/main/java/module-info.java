module TodoApp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jdk.httpserver;
    requires okhttp3;
    requires org.eclipse.jetty.server;
    requires org.eclipse.jetty.servlet;
    requires jbcrypt;
    requires javax.servlet.api;
    exports model;
    exports database;
    exports register;
    exports controller;

    opens controller to javafx.fxml;
    opens model to javafx.fxml;
    opens register to javafx.fxml;
    opens database to javafx.fxml;
}