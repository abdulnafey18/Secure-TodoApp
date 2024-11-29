package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.LogginUser;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private AnchorPane anchorPaneMain;

    @FXML
    private AnchorPane anchorPaneMainLead;

    @FXML
    private Button btnExit;

    @FXML
    private Button btnLogin;

    @FXML
    private Label labelInfo;

    @FXML
    void onAdminButtonClicked(ActionEvent event) throws IOException {
        String dest = "/view/admin-view.fxml";
        MainApplication.navigateTo(anchorPaneMain,dest);

    }

    @FXML
    void onLoginButtonClicked(ActionEvent event) throws IOException {
        String dest = "/view/login-view.fxml";
        MainApplication.navigateTo(anchorPaneMain,dest);
    }

    @FXML
    void onMonitorButtonClicked(ActionEvent event) throws IOException {
        String dest = "/view/log-view.fxml";
        MainApplication.navigateTo(anchorPaneMain,dest);

    }

    @FXML
    void onExitClicked(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("you are about to Exit");
        alert.setContentText("");
        if(alert.showAndWait().get() == ButtonType.OK){
            Stage stage = (Stage) anchorPaneMain.getScene().getWindow();
            System.out.println("logging oup");
            stage.close();
        }
    }
    void setDisplayVisability(String role){
            anchorPaneMainLead.setVisible(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        User currentUser = LogginUser.getUser();
        if(currentUser !=null){
            labelInfo.setText(currentUser.getName()+" is logged In");
            setDisplayVisability(currentUser.getName().toString());
        }

    }
}