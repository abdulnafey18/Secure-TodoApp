package controller;

import database.UserDAO;
import database.UserDAOImpl;
import database.LogDAO;
import database.LogDAOImpl;
import model.User;
import model.Log;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserDAO userDAO;
    private final LogDAO logDAO;

    public AdminController() {
        this.userDAO = new UserDAOImpl();
        this.logDAO = new LogDAOImpl();
    }

    @GetMapping("/users")
    public List<User> getAllUsers(HttpSession session) {
        try {
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser == null) {
                throw new RuntimeException("Current user not logged in.");
            }
            return userDAO.readAll();
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching users: " + e.getMessage(), e);
        }
    }

    @PutMapping("/users/{id}/lock")
    public ResponseEntity<?> lockUser(@PathVariable int id, @RequestParam boolean locked, HttpSession session) {
        try {
            // Check if the current user is logged in
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser == null) {
                return ResponseEntity.status(401).body(Map.of("success", false, "message", "Current user not logged in"));
            }

            // Retrieve the user to be locked or unlocked
            User user = userDAO.readOne(id);
            if (user == null) {
                return ResponseEntity.status(404).body(Map.of("success", false, "message", "User not found with ID: " + id));
            }

            // Update the lock status
            user.setLock(locked ? User.LOCK.LOCKED : User.LOCK.UNLOCKED);
            userDAO.update(user);

            // Log the action
            String action = locked ? "locked" : "unlocked";
            Log log = new Log("WARN", currentUser.getName() + " " + action + " " + user.getName(), new Date().toString());
            logDAO.create(log);

            // Return a success response
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "User " + action + " successfully",
                    "user", Map.of(
                            "id", user.getId(),
                            "name", user.getName(),
                            "lockStatus", user.getLock().toString()
                    )
            ));
        } catch (SQLException e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Error updating user lock status: " + e.getMessage()
            ));
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id, HttpSession session) {
        try {
            // Check if the current user is logged in
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser == null) {
                return ResponseEntity.status(401).body(Map.of("success", false, "message", "Current user not logged in."));
            }

            // Fetch the user by ID
            User user = userDAO.readOne(id);
            if (user == null) {
                return ResponseEntity.status(404).body(Map.of("success", false, "message", "User not found with ID: " + id));
            }

            // Delete the user
            userDAO.delete(user);

            // Log the delete action
            Log log = new Log("INFO", "Admin " + currentUser.getName() + " deleted user: " + user.getName(), new Date().toString());
            logDAO.create(log);

            // Return success response
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "User deleted successfully.",
                    "user", Map.of(
                            "id", user.getId(),
                            "name", user.getName()
                    )
            ));
        } catch (SQLException e) {
            // Return error response
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Error deleting user: " + e.getMessage()
            ));
        }
    }
}