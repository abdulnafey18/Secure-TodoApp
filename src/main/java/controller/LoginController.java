package controller;

import database.UserDAO;
import database.UserDAOImpl;
import model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private final UserDAO userDAO;

    public LoginController() {
        this.userDAO = new UserDAOImpl();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest, HttpSession session) {
        try {
            // Call DAO method to fetch user
            User existingUser = userDAO.readLoggedInUser(loginRequest.getName(), loginRequest.getPassword());

            // If no user is found, return an error
            if (existingUser == null) {
                return ResponseEntity.badRequest().body("Invalid credentials");
            }

            // Skip password verification for testing SQL injection
            session.setAttribute("currentUser", existingUser);
            return ResponseEntity.ok(existingUser);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("An error occurred during login. Please try again.");
        }
    }

    @GetMapping("/currentUser")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return ResponseEntity.status(401).body("User is not logged in");
        }
        return ResponseEntity.ok(currentUser);
    }
}