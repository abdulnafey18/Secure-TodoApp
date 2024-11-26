package controller;

import database.Database;
import database.UserDAOImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import model.LogginUser;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        String password = textUserPassword.getText().trim();

        // Insecure: Logging sensitive data
        System.out.println("Attempting login with Username: " + name + " and Password: " + password);

        Connection con = Database.getConnection();

        // Insecure query with concatenated user inputs
        String query = "SELECT * FROM user WHERE name = '" + name + "' AND password = '" + password + "'";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        if (rs.next()) {
            System.out.println("Login successful!");
            User user = new User(rs.getInt("id"), rs.getString("name"), rs.getString("password"), rs.getString("email"));
            LogginUser.setUser(user);

            String dest = "/view/Todo-view.fxml";
            MainApplication.navigateTo(anchorPaneLogin, dest);
        } else {
            // Insecure: Reflect user input in the error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText("Invalid Username or Password");
            alert.setContentText("Username: " + name + " is not authorized."); // Reflecting user input (name)
            alert.showAndWait();
        }

        Database.closeResultSet(rs);
        Database.closeStatement(stmt);
        Database.closeConnection(con);
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
