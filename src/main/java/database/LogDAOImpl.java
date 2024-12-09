package database;

import model.Log;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LogDAOImpl implements LogDAO<Log> {
    //SQL Injection mitigation
    @Override
    public int create(Log log) throws SQLException {
        String sql = "INSERT INTO log (level, message, created) VALUES (?, ?, ?)";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, log.getLevel());
            ps.setString(2, log.getMessage());
            ps.setString(3, log.getCreated());
            return ps.executeUpdate();
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
        Connection con = Database.getConnection();
        String sql = "DELETE FROM log WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, log.getLogId());
        int result = ps.executeUpdate();
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);
        return result;
    }

    @Override
    public List<Log> readAll() throws SQLException {
        Connection con = Database.getConnection();
        String sql = "SELECT * FROM log";
        List<Log> logs = new ArrayList<>();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            Integer _id = rs.getInt("Id");
            String logLevel = rs.getString("level");
            String logMessage = rs.getString("message");
            String logCreated = rs.getString("created");
            Log log = new Log(_id, logLevel, logMessage, logCreated);
            logs.add(log);
        }
        return logs;
    }

    @Override
    public List<Log> readTableColumnQuery(String tableName, String columnName, String columnValue) throws SQLException {
        Connection con = Database.getConnection();
        String sql = "SELECT * FROM " + tableName + " WHERE " + columnName + " = ?";
        List<Log> logs = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, columnValue);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Integer _id = rs.getInt("Id");
                    String logLevel = rs.getString("level");
                    String logMessage = rs.getString("message");
                    String logCreated = rs.getString("created");
                    Log log = new Log(_id, logLevel, logMessage, logCreated);
                    logs.add(log);
                }
            }
        }
        return logs;
    }
}