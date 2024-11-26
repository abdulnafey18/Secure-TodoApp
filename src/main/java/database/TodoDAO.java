// TodoDAO.java
package database;

import model.Todo;

import java.sql.SQLException;
import java.util.List;

public interface TodoDAO extends DAO<Todo> {
    // Fetch all todos for a specific user
    List<Todo> readAllByUserId(int userId) throws SQLException;
}

