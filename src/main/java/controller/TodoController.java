package controller;

import database.TodoDAO;
import database.TodoDAOImpl;
import model.Todo;
import model.User;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoDAO todoDAO;

    public TodoController() {
        this.todoDAO = new TodoDAOImpl();
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
            System.out.println("Task received before saving: " + todo.getTask()); // Debugging log
            todo.setUserId(currentUser.getId());
            todoDAO.create(todo);
            return todo;
        } catch (SQLException e) {
            e.printStackTrace();
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
            todo.setId(id);
            todoDAO.update(todo);
            return todo;
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
            todoDAO.delete(new Todo(id, currentUser.getId(), ""));
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting todo", e);
        }
    }
}