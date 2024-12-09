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
            if (loginRequest.getName() == null || loginRequest.getPassword() == null) {
                return ResponseEntity.badRequest().body("Username or password is missing");
            }

            // Retrieve user by username
            User existingUser = userDAO.readLoggedInUser(loginRequest.getName(), loginRequest.getPassword());
            if (existingUser == null) {
                return ResponseEntity.badRequest().body("Invalid credentials");
            }

            // Check if the user is locked
            if (existingUser.getLock() == User.LOCK.LOCKED) {
                return ResponseEntity.badRequest().body("User account is locked. Please contact support.");
            }

            // Verify password
            if (!loginRequest.getPassword().equals(existingUser.getPassword())) {
                return ResponseEntity.badRequest().body("Invalid credentials");
            }

            // Store user in session
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