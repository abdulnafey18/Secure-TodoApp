package model;

public class LogginUser {
    private static User user;

    public LogginUser() {}

    public static void setUser(User user){
        LogginUser.user = user;
    }
    public static User getUser(){
        return LogginUser.user;
    }
}

