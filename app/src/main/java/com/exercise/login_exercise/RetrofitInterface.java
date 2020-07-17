package com.exercise.login_exercise;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by jongwow on 2020-07-17.
 */
public interface RetrofitInterface {

    @POST("/api/v1/user/login")
    Call<LoginResult> executeLogin(@Body HashMap<String, String> map);

    @POST("/api/v1/user/signup")
    Call<Void> executeSignup(@Body HashMap<String, String> map);
}
