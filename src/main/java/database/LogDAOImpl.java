package database;

import model.Log;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LogDAOImpl implements LogDAO<Log> {

    @Override
    public int create(Log log) throws SQLException {
        String sql = "INSERT INTO log (level, message, created) VALUES ('"
                + log.getLevel() + "', '"
                + log.getMessage() + "', '"
                + log.getCreated() + "')";
        try (Connection con = Database.getConnection();
             Statement stmt = con.createStatement()) {
            return stmt.executeUpdate(sql);
        }
    }

    @Override
    public Log readOne(int id) throws SQLException {
        return null; // Not implemented for now
    }

    @Override
    public int update(Log log) throws SQLException {
        return 0; // Not implemented for now
    }

    @Override
    public int delete(Log log) throws SQLException {
        String sql = "DELETE FROM log WHERE id = " + log.getLogId();
        try (Connection con = Database.getConnection();
             Statement stmt = con.createStatement()) {
            return stmt.executeUpdate(sql);
        }
    }

    @Override
    public List<Log> readAll() throws SQLException {
        String sql = "SELECT * FROM log";
        List<Log> logs = new ArrayList<>();
        try (Connection con = Database.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Integer _id = rs.getInt("Id");
                String logLevel = rs.getString("level");
                String logMessage = rs.getString("message");
                String logCreated = rs.getString("created");
                Log log = new Log(_id, logLevel, logMessage, logCreated);
                logs.add(log);
            }
        }
        return logs;
    }

    @Override
    public List<Log> readTableColumnQuery(String tableName, String columnName, String columnValue) throws SQLException {
        String sql = "SELECT * FROM " + tableName + " WHERE " + columnName + " = '" + columnValue + "'";
        List<Log> logs = new ArrayList<>();
        try (Connection con = Database.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Integer _id = rs.getInt("Id");
                String logLevel = rs.getString("level");
                String logMessage = rs.getString("message");
                String logCreated = rs.getString("created");
                Log log = new Log(_id, logLevel, logMessage, logCreated);
                logs.add(log);
            }
        }
        return logs;
    }
}