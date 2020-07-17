package com.exercise.login_exercise.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jongwow on 2020-07-17.
 */
public class DefaultResponse {

    private String msg;

    public DefaultResponse(String msg){
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
