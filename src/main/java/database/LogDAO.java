package database;

import model.Log;
import model.Log;

import java.sql.SQLException;
import java.util.List;

public interface LogDAO<L> extends DAO<L>{
    int create(Log log) throws SQLException;

    int update(Log log) throws SQLException;

    List<Log> readAll() throws SQLException;

    List<Log> readTableColumnQuery(String a, String b, String c) throws SQLException;


}
