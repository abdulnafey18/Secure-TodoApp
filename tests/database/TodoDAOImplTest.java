package database;

import model.Todo;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class TodoDAOImplTest {
    TodoDAOImpl todoDAO;
    Connection conn;
    Todo testTodo;
    int result;

    @BeforeMethod
    public void setUp() throws SQLException {
        Reporter.log("Setting up BeforeMethod");
        todoDAO = new TodoDAOImpl();
        conn = Database.getConnection();
        testTodo = new Todo(1, "Test Task", "Description for Test Task");
    }

    @AfterMethod
    public void tearDown() throws SQLException {
        Reporter.log("Closing Connection");
        Database.closeConnection(conn);
    }

    @Test(description = "This will create a Test Task", priority = 1)
    public void testCreate() throws SQLException {
        Reporter.log("Creating a unique Task");
        result = todoDAO.create(testTodo);
        Assert.assertEquals(result, 1);
    }

    @Test(description = "This will Read all Tasks", priority = 2)
    public void testReadAll() throws SQLException {
        Reporter.log("Checking ResultSet for entry with unique Task");
        List<Todo> actualList = todoDAO.readAll();
        boolean contains = actualList.stream().anyMatch(t -> t.getTitle().equals("Test Task"));
        Assert.assertTrue(contains, "Comparison of List");
    }


    @Test(description = "This will delete test Task", priority = 4)
    public void testDelete() throws SQLException {
        Reporter.log("Deleting unique Task");
        result = todoDAO.delete(testTodo);
        Assert.assertEquals(result, 1);
    }
}