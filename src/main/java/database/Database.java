package database;

import model.User;

import java.sql.*;

public class Database {
    private static String url = "jdbc:sqlite:data.db";
    private static Connection conn = null;

    private Database() {

    }
    public static Connection getConnection() throws SQLException {
        if(conn !=null){
            System.out.println("Connection exists");
            return conn;
        }
        Connection conn = DriverManager.getConnection(url);
        System.out.println("connection made");
        createTableUser(conn);
        createTableTodos(conn);
        return conn;
    }

    public static void closeConnection(Connection connection) throws SQLException {
        connection.close();
    }

    public static void closeStatement(Statement statement) throws SQLException {
        statement.close();
    }

    public static void closePreparedStatement(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.close();
    }

    public static void closeResultSet(ResultSet resultSet) throws SQLException {
        resultSet.close();
    }

    /* setup database by creating a table for user */

    public static void createTableUser(Connection con) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS user (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	name text NOT NULL,\n"
                + "	password text NOT NULL,\n"
                + "	email text NOT NULL UNIQUE\n"
                + ");";

        Statement stmt = con.createStatement();
        stmt.execute(sql);
    }
    public static void createTableTodos(Connection con) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS todos (\n"
                + "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "    user_id INTEGER NOT NULL,\n"
                + "    task TEXT NOT NULL,\n"
                + "    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE\n"
                + ");";

        Statement stmt = con.createStatement();
        stmt.execute(sql);
    }

}

