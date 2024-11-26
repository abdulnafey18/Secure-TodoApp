package controller;

import database.UserDAOImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import model.LogginUser;
import model.User;
import security.Encryption;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private AnchorPane anchorPaneLogin;

    @FXML
    private Button btnRegisterNewUser;

    @FXML
    private Button btnSubmitLogin;

    @FXML
    private TextField textUserName;

    @FXML
    private TextField textUserPassword;

    UserDAOImpl db;

    @FXML
    void onLoginButtonClicked(ActionEvent event) throws IOException, SQLException {
        String name = textUserName.getText().trim();
        String plainPassword = textUserPassword.getText().trim();

        if (name.isEmpty() || plainPassword.isEmpty()) {
            // Show an alert for empty fields
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText("Missing Fields");
            alert.setContentText("Please enter both username and password.");
            alert.showAndWait();
            return;
        }

        // Retrieve the user from the database
        User user = db.readLoggedInUser(name, plainPassword); // Pass both username and password

        if (user == null) {
            // Show an alert for incorrect username or password
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText("Invalid Username or Password");
            alert.setContentText("Please try again.");
            alert.showAndWait();
            return;
        }

        // Verify the password securely
        if (!Encryption.verifyPassword(plainPassword, user.getPassword())) {
            // Passwords do not match
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText("Invalid Username or Password");
            alert.setContentText("Please try again.");
            alert.showAndWait();
            return;
        }

        // Password verified, set the logged-in user and navigate
        LogginUser.setUser(user);
        String dest = "/view/Todo-view.fxml";
        MainApplication.navigateTo(anchorPaneLogin, dest);
    }

    @FXML
    void onRegisterButtonClicked(ActionEvent event) throws IOException {
        String dest = "/view/register-view.fxml";
        MainApplication.navigateTo(anchorPaneLogin,dest);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        db = new UserDAOImpl();
        User currentUser = LogginUser.getUser();

        if(currentUser !=null){
            textUserName.setText(currentUser.getName());
            textUserPassword.setText(currentUser.getPassword());
        }

    }
}
