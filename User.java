package com.example.cafepaykict;

public abstract class User {
    protected String username;
    protected String password;
    protected String name;
    protected String role;

    public User(String username, String password, String name, String role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public String getRole() { return role; }

}

