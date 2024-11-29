package database;

import java.sql.*;

public class Database {
    private static final String url = "jdbc:sqlite:data.db";
    private static Connection conn = null;

    private Database() {
        // Private constructor to prevent instantiation
    }

    public static Connection getConnection() throws SQLException {
        // Create a connection to the database
        Connection conn = DriverManager.getConnection(url);
        System.out.println("Connection made");

        // Set PRAGMA busy_timeout to avoid lock-related issues
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA busy_timeout = 5000"); // Wait up to 5 seconds for a lock to release
        }

        // Ensure tables exist
        createTableUser(conn);
        createTableTodos(conn);
        createTableLog(conn);

        return conn;
    }

    public static void closeConnection(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    public static void closeStatement(Statement statement) throws SQLException {
        if (statement != null) {
            statement.close();
        }
    }

    public static void closePreparedStatement(PreparedStatement preparedStatement) throws SQLException {
        if (preparedStatement != null) {
            preparedStatement.close();
        }
    }

    public static void closeResultSet(ResultSet resultSet) throws SQLException {
        if (resultSet != null) {
            resultSet.close();
        }
    }

    // Set up the database by creating necessary tables
    public static void createTableUser(Connection con) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS user (\n"
                + "    id INTEGER PRIMARY KEY,\n"
                + "    name TEXT NOT NULL,\n"
                + "    password TEXT NOT NULL,\n"
                + "    email TEXT NOT NULL UNIQUE,\n"
                + "    role TEXT DEFAULT 'USER',\n"
                + "    lock TEXT DEFAULT 'UNLOCKED'\n"
                + ");";
        try (Statement stmt = con.createStatement()) {
            stmt.execute(sql);
        }
    }

    public static void createTableTodos(Connection con) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS todos (\n"
                + "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "    user_id INTEGER NOT NULL,\n"
                + "    task TEXT NOT NULL,\n"
                + "    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE\n"
                + ");";
        try (Statement stmt = con.createStatement()) {
            stmt.execute(sql);
        }
    }

    public static void createTableLog(Connection con) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS log (\n"
                + "    id INTEGER PRIMARY KEY,\n"
                + "    level TEXT NOT NULL,\n"
                + "    message TEXT,\n"
                + "    created TEXT\n"
                + ");";
        try (Statement stmt = con.createStatement()) {
            stmt.execute(sql);
        }
    }
}