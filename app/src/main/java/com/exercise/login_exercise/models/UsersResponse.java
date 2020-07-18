package com.exercise.login_exercise.models;

import java.util.ArrayList;

/**
 * Created by jongwow on 2020-07-18.
 */
public class UsersResponse {
    private boolean error;
    private ArrayList<User> users;

    public UsersResponse(boolean error,  ArrayList<User> users) {
        this.error = error;
        this.users = users;
    }

    public boolean isError() {
        return error;
    }

    public ArrayList<User> getUsers() {
        return users;
    }
}
