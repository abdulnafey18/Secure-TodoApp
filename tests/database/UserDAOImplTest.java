package database;

import model.User;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserDAOImplTest {
    UserDAOImpl userDAO;
    Connection conn;
    User testUser;
    int result;

    @BeforeMethod
    public void setUp() throws SQLException {
        Reporter.log("Setting up BeforeMethod");
        userDAO = new UserDAOImpl();
        conn = Database.getConnection();
        testUser = new User(1, "testUser", "Password@789", "test@test.com", "USER", "UNLOCKED");
    }

    @AfterMethod
    public void tearDown() throws SQLException {
        Reporter.log("Closing Connection");
        Database.closeConnection(conn);
    }

    @Test(description = "This will create a Test User", priority = 1)
    public void testCreate() throws SQLException {
        Reporter.log("Test to Create a unique User");
        result = userDAO.create(testUser);
        Assert.assertEquals(result, 1);
    }

    @Test(description = "This will Read all Users", priority = 2)
    public void testReadAll() throws SQLException {
        Reporter.log("Checking ResultSet for entry with unique User");
        List<User> actualList = userDAO.readAll();
        boolean contains = actualList.stream().anyMatch(p -> p.getEmail().equals("test@test.com"));
        Assert.assertTrue(contains, "Comparison of List");
    }

    @Test(description = "This will update the testUser's lock status", priority = 3)
    public void testUpdate() throws SQLException {
        Reporter.log("Updating User Lock Status");
        testUser.setLockStatus("LOCKED");
        result = userDAO.update(testUser);
        Assert.assertEquals(result, 1);
    }

    @Test(description = "This will delete testUser", priority = 4)
    public void testDelete() throws SQLException {
        Reporter.log("Deleting unique User");
        result = userDAO.delete(testUser);
        Assert.assertEquals(result, 1);
    }
}