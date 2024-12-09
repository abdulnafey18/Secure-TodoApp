package model;

public class Todo {
    private int id;
    private int userId;
    private String task;

    // Constructors
    public Todo() {}

    public Todo(int id, int userId, String task) {
        this.id = id;
        this.userId = userId;
        this.task = task;
    }

    public Todo(int userId, String task) {
        this.userId = userId;
        this.task = task;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", userId=" + userId +
                ", task='" + task + '\'' +
                '}';
    }
}