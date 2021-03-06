package com.exercise.login_exercise.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jongwow on 2020-07-17.
 */
public class LoginResponse {
    private boolean error;
    @SerializedName("msg")
    private String message;
    private User user;

    public LoginResponse(boolean error, String message, User user) {
        this.error = error;
        this.message = message;
        this.user = user;
    }

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }
}
