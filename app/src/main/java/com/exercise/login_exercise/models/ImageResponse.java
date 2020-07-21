package com.exercise.login_exercise.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jongwow on 2020-07-21.
 */
public class ImageResponse {
    @SerializedName("msg")
    @Expose
    private String message;

    @SerializedName("url")
    @Expose
    private String url;

    public String getMessage() {
        return message;
    }

    public String getUrl() {
        return url;
    }
}
