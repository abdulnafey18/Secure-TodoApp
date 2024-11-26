package controller;

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
import java.sql.SQLException;
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

        // Check if any fields are empty
        if (name.isBlank() || password1.isBlank() || password2.isBlank() || email.isBlank()) {
            System.out.println("Fill in all Entries");
            labelFeedback.setText("Fill in all fields");
            return;
        }

        // Validate the password
        PasswordFactory passwordFactory = new PasswordFactory();
        String p = passwordFactory.confirmPassworValid(password1, password2);
        System.out.println(p);
        labelFeedback.setText(p);

        if (p.equals("OK")) {
            // Hash the password before storing it
            String hashedPassword = Encryption.hashPassword(password1);

            // Create the user object and store it in the database
            User u = new User();
            UserDAOImpl dbu = new UserDAOImpl();
            u.setName(name);
            u.setPassword(hashedPassword); // Store the hashed password
            u.setEmail(email);
            LogginUser.setUser(u);

            dbu.create(u);

            // Navigate to the login screen
            String dest = "/view/login-view.fxml";
            MainApplication.navigateTo(anchorPaneRegister, dest);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
