package com.exercise.login_exercise.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by jongwow on 2020-07-18.
 */
public class ImagesResponse {
    private boolean error;
    @SerializedName("msg")
    private String message;
    private ArrayList<String> urls;

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<String> getUrls() {
        return urls;
    }

    public ImagesResponse(boolean error, String message, ArrayList<String> urls) {
        this.error = error;
        this.message = message;
        this.urls = urls;
    }
}
