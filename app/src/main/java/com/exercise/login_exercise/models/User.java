package com.exercise.login_exercise.models;

/**
 * Created by jongwow on 2020-07-17.
 */
public class User {
    private int userId;
    private String email, name;

    public User(int userId, String email, String name) {
        this.userId = userId;
        this.email = email;
        this.name = name;
    }

    public int getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
