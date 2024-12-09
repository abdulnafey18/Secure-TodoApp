package controller;

import database.UserDAO;
import database.UserDAOImpl;
import model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import register.PasswordFactory;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/register")
public class RegisterController {

    private final UserDAO userDAO;

    public RegisterController() {
        this.userDAO = new UserDAOImpl();
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            PasswordFactory passwordFactory = new PasswordFactory();
            String validationMessage = passwordFactory.confirmPassworValid(user.getPassword(), user.getPassword());

            if (!"OK".equals(validationMessage)) {
                return ResponseEntity.badRequest().body(validationMessage);
            }

            // Store the password in plaintext
            System.out.println("Plaintext Password for Registration: " + user.getPassword());

            // Save user to database with plaintext password
            userDAO.create(user);
            return ResponseEntity.ok("User registered successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("An error occurred during registration. Please try again.");
        }
    }
}