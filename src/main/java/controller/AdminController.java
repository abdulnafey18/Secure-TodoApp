package controller;

import database.UserDAOImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Log;
import model.LogginUser;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @FXML
    private AnchorPane anchorPaneAdmin;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnDelete;

    @FXML
    private TableView<User> tabelViewAdmin;

    @FXML
    private TableColumn<User, String> columnName;

    @FXML
    private TableColumn<User, String> columnPassword;

    @FXML
    private TableColumn<User, String> columnRole;

    @FXML
    private TableColumn<User, String> columnLocked;

    @FXML
    private ComboBox<String> comboLock;

    @FXML
    private Button btnLogMonitor;

    @FXML
    private ComboBox<String> comboRole;

    @FXML
    private Label labelInfo;

    @FXML
    private TextField textNewPassword;

    private UserDAOImpl db;

    private ObservableList<String> roles;
    private ObservableList<String> lock;
    private User currentUser;

    @FXML
    void onBackButtonClicked(ActionEvent event) throws IOException {
        String dest = "/view/login-view.fxml";
        MainApplication.navigateTo(anchorPaneAdmin, dest);
    }

    @FXML
    void onDeleteButtonClicked(ActionEvent event) throws IOException, SQLException {
        int row = tabelViewAdmin.getSelectionModel().getSelectedIndex();
        if (row >= 0) {
            delete(row);
            tabelViewAdmin.setItems(populateUsers());
        }
    }

    @FXML
    void onLogMonitorButtonClicked(ActionEvent event) throws IOException {
        String dest = "/view/log-view.fxml"; // Path to your Log View FXML file
        MainApplication.navigateTo(anchorPaneAdmin, dest);
    }

    @FXML
    void onUpdateButtonClicked(ActionEvent event) throws SQLException {
        int row = tabelViewAdmin.getSelectionModel().getSelectedIndex();
        if (row >= 0) {
            update(row);
            textNewPassword.setText("");
            comboRole.setValue(roles.get(2));
            comboLock.setValue(lock.get(0));
            tabelViewAdmin.setItems(populateUsers());
        }
    }

    private void setUpTable() {
        db = new UserDAOImpl();
        tabelViewAdmin.setEditable(true);

        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        columnRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        columnLocked.setCellValueFactory(new PropertyValueFactory<>("lock"));

        try {
            ObservableList<User> users = populateUsers();
            tabelViewAdmin.setItems(users);
            tabelViewAdmin.refresh(); // Ensure table updates
        } catch (SQLException e) {
            e.printStackTrace(); // Debug any issues
        }

        tabelViewAdmin.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                textNewPassword.setText(newSelection.getPassword());
                comboLock.setValue(newSelection.getLock().toString());
            }
        });
    }

    private void update(int row) throws SQLException {
        User u = (User) tabelViewAdmin.getItems().get(row);
        u.setPassword(textNewPassword.getText().trim());
        String l = comboLock.getValue();

        // Set updated role and lock status
        u.setLock(User.LOCK.valueOf(l));

        db = new UserDAOImpl();
        db.update(u);

        // Log the lock/unlock action
        if ("LOCKED".equalsIgnoreCase(l)) {
            MainApplication.logData(new Log(0, "INFO", "User " + u.getName() + " was locked.", new Date().toString()));
        } else if ("UNLOCKED".equalsIgnoreCase(l)) {
            MainApplication.logData(new Log(0, "INFO", "User " + u.getName() + " was unlocked.", new Date().toString()));
        }
    }

    private void delete(int row) throws SQLException {
        if (row >= 0) {
            User u = (User) tabelViewAdmin.getItems().get(row);
            if (currentUser != null && currentUser.getRole().toString().equals("ADMIN")) {
                tabelViewAdmin.getItems().remove(row);
                tabelViewAdmin.getSelectionModel().clearSelection();
                db.delete(u);
                MainApplication.logData(new Log(0, "WARN", u.getName() + " Deleted ", new Date().toString()));
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning ");
                alert.setHeaderText("Unauthorized Action");
                alert.setContentText("You do not have permission to delete this user.");
                alert.showAndWait();
                MainApplication.logData(new Log(0, "WARN", "Failed to Delete ", new Date().toString()));
            }
        }
    }

    void setUpComboBoxRoles() {
        lock = FXCollections.observableArrayList();
        lock.add(User.LOCK.LOCKED.toString());
        lock.add(User.LOCK.UNLOCKED.toString());
        comboLock.setItems(lock);
    }

    private ObservableList<User> populateUsers() throws SQLException {
        UserDAOImpl db = new UserDAOImpl();
        List<User> list = db.readAll();
        return FXCollections.observableArrayList(list);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpTable();
        setUpComboBoxRoles();

        currentUser = LogginUser.getUser();
        if (currentUser != null) {
            labelInfo.setText(currentUser.getName() + " is logged In");
        } else {
            labelInfo.setText("");
        }
    }
}