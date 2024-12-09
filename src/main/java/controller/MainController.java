package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String homePage() {
        return "index"; // Render index.html
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register"; // Render register.html
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Render login.html
    }
}