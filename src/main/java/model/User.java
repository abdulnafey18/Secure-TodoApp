package model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class User {
    private int id;
    private String name;
    private String password;
    private String email;
    public ObjectProperty<LOCK> lock;
    public ObjectProperty<ROLE> role;

    public User() {
        this.id = 0;
        this.name = "";
        this.password = "";
        this.email = "";
        this.lock = new SimpleObjectProperty<>(LOCK.UNLOCKED); // Default to UNLOCKED
        this.role = new SimpleObjectProperty<>(ROLE.USER);     // Default to USER
    }

    public User(Integer id, String name, String password, String email, String role, String lock) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;

        // Handle null values and assign defaults
        this.role = new SimpleObjectProperty<>(ROLE.valueOf(role != null ? role : "USER"));
        this.lock = new SimpleObjectProperty<>(LOCK.valueOf(lock != null ? lock : "UNLOCKED"));
    }

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
        return lock.get();
    }

    public ObjectProperty<LOCK> lockProperty() {
        return lock;
    }

    public void setLock(LOCK lock) {
        this.lock.set(lock);
    }

    public ROLE getRole() {
        return role.get();
    }

    public ObjectProperty<ROLE> roleProperty() {
        return role;
    }

    public void setRole(ROLE role) {
        this.role.set(role);
    }

    public enum LOCK {LOCKED, UNLOCKED}
    public enum ROLE {ADMIN, USER, TEST}
}