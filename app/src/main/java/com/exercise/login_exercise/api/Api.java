package com.exercise.login_exercise.api;

import com.exercise.login_exercise.models.DefaultResponse;
import com.exercise.login_exercise.models.ImagesResponse;
import com.exercise.login_exercise.models.LoginResponse;
import com.exercise.login_exercise.models.UsersResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by jongwow on 2020-07-17.
 */
public interface Api {

    @FormUrlEncoded
    @POST("/api/v3/register")
    Call<LoginResponse> register(
            @Field("email") String email,
            @Field("name") String name,
            @Field("password") String password,
            @Field("phone") String phone
    );

    @FormUrlEncoded
    @POST("/api/v3/login")
    Call<LoginResponse> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("/api/v3/facebook")
    Call<LoginResponse> facebookLogin(
            @Field("email") String email,
            @Field("name") String name,
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("/api/v3/check")
    Call<DefaultResponse> tempUser(
            @Field("temperature") String temperature,
            @Field("email") String email
    );

    @GET("/api/v3/list")
    Call<UsersResponse> getUsers();

    @GET("/api/v3/qr")
    Call<DefaultResponse> getQR(
            @Query("token") String token
    );

    ///////////////////////////////////////// previous version ////////////////////////////////////////
    @GET("/api/v1/image/list")
    Call<ImagesResponse> getImages();


    @Multipart
    @POST("/api/v1/image/upload")
    Call<DefaultResponse> postImage(@Part MultipartBody.Part image, @Part("upload")RequestBody name);
}
