package controller;

import database.UserDAO;
import database.UserDAOImpl;
import model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;
import register.PasswordFactory;
import security.Encryption;

import java.sql.SQLException;
// Cross site scripting mitigation
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
            // Sanitize inputs
            String sanitizedName = HtmlUtils.htmlEscape(user.getName());
            String sanitizedEmail = HtmlUtils.htmlEscape(user.getEmail());
            user.setName(sanitizedName);
            user.setEmail(sanitizedEmail);

            PasswordFactory passwordFactory = new PasswordFactory();
            String validationMessage = passwordFactory.confirmPassworValid(user.getPassword(), user.getPassword());

            if (!"OK".equals(validationMessage)) {
                return ResponseEntity.badRequest().body(HtmlUtils.htmlEscape(validationMessage));
            }

            // Hash the password before storing
            String hashedPassword = Encryption.hashPassword(user.getPassword());
            user.setPassword(hashedPassword);

            // Save user to database
            userDAO.create(user);
            return ResponseEntity.ok("User registered successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("An error occurred during registration. Please try again.");
        }
    }
}