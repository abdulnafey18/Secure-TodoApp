package controller;

import database.LogDAOImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import model.Log;
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
    private LogDAOImpl db = new LogDAOImpl();

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
            int userId = LogginUser.getUser().getId();

            // Insert the new task into the database
            String query = "INSERT INTO todos (user_id, task) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            statement.setString(2, newTask);
            statement.executeUpdate();

            tasks.add(newTask);
            listAdd.clear();

            // Log task creation
            Log log = new Log("INFO", "Task created by user: " + LogginUser.getUser().getName() + ", details: " + newTask, new java.util.Date().toString());
            db.create(log);

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
            int userId = LogginUser.getUser().getId();

            // Update the task for the logged-in user
            String query = "UPDATE todos SET task = ? WHERE user_id = ? AND task = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, newTask);
            statement.setInt(2, userId);
            statement.setString(3, selectedTask);
            statement.executeUpdate();

            tasks.set(tasks.indexOf(selectedTask), newTask);
            listView.refresh();
            listAdd.clear();

            // Log task update
            Log log = new Log("INFO", "Task updated by user: " + LogginUser.getUser().getName() + ", old details: " + selectedTask + ", new details: " + newTask, new java.util.Date().toString());
            db.create(log);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteTask() {
        String selectedTask = listView.getSelectionModel().getSelectedItem();
        if (selectedTask == null) return;

        try {
            int userId = LogginUser.getUser().getId();

            // Delete the selected task for the logged-in user
            String query = "DELETE FROM todos WHERE user_id = ? AND task = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            statement.setString(2, selectedTask);
            statement.executeUpdate();

            tasks.remove(selectedTask);

            // Log task deletion
            Log log = new Log("INFO", "Task deleted by user: " + LogginUser.getUser().getName() + ", details: " + selectedTask, new java.util.Date().toString());
            db.create(log);

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