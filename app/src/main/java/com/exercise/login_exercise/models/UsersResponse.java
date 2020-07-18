package com.exercise.login_exercise.models;

import java.util.List;

/**
 * Created by jongwow on 2020-07-18.
 */
public class UsersResponse {
    private boolean error;
    private List<User> users;

    public UsersResponse(boolean error,  List<User> users) {
        this.error = error;
        this.users = users;
    }

    public boolean isError() {
        return error;
    }

    public List<User> getUsers() {
        return users;
    }
}
