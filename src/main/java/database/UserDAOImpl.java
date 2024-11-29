package database;

import model.User;
import security.Encryption;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    @Override
    public int create(User user) throws SQLException {
        Connection con = Database.getConnection();
        String sql = "INSERT INTO user (name, password, email, role, lock) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, user.getName());
        ps.setString(2, Encryption.hashPassword(user.getPassword())); // Securely hash the password
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
        ps.setString(2, Encryption.hashPassword(user.getPassword())); // Securely hash the password
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
        String query = "SELECT COUNT(*) FROM user WHERE name = ? AND password = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, name);
        ps.setString(2, Encryption.hashPassword(password)); // Hash the password before comparing

        ResultSet rs = ps.executeQuery();
        int count = rs.next() ? rs.getInt(1) : 0;

        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);

        return count;
    }

    @Override
    public User readLoggedInUser(String name, String password) throws SQLException {
        Connection con = Database.getConnection();
        User user = null;

        String sql = "SELECT * FROM user WHERE name = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, name);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            String hashedPassword = rs.getString("password");
            if (Encryption.verifyPassword(password, hashedPassword)) {
                user = mapUser(rs);
            }
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