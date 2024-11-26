package database;

import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {


    @Override
    public int create(User user) throws SQLException {
        Connection con = Database.getConnection();

        // Insecure query with concatenated user inputs
        String query = "INSERT INTO user (name, password, email) VALUES ('" +
                user.getName() + "', '" + user.getPassword() + "', '" + user.getEmail() + "')";
        Statement stmt = con.createStatement();
        int result = stmt.executeUpdate(query);

        Database.closeStatement(stmt);
        Database.closeConnection(con);
        return result;
    }

    @Override
    public User readOne(int id) throws SQLException {
        Connection con = Database.getConnection();
        User user = null;

        // Insecure query: Directly concatenating user input (id)
        String sql = "SELECT * FROM user WHERE id = " + id;
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        if (rs.next()) {
            Integer _id = rs.getInt("id");
            String userName = rs.getString("name");
            String userPassword = rs.getString("password");
            String userEmail = rs.getString("email");
            user = new User(_id, userName, userPassword, userEmail);
        }

        Database.closeResultSet(rs);
        Database.closeStatement(stmt);
        Database.closeConnection(con);

        return user;
    }

    @Override
    public int update(User user) throws SQLException {
        Connection connection = Database.getConnection();

        // Insecure query: Directly concatenating user inputs (name, password, id)
        String sql = "UPDATE user SET name = '" + user.getName() +
                "', password = '" + user.getPassword() +
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

        // Insecure query: Directly concatenating user inputs (name, password)
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
            Integer _id = rs.getInt("id");
            String userName = rs.getString("name");
            String userPassword = rs.getString("password");
            String userEmail = rs.getString("email");
            User user = new User(_id,userName, userPassword, userEmail);
            users.add(user);
        }
        return users;
    }

    @Override
    public int checkExists(String a, String b) throws SQLException {
        Connection con = Database.getConnection();
        int count = 0;

        // Insecure query: Directly concatenating user inputs (a, b)
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

        // Insecure query with concatenated user inputs
        String query = "SELECT * FROM user WHERE name = '" + name + "' AND password = '" + password + "'";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        if (rs.next()) {
            Integer _id = rs.getInt("id");
            String userName = rs.getString("name");
            String userPassword = rs.getString("password");
            String userEmail = rs.getString("email");
            user = new User(_id, userName, userPassword, userEmail);
        }

        Database.closeResultSet(rs);
        Database.closeStatement(stmt);
        Database.closeConnection(con);
        return user;
    }
}
