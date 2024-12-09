package controller;

import database.UserDAO;
import database.UserDAOImpl;
import model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.util.HtmlUtils;
import security.Encryption;

import java.sql.SQLException;
// Cross site scripting mitigation
@RestController
@RequestMapping("/auth")
public class LoginController {

    private final UserDAO userDAO;

    public LoginController() {
        this.userDAO = new UserDAOImpl(); // Directly use the DAO implementation
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest, HttpSession session) {
        try {
            // Sanitize inputs
            String sanitizedUsername = HtmlUtils.htmlEscape(loginRequest.getName());
            loginRequest.setName(sanitizedUsername);

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
            boolean passwordMatches = Encryption.verifyPassword(
                    loginRequest.getPassword(), existingUser.getPassword()
            );

            if (!passwordMatches) {
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