package model;

import java.io.Serializable;

public class Log implements Serializable {
    private int logId;
    private String level;
    private String message;
    private String created;

    // Constructors
    public Log() {}

    public Log(String level, String message, String created) {
        this.level = level;
        this.message = message;
        this.created = created;
    }

    public Log(int logId, String level, String message, String created) {
        this.logId = logId;
        this.level = level;
        this.message = message;
        this.created = created;
    }

    // Getters and Setters
    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "Log{" +
                "logId=" + logId +
                ", level='" + level + '\'' +
                ", message='" + message + '\'' +
                ", created='" + created + '\'' +
                '}';
    }
}