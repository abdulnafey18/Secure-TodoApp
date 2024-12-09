package database;

import model.Todo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TodoDAOImpl implements TodoDAO {

    @Override
    public int create(Todo todo) throws SQLException {
        String sql = "INSERT INTO todos (user_id, task) VALUES ("
                + todo.getUserId() + ", '"
                + todo.getTask() + "')";
        try (Connection con = Database.getConnection();
             Statement stmt = con.createStatement()) {
            return stmt.executeUpdate(sql);
        }
    }

    @Override
    public Todo readOne(int id) throws SQLException {
        String sql = "SELECT * FROM todos WHERE id = " + id;
        Todo todo = null;

        try (Connection con = Database.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                int todoId = rs.getInt("id");
                int userId = rs.getInt("user_id");
                String task = rs.getString("task");
                todo = new Todo(todoId, userId, task);
            }
        }
        return todo;
    }

    @Override
    public int update(Todo todo) throws SQLException {
        String sql = "UPDATE todos SET task = '"
                + todo.getTask() + "' WHERE id = "
                + todo.getId();
        try (Connection con = Database.getConnection();
             Statement stmt = con.createStatement()) {
            return stmt.executeUpdate(sql);
        }
    }

    @Override
    public int delete(Todo todo) throws SQLException {
        String sql = "DELETE FROM todos WHERE id = " + todo.getId();
        try (Connection con = Database.getConnection();
             Statement stmt = con.createStatement()) {
            return stmt.executeUpdate(sql);
        }
    }

    @Override
    public List<Todo> readAllByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM todos WHERE user_id = " + userId;
        List<Todo> todos = new ArrayList<>();

        try (Connection con = Database.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String task = rs.getString("task");
                todos.add(new Todo(id, userId, task));
            }
        }
        return todos;
    }
}