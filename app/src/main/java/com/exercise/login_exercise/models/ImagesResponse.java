package com.exercise.login_exercise.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jongwow on 2020-07-18.
 */
public class ImagesResponse {
    private boolean error;
    @SerializedName("msg")
    private String message;
    private String url;

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getUrl() {
        return url;
    }

    public ImagesResponse(boolean error, String message, String url) {
        this.error = error;
        this.message = message;
        this.url = url;
    }
}
