package database;

import java.sql.SQLException;

public interface DAO<T> {

    int create(T t) throws SQLException;
    T readOne(int id) throws SQLException;
    int update(T t) throws SQLException;
    int delete(T t) throws SQLException;

}
