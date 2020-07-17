package com.exercise.login_exercise.api;

import com.exercise.login_exercise.models.DefaultResponse;
import com.exercise.login_exercise.models.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by jongwow on 2020-07-17.
 */
public interface Api {

    @FormUrlEncoded
    @POST("/api/v1/user/fbLogin")
    Call<LoginResponse> fbLogin(
            @Field("name") String name,
            @Field("email") String email,
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("/api/v1/user/test")
    Call<DefaultResponse> testLogin(
            @Field("name") String name
    );

}
