package com.exercise.login_exercise;


import com.google.gson.annotations.SerializedName;

/**
 * Created by jongwow on 2020-07-17.
 */
public class LoginResult {
    private String name;
    private String email;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
// @SerializedName("email") // 응답값과 객체의 속성으로 가질 값이 다른 이름을 가질 경우, 이 어노테이션을 사용함.

}
