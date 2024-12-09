package controller;

import database.TodoDAO;
import database.TodoDAOImpl;
import database.UserDAO;
import database.UserDAOImpl;
import database.LogDAO;
import database.LogDAOImpl;
import model.Todo;
import model.User;
import model.Log;

import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoDAO todoDAO;
    private final UserDAO userDAO;
    private final LogDAO logDAO;

    public TodoController() {
        this.todoDAO = new TodoDAOImpl();
        this.userDAO = new UserDAOImpl();
        this.logDAO = new LogDAOImpl();
    }

    @GetMapping("/{userId}")
    public List<Todo> getAllTodos(@PathVariable int userId, HttpSession session) {
        try {
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser == null || currentUser.getId() != userId) {
                throw new RuntimeException("Unauthorized access or user not logged in.");
            }
            return todoDAO.readAllByUserId(userId);
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching todos", e);
        }
    }

    @PostMapping
    public Todo addTodo(@RequestBody Todo todo, HttpSession session) {
        try {
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser == null) {
                throw new RuntimeException("Current user not logged in.");
            }

            todo.setUserId(currentUser.getId());
            int result = todoDAO.create(todo);
            if (result > 0) {
                Log log = new Log("INFO", currentUser.getName() + " added a task " + todo.getTask(), new Date().toString());
                logDAO.create(log);
                return todo;
            } else {
                throw new RuntimeException("Failed to create todo");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating todo", e);
        }
    }

    @PutMapping("/{id}")
    public Todo updateTodo(@PathVariable int id, @RequestBody Todo todo, HttpSession session) {
        try {
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser == null) {
                throw new RuntimeException("Current user not logged in.");
            }

            Todo existingTodo = todoDAO.readOne(id);
            if (existingTodo == null) {
                throw new RuntimeException("Task not found.");
            }

            todo.setId(id);
            int result = todoDAO.update(todo);
            if (result > 0) {
                Log log = new Log("INFO", currentUser.getName() + " updated task \"" + existingTodo.getTask() + "\" to \"" + todo.getTask() + "\"", new Date().toString());
                logDAO.create(log);
                return todo;
            } else {
                throw new RuntimeException("Failed to update todo");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating todo", e);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteTodoById(@PathVariable int id, HttpSession session) {
        try {
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser == null) {
                throw new RuntimeException("Current user not logged in.");
            }

            Todo todo = todoDAO.readOne(id);
            if (todo != null) {
                todoDAO.delete(todo);
                Log log = new Log("INFO", currentUser.getName() + " deleted task \"" + todo.getTask() + "\"", new Date().toString());
                logDAO.create(log);
            } else {
                throw new RuntimeException("Todo not found with ID: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting todo", e);
        }
    }
}