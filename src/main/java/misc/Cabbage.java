package misc;

import database.Database;
import model.User;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Cabbage {

    public static void setSprouts(){
        try {

            File f = new File("data.db");
            System.out.println("Database exists"+f.exists());
            if(!f.exists()){
                Database.getConnection();
                User u1 = new User(1,"Abdul","Adjonas@123","abdul@company.com");
                User u2 = new User(2,"Sofia","Mhfocus1","sofia@company.com");
                User u3 = new User(3,"Emma","eKelly@24","emma@company.com");
                createDefaultUser(u1);
                createDefaultUser(u2);
                createDefaultUser(u3);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private static int createDefaultUser(User user) throws SQLException {
        Connection con = Database.getConnection();
        String sql = "INSERT INTO user (name, password,email) VALUES (?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, user.getName());
        ps.setString(2, user.getPassword());
        ps.setString(3, user.getEmail());

        int result = ps.executeUpdate();
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);
        return result;

    }
}
