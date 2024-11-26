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
        String sql = "INSERT INTO user (name, password, email) VALUES (?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, user.getName());
        ps.setString(2, user.getPassword());
        ps.setString(3, user.getEmail());

        int result = ps.executeUpdate();

        Database.closePreparedStatement(ps);
        Database.closeConnection(con);

        return result;

    }

    @Override
    public User readOne(int id) throws SQLException {
        Connection con = Database.getConnection();
        User user = null;
        System.out.println(id);
        String sql = "SELECT * FROM user WHERE id = ? ";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Integer _id = rs.getInt("id");
            String userName = rs.getString("name");
            String userPassword = rs.getString("password");
            String userEmail = rs.getString("email");
            user = new User(_id,userName, userPassword, userEmail);
        }
        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);
        return user;
    }

    @Override
    public int update(User user) throws SQLException {
        Connection connection = Database.getConnection();
        System.out.println(user.getName()+" "+user.getPassword());
        String sql = "UPDATE user set name = ?, password = ?, where id = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, user.getName());
        ps.setString(2, user.getPassword());
        ps.setInt(3, user.getId());

        int result = ps.executeUpdate();
        Database.closePreparedStatement(ps);
        Database.closeConnection(connection);
        return result;

    }

    @Override
    public int delete(User user) throws SQLException {
        Connection connection = Database.getConnection();

        String sql = "DELETE  FROM user WHERE name =? AND password = ?";
        System.out.println(sql);

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, user.getName());
        ps.setString(2, user.getPassword());
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
        int count = 0;
        User user= null;
        Connection con = Database.getConnection();

        String query = "SELECT COUNT(*) FROM user WHERE name = ? AND password = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1,a);
        ps.setString(2,b);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            count = rs.getInt(1);
        }
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
        ps.setString(1, name); // Only the name is used for login purposes in the secure implementation

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            String hashedPassword = rs.getString("password");
            if (!Encryption.verifyPassword(password, hashedPassword)) {
                // Password does not match
                Database.closeResultSet(rs);
                Database.closePreparedStatement(ps);
                Database.closeConnection(con);
                return null;
            }
            // Password matches, retrieve the user
            Integer _id = rs.getInt("id");
            String userName = rs.getString("name");
            String userEmail = rs.getString("email");
            user = new User(_id, userName, hashedPassword, userEmail);
        }

        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);

        return user;
    }
}
