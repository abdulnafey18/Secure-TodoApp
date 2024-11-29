package monitor.Misc;

import database.Database;
import model.User;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Cabbage {

    public static void setSprouts() {
        try {
            File f = new File("data.db");
            System.out.println("Database exists: " + f.exists());
            if (!f.exists()) {
                Database.getConnection();
                User u1 = new User(1, "Admin", "Password@123", "admin@company.com", User.ROLE.ADMIN.toString(), User.LOCK.UNLOCKED.toString());
                User u2 = new User(2, "User", "Password@456", "developer@company.com", User.ROLE.USER.toString(), User.LOCK.UNLOCKED.toString());
                System.out.println("Creating Admin user...");
                createDefaultUser(u1);
                System.out.println("Creating User...");
                createDefaultUser(u2);
            }
        } catch (SQLException e) {
            System.err.println("Error creating default users: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static int createDefaultUser(User user) throws SQLException {
        Connection con = Database.getConnection();
        String sql = "INSERT INTO user (name, password, email, role, lock) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, user.getName());
        ps.setString(2, user.getPassword());
        ps.setString(3, user.getEmail());
        ps.setString(4, user.getRole().toString());
        ps.setString(5, user.getLock().toString());

        int result = ps.executeUpdate();
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);
        return result;
    }
}