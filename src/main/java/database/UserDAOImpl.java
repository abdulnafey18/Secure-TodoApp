package database;

import model.User;
import security.Encryption;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    //SQL Injection mitigation
    @Override
    public int create(User user) throws SQLException {
        Connection con = Database.getConnection();
        String sql = "INSERT INTO user (name, password, email, role, lock) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, user.getName());
        ps.setString(2, user.getPassword());
        ps.setString(3, user.getEmail());
        ps.setString(4, user.getRole() != null ? user.getRole().toString() : "USER"); // Default role
        ps.setString(5, user.getLock() != null ? user.getLock().toString() : "UNLOCKED"); // Default lock status

        int result = ps.executeUpdate();

        Database.closePreparedStatement(ps);
        Database.closeConnection(con);

        return result;
    }

    @Override
    public User readOne(int id) throws SQLException {
        Connection con = Database.getConnection();
        User user = null;
        String sql = "SELECT * FROM user WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            user = mapUser(rs);
        }

        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);

        return user;
    }

    @Override
    public int update(User user) throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "UPDATE user SET name = ?, password = ?, role = ?, lock = ? WHERE id = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, user.getName());
        ps.setString(2, user.getPassword()); // Save the hashed password directly
        ps.setString(3, user.getRole().toString());
        ps.setString(4, user.getLock().toString());
        ps.setInt(5, user.getId());

        int result = ps.executeUpdate();

        Database.closePreparedStatement(ps);
        Database.closeConnection(connection);

        return result;
    }

    @Override
    public int delete(User user) throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "DELETE FROM user WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, user.getId());

        int result = ps.executeUpdate();

        Database.closePreparedStatement(ps);
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
            users.add(mapUser(rs));
        }

        Database.closeResultSet(rs);
        Database.closeStatement(stmt);
        Database.closeConnection(con);

        return users;
    }

    @Override
    public int checkExists(String name, String password) throws SQLException {
        Connection con = Database.getConnection();
        String query = "SELECT password FROM user WHERE name = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, name);

        ResultSet rs = ps.executeQuery();
        int exists = 0;

        if (rs.next()) {
            String hashedPassword = rs.getString("password");
            if (Encryption.verifyPassword(password, hashedPassword)) {
                exists = 1; // User exists with correct credentials
            }
        }

        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);

        return exists;
    }

    @Override
    public User readLoggedInUser(String name, String password) throws SQLException {
        Connection con = Database.getConnection();
        User user = null;

        System.out.println("Executing SQL: SELECT * FROM `user` WHERE name = ?");
        System.out.println("With Parameter: name = " + name);

        String sql = "SELECT * FROM `user` WHERE name = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, name);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            String hashedPassword = rs.getString("password");
            System.out.println("User Found: " + rs.getString("name"));
            System.out.println("Stored Hashed Password: " + hashedPassword);

            // Verify the password
            if (Encryption.verifyPassword(password, hashedPassword)) {
                System.out.println("Password Verified Successfully");
                user = mapUser(rs);
            } else {
                System.out.println("Password Verification Failed");
            }
        } else {
            System.out.println("No user found with name: " + name);
        }

        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);

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