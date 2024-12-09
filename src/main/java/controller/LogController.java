package controller;

import database.LogDAO;
import database.LogDAOImpl;
import model.Log;

import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    private final LogDAO<Log> logDAO;

    public LogController() {
        this.logDAO = new LogDAOImpl(); // Directly use the DAO implementation
    }

    @GetMapping
    public List<Log> getAllLogs() {
        try {
            return logDAO.readAll(); // Fetch all logs
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching logs", e);
        }
    }

    @GetMapping("/{level}")
    public List<Log> getLogsByLevel(@PathVariable String level) {
        try {
            return logDAO.readTableColumnQuery("log", "level", level); // Fetch logs filtered by level
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching logs by level", e);
        }
    }
}