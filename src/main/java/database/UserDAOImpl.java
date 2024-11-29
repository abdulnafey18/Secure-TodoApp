package database;

import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    @Override
    public int create(User user) throws SQLException {
        Connection con = Database.getConnection();

        // Use default values for role and lock if not provided
        String role = (user.getRole() != null) ? user.getRole().toString() : "USER";
        String lock = (user.getLock() != null) ? user.getLock().toString() : "UNLOCKED";

        // Updated insert query with explicit default values
        String query = "INSERT INTO user (name, password, email, role, lock) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = con.prepareStatement(query);

        stmt.setString(1, user.getName());
        stmt.setString(2, user.getPassword());
        stmt.setString(3, user.getEmail());
        stmt.setString(4, role);  // Default to USER if not set
        stmt.setString(5, lock);  // Default to UNLOCKED if not set

        int result = stmt.executeUpdate();

        Database.closePreparedStatement(stmt);
        Database.closeConnection(con);
        return result;
    }

    @Override
    public User readOne(int id) throws SQLException {
        Connection con = Database.getConnection();
        User user = null;

        // Select query to fetch a user by id
        String sql = "SELECT * FROM user WHERE id = " + id;
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        if (rs.next()) {
            // Map the result set to a User object
            user = new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("role"),
                    rs.getString("lock")
            );
        }

        Database.closeResultSet(rs);
        Database.closeStatement(stmt);
        Database.closeConnection(con);

        return user;
    }

    @Override
    public int update(User user) throws SQLException {
        Connection connection = Database.getConnection();

        // Update query to modify user attributes
        String sql = "UPDATE user SET name = '" + user.getName() +
                "', password = '" + user.getPassword() +
                "', role = '" + user.getRole() +
                "', lock = '" + user.getLock() +
                "' WHERE id = " + user.getId();
        Statement stmt = connection.createStatement();
        int result = stmt.executeUpdate(sql);

        Database.closeStatement(stmt);
        Database.closeConnection(connection);

        return result;
    }

    @Override
    public int delete(User user) throws SQLException {
        Connection connection = Database.getConnection();

        // Delete query to remove a user
        String sql = "DELETE FROM user WHERE name = '" + user.getName() +
                "' AND password = '" + user.getPassword() + "'";
        Statement stmt = connection.createStatement();
        int result = stmt.executeUpdate(sql);

        Database.closeStatement(stmt);
        Database.closeConnection(connection);

        return result;
    }

    @Override
    public List<User> readAll() throws SQLException {
        Connection con = Database.getConnection();
        String sql = "SELECT * FROM user";
        List<User> users = new ArrayList<>();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            // Map the result set to User objects and add to the list
            User user = new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("role"),
                    rs.getString("lock")
            );
            users.add(user);
        }

        Database.closeResultSet(rs);
        Database.closeStatement(stmt);
        Database.closeConnection(con);

        return users;
    }

    @Override
    public int checkExists(String a, String b) throws SQLException {
        Connection con = Database.getConnection();
        int count = 0;

        // Check existence of user by name and password
        String query = "SELECT COUNT(*) FROM user WHERE name = '" + a +
                "' AND password = '" + b + "'";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        if (rs.next()) {
            count = rs.getInt(1);
        }

        Database.closeResultSet(rs);
        Database.closeStatement(stmt);
        Database.closeConnection(con);

        return count;
    }

    @Override
    public User readLoggedInUser(String name, String password) throws SQLException {
        Connection con = Database.getConnection();
        User user = null;

        // Select query to find a user with specific name and password
        String query = "SELECT * FROM user WHERE name = '" + name + "' AND password = '" + password + "'";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        if (rs.next()) {
            // Map the result set to a User object
            user = new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("role"),
                    rs.getString("lock")
            );
        }

        Database.closeResultSet(rs);
        Database.closeStatement(stmt);
        Database.closeConnection(con);

        return user;
    }
}
