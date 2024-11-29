package monitor;

import model.Log;

import java.io.IOException;
import java.sql.SQLException;

public interface Observer {
    public void update(Log log) throws SQLException, IOException;
}