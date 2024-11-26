package database;

import model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDAO extends DAO<User> {
    List<User> readAll() throws SQLException;
    int checkExists(String a, String b) throws SQLException;
    User readLoggedInUser(String a, String b) throws SQLException;
}
