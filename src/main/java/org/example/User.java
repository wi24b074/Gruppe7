package org.example;

public abstract class User {
    private final String userId;
    private String name;
    private String email;
    private String passwordHash;

    public User(String userId, String email, String name, String passwordHash) {
        this.userId = userId;
        this.email = email;
        this.name = name;
    }

    public String getUserId(){ return userId; }
    public String getName(){ return name; }
    public User setName(String name){ this.name = name; return this; }
    public String getEmail(){ return email; }
    public String getPasswordHash(){ return passwordHash; }
}
