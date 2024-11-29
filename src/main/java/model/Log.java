package model;

import java.io.Serializable;

public class Log implements Serializable {
    public int logId;
    public String level;
    public String message;

    public  String created;

    public Log(String level, String message, String created) {
        this.logId = 0;
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
    public String toString(){
        return this.logId + ":"+this.level+" :"+this.message+" :"+this.created;
    }

}
