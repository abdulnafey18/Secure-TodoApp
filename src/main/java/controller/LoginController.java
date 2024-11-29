package controller;

import database.Database;
import database.UserDAOImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.Log;
import model.LogginUser;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
    private static final int MAX_ATTEMPTS = 3; // Max incorrect attempts
    private Map<String, Integer> failedAttempts = new HashMap<>(); // Track failed attempts per user

    @FXML
    void onLoginButtonClicked(ActionEvent event) throws IOException, SQLException {
        String name = textUserName.getText().trim();
        String password = textUserPassword.getText().trim();

        if (name.isEmpty() || password.isEmpty()) {
            // Show alert for missing fields
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Login Error");
            alert.setHeaderText("Fields Missing");
            alert.setContentText("Please enter both username and password.");
            alert.showAndWait();
            return;
        }

        Connection con = Database.getConnection();

        // Query to retrieve the user
        String query = "SELECT * FROM user WHERE name = '" + name + "'";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        if (rs.next()) {
            // Map the result set to a User object
            User user = new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("role"),
                    rs.getString("lock")
            );

            if (user.getLock() == User.LOCK.LOCKED) {
                // Show error alert for locked account
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Account Locked");
                alert.setHeaderText("Your account is locked.");
                alert.setContentText("Please contact the administrator.");
                alert.showAndWait();
                return;
            }

            if (user.getPassword().equals(password)) {
                // Successful login
                LogginUser.setUser(user);
                failedAttempts.remove(name); // Reset failed attempts on successful login

                // Check user role and navigate accordingly
                String dest;
                switch (user.getRole()) {
                    case ADMIN:
                        dest = "/view/Admin-view.fxml"; // Admin view
                        break;
                    case USER:
                        dest = "/view/Todo-view.fxml"; // Normal user view
                        break;
                    default:
                        dest = "/view/Todo-view.fxml"; // Default to normal user view
                        break;
                }

                MainApplication.navigateTo(anchorPaneLogin, dest);
            } else {
                handleFailedAttempt(name, stmt, rs); // Handle incorrect password
            }
        } else {
            handleFailedAttempt(name, stmt, rs); // Handle unknown user
        }

        Database.closeResultSet(rs);
        Database.closeStatement(stmt);
        Database.closeConnection(con);
    }

    private void handleFailedAttempt(String name, Statement stmt, ResultSet rs) throws SQLException {
        int attempts = failedAttempts.getOrDefault(name, 0) + 1;
        failedAttempts.put(name, attempts);

        if (attempts >= MAX_ATTEMPTS) {
            // Lock the user account
            String lockQuery = "UPDATE user SET lock = 'LOCKED' WHERE name = '" + name + "'";
            stmt.executeUpdate(lockQuery);

            // Log the lock event
            MainApplication.logData(new Log(0, "WARN", "User " + name + " was locked due to too many failed login attempts.", new Date().toString()));

            // Show account locked alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Account Locked");
            alert.setHeaderText("Too Many Failed Attempts");
            alert.setContentText("Your account has been locked. Please contact the administrator.");
            alert.showAndWait();
        } else {
            // Show error alert for incorrect credentials
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText("Invalid Username or Password");
            alert.setContentText("Attempts remaining: " + (MAX_ATTEMPTS - attempts));
            alert.showAndWait();
        }
    }

    @FXML
    void onRegisterButtonClicked(ActionEvent event) throws IOException {
        String dest = "/view/register-view.fxml";
        MainApplication.navigateTo(anchorPaneLogin, dest);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        db = new UserDAOImpl();
        User currentUser = LogginUser.getUser();

        if (currentUser != null) {
            textUserName.setText(currentUser.getName());
            textUserPassword.setText(currentUser.getPassword());
        }
    }
}