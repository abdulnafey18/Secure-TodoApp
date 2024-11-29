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
import security.Encryption;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    private UserDAOImpl db;
    private static final int MAX_ATTEMPTS = 3; // Max incorrect attempts
    private Map<String, Integer> failedAttempts = new HashMap<>(); // Track failed attempts per user

    @FXML
    void onLoginButtonClicked(ActionEvent event) throws IOException, SQLException {
        String name = textUserName.getText().trim();
        String password = textUserPassword.getText().trim();

        if (name.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Login Error", "Fields Missing", "Please enter both username and password.");
            return;
        }

        Connection con = Database.getConnection();

        String query = "SELECT * FROM user WHERE name = ?";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setString(1, name);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            User user = new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("role"),
                    rs.getString("lock")
            );

            if (user.getLock() == User.LOCK.LOCKED) {
                showAlert(Alert.AlertType.ERROR, "Account Locked", "Your account is locked.", "Please contact the administrator.");
                return;
            }

            if (Encryption.verifyPassword(password, user.getPassword())) {
                LogginUser.setUser(user);
                failedAttempts.remove(name);
                navigateToRoleView(user.getRole());
            } else {
                handleFailedAttempt(name, con); // Incorrect password
            }
        } else {
            handleFailedAttempt(name, con); // User not found
        }

        Database.closeResultSet(rs);
        Database.closePreparedStatement(stmt);
        Database.closeConnection(con);
    }

    private void handleFailedAttempt(String name, Connection con) throws SQLException {
        int attempts = failedAttempts.getOrDefault(name, 0) + 1;
        failedAttempts.put(name, attempts);

        if (attempts >= MAX_ATTEMPTS) {
            // Lock the user account
            String lockQuery = "UPDATE user SET lock = 'LOCKED' WHERE name = ?";
            PreparedStatement lockStmt = con.prepareStatement(lockQuery);
            lockStmt.setString(1, name);
            lockStmt.executeUpdate();

            MainApplication.logData(new Log(0, "WARN", "User " + name + " was locked due to too many failed login attempts.", new Date().toString()));

            showAlert(Alert.AlertType.ERROR, "Account Locked", "Too Many Failed Attempts", "Your account has been locked. Please contact the administrator.");

            Database.closePreparedStatement(lockStmt);
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid Username or Password", "Attempts remaining: " + (MAX_ATTEMPTS - attempts));
        }
    }

    private void navigateToRoleView(User.ROLE role) throws IOException {
        String dest;
        switch (role) {
            case ADMIN:
                dest = "/view/Admin-view.fxml";
                break;
            case USER:
                dest = "/view/Todo-view.fxml";
                break;
            default:
                dest = "/view/Todo-view.fxml";
                break;
        }
        MainApplication.navigateTo(anchorPaneLogin, dest);
    }

    @FXML
    void onRegisterButtonClicked(ActionEvent event) throws IOException {
        MainApplication.navigateTo(anchorPaneLogin, "/view/register-view.fxml");
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

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}