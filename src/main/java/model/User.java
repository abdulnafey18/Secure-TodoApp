package model;

public class User {
    private int id;
    private String name;
    private String password;
    private String email;
    private LOCK lock; // Lock status (LOCKED or UNLOCKED)
    private ROLE role; // User role (ADMIN, USER)

    // Default Constructor
    public User() {
        this.id = 0;
        this.name = "";
        this.password = "";
        this.email = "";
        this.lock = LOCK.UNLOCKED; // Default to UNLOCKED
        this.role = ROLE.USER;     // Default to USER
    }

    // Parameterized Constructor
    public User(int id, String name, String password, String email, String role, String lock) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;

        // Handle null values and assign defaults
        this.role = role != null ? ROLE.valueOf(role.toUpperCase()) : ROLE.USER;
        this.lock = lock != null ? LOCK.valueOf(lock.toUpperCase()) : LOCK.UNLOCKED;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LOCK getLock() {
        return lock;
    }

    public void setLock(LOCK lock) {
        this.lock = lock;
    }

    public ROLE getRole() {
        return role;
    }

    public void setRole(ROLE role) {
        this.role = role;
    }

    // Enums for Role and Lock Status
    public enum LOCK { LOCKED, UNLOCKED }
    public enum ROLE { ADMIN, USER }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", lock=" + lock +
                ", role=" + role +
                '}';
    }
}