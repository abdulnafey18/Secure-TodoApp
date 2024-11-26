package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.LogginUser;

import java.io.IOException;
import java.sql.*;

public class TodoController {

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnEdit;

    @FXML
    private TextField listAdd;

    @FXML
    private ListView<String> listView;

    private ObservableList<String> tasks = FXCollections.observableArrayList();

    private Connection connection;
    private int userId = 1; // Assuming a logged-in user with ID 1 for simplicity

    @FXML
    public void initialize() {
        connectToDatabase();
        loadTasks();
    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:data.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadTasks() {
        tasks.clear();

        try {
            // Get the logged-in user's ID
            int userId = LogginUser.getUser().getId();

            // Fetch tasks for the logged-in user
            String query = "SELECT task FROM todos WHERE user_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                tasks.add(resultSet.getString("task"));
            }
            listView.setItems(tasks);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addTask() {
        String newTask = listAdd.getText();
        if (newTask.isEmpty()) return;

        try {
            // Get the logged-in user's ID
            int userId = LogginUser.getUser().getId();

            // Insecure: Logging sensitive data
            System.out.println("Adding task for UserID: " + userId + " with Task: " + newTask);

            // Insecure query: Directly concatenating user inputs (no sanitization or validation)
            String query = "INSERT INTO todos (user_id, task) VALUES (" + userId + ", '" + newTask + "')";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);

            // Add the task to the list directly without sanitizing or escaping
            tasks.add(newTask);
            listView.setItems(tasks); // Display the task without escaping
            listAdd.clear();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editTask() {
        String selectedTask = listView.getSelectionModel().getSelectedItem();
        String newTask = listAdd.getText();

        if (selectedTask == null || newTask.isEmpty()) return;

        try {
            // Get the logged-in user's ID
            int userId = LogginUser.getUser().getId();

            // Insecure query: Directly concatenating user inputs
            String query = "UPDATE todos SET task = '" + newTask + "' WHERE user_id = " + userId +
                    " AND task = '" + selectedTask + "'";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);

            tasks.set(tasks.indexOf(selectedTask), newTask);
            listView.refresh();
            listAdd.clear();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteTask() {
        String selectedTask = listView.getSelectionModel().getSelectedItem();
        if (selectedTask == null) return;

        try {
            // Get the logged-in user's ID
            int userId = LogginUser.getUser().getId();

            // Insecure query: Directly concatenating user inputs
            String query = "DELETE FROM todos WHERE user_id = " + userId + " AND task = '" + selectedTask + "'";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);

            tasks.remove(selectedTask);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void switchToMain(ActionEvent actionEvent) {
        try {
            // Load the login screen FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main-view.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            // Set the login screen scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
