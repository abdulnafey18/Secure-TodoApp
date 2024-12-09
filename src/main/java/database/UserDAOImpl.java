package database;

import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    @Override
    public int create(User user) throws SQLException {
        String sql = "INSERT INTO user (name, password, email, role, lock) VALUES ('"
                + user.getName() + "', '"
                + user.getPassword() + "', '"
                + user.getEmail() + "', '"
                + (user.getRole() != null ? user.getRole().toString() : "USER") + "', '"
                + (user.getLock() != null ? user.getLock().toString() : "UNLOCKED") + "')";

        try (Connection con = Database.getConnection();
             Statement stmt = con.createStatement()) {
            return stmt.executeUpdate(sql);
        }
    }

    @Override
    public User readOne(int id) throws SQLException {
        String sql = "SELECT * FROM user WHERE id = " + id;
        User user = null;

        try (Connection con = Database.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                user = mapUser(rs);
            }
        }
        return user;
    }

    @Override
    public int update(User user) throws SQLException {
        String sql = "UPDATE user SET name = '" + user.getName() + "', "
                + "password = '" + user.getPassword() + "', "
                + "role = '" + user.getRole().toString() + "', "
                + "lock = '" + user.getLock().toString() + "' "
                + "WHERE id = " + user.getId();

        try (Connection con = Database.getConnection();
             Statement stmt = con.createStatement()) {
            return stmt.executeUpdate(sql);
        }
    }

    @Override
    public int delete(User user) throws SQLException {
        String sql = "DELETE FROM user WHERE id = " + user.getId();

        try (Connection con = Database.getConnection();
             Statement stmt = con.createStatement()) {
            return stmt.executeUpdate(sql);
        }
    }

    @Override
    public List<User> readAll() throws SQLException {
        String sql = "SELECT * FROM user";
        List<User> users = new ArrayList<>();

        try (Connection con = Database.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(mapUser(rs));
            }
        }
        return users;
    }

    @Override
    public int checkExists(String name, String password) throws SQLException {
        String sql = "SELECT password FROM user WHERE name = '" + name + "'";
        int exists = 0;

        try (Connection con = Database.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (password.equals(storedPassword)) {
                    exists = 1; // User exists with matching credentials
                }
            }
        }
        return exists;
    }

    @Override
    public User readLoggedInUser(String name, String password) throws SQLException {
        String sql = "SELECT * FROM user WHERE name = '" + name + "'";
        User user = null;

        try (Connection con = Database.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (password.equals(storedPassword)) {
                    user = mapUser(rs);
                }
            }
        }
        return user;
    }

    private User mapUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("password"),
                rs.getString("email"),
                rs.getString("role"),
                rs.getString("lock")
        );
    }
}