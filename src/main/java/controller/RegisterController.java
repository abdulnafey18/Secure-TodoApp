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
import security.Encryption;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
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

        // Validate if fields are empty
        if (name.isBlank() || password1.isBlank() || password2.isBlank() || email.isBlank()) {
            labelFeedback.setText("All fields are required.");
            return;
        }

        // Validate if passwords match
        if (!password1.equals(password2)) {
            labelFeedback.setText("Passwords do not match.");
            return;
        }

        // Validate password strength
        PasswordFactory passwordFactory = new PasswordFactory();
        String validationMessage = passwordFactory.confirmPassworValid(password1, password2);
        System.out.println(validationMessage);
        labelFeedback.setText(validationMessage);

        // Proceed only if the password passes all checks
        if (!validationMessage.equals("OK")) {
            return;
        }

        // Hash the password and store it securely
        String hashedPassword = Encryption.hashPassword(password1);

        // Insert the user into the database
        Connection con = Database.getConnection();
        String query = "INSERT INTO user (name, password, email) VALUES (?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, name);
            ps.setString(2, hashedPassword);
            ps.setString(3, email);
            ps.executeUpdate();
            labelFeedback.setText("User registered successfully!");
            // Navigate to the login page
            String dest = "/view/login-view.fxml";
            MainApplication.navigateTo(anchorPaneRegister, dest);
        } catch (SQLException e) {
            labelFeedback.setText("Error: Unable to register user.");
            e.printStackTrace();
        } finally {
            Database.closeConnection(con);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
