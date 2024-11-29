package controller;

import controller.MainApplication;
import database.Database;
import database.UserDAOImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.LogginUser;
import model.User;
import register.PasswordFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML
    private AnchorPane anchorPaneRegister;

    @FXML
    private Button btnRegister;

    @FXML
    private Label labelFeedback;

    @FXML
    private TextField textConfirmPassword;

    @FXML
    private TextField textEmail;

    @FXML
    private TextField textName;

    @FXML
    private TextField textPassword;

    @FXML
    void onRegisterUser(ActionEvent event) throws IOException, SQLException {
        String name = textName.getText().trim();
        String password1 = textPassword.getText().trim();
        String password2 = textConfirmPassword.getText().trim();
        String email = textEmail.getText().trim();

        if (name.isBlank() || password1.isBlank() || password2.isBlank() || email.isBlank()) {
            // Reflecting user inputs in feedback message
            labelFeedback.setText("Fill in all fields. Name entered: " + name + ", Email entered: " + email);
            return;
        }

        if (!password1.equals(password2)) {
            // Reflecting user inputs in error message
            labelFeedback.setText("Passwords do not match for user: " + name + " with email: " + email);
            return;
        }

        Connection con = Database.getConnection();

        // Insecure query with concatenated user inputs
        String query = "INSERT INTO user (name, password, email) VALUES ('" +
                name + "', '" + password1 + "', '" + email + "')";
        Statement stmt = con.createStatement();
        stmt.executeUpdate(query);

        // Insecure: Exposing sensitive data in feedback
        labelFeedback.setText("User registered with Name=" + name + " and Password=" + password1);

        Database.closeStatement(stmt);
        Database.closeConnection(con);

        String dest = "/view/login-view.fxml";
        MainApplication.navigateTo(anchorPaneRegister, dest);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
