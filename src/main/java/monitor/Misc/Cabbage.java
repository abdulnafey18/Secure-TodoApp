package monitor.Misc;

import database.Database;
import model.User;
import security.Encryption;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Cabbage {

    public static void setSprouts() {
        try {
            File f = new File("data.db");
            System.out.println("Database exists: " + f.exists());
            if (!f.exists()) {
                Database.getConnection();
                // Hash passwords before passing to User object
                User u1 = new User(1, "Admin", Encryption.hashPassword("Password@123"), "admin@company.com", User.ROLE.ADMIN.toString(), User.LOCK.UNLOCKED.toString());
                User u2 = new User(2, "User", Encryption.hashPassword("Password@456"), "developer@company.com", User.ROLE.USER.toString(), User.LOCK.UNLOCKED.toString());
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

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getPassword()); // Already hashed password
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getRole().toString());
            ps.setString(5, user.getLock().toString());
            return ps.executeUpdate();
        } finally {
            Database.closeConnection(con);
        }
    }
}