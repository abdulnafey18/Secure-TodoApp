// TodoDAOImpl.java
package database;

import model.Todo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TodoDAOImpl implements TodoDAO {

    @Override
    public int create(Todo todo) throws SQLException {
        Connection con = Database.getConnection();

        // Insecure query with concatenated user inputs
        String query = "INSERT INTO todos (user_id, task) VALUES (" +
                todo.getUserId() + ", '" + todo.getTask() + "')";
        Statement stmt = con.createStatement();
        int result = stmt.executeUpdate(query);

        Database.closeStatement(stmt);
        Database.closeConnection(con);
        return result;
    }

    @Override
    public Todo readOne(int id) throws SQLException {
        Connection con = Database.getConnection();
        Todo todo = null;

        String sql = "SELECT * FROM todos WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int todoId = rs.getInt("id");
            int userId = rs.getInt("user_id");
            String task = rs.getString("task");
            todo = new Todo(todoId, userId, task);
        }

        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);

        return todo;
    }

    @Override
    public int update(Todo todo) throws SQLException {
        Connection con = Database.getConnection();

        String sql = "UPDATE todos SET task = ? WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, todo.getTask());
        ps.setInt(3, todo.getId());

        int result = ps.executeUpdate();

        Database.closePreparedStatement(ps);
        Database.closeConnection(con);

        return result;
    }

    @Override
    public int delete(Todo todo) throws SQLException {
        Connection con = Database.getConnection();

        String sql = "DELETE FROM todos WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, todo.getId());

        int result = ps.executeUpdate();

        Database.closePreparedStatement(ps);
        Database.closeConnection(con);

        return result;
    }

    @Override
    public List<Todo> readAllByUserId(int userId) throws SQLException {
        Connection con = Database.getConnection();
        List<Todo> todos = new ArrayList<>();

        // Insecure query with concatenated user inputs
        String query = "SELECT * FROM todos WHERE user_id = " + userId;
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            int id = rs.getInt("id");
            String task = rs.getString("task");
            todos.add(new Todo(id, userId, task));
        }

        Database.closeResultSet(rs);
        Database.closeStatement(stmt);
        Database.closeConnection(con);
        return todos;
    }
}
