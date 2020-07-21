package com.exercise.login_exercise.api;

import com.exercise.login_exercise.models.ImageResponse;
import com.exercise.login_exercise.models.ImagesResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by jongwow on 2020-07-21.
 */
public interface GalleryService {
    @GET("/api/v1/image/list")
    Call<ImagesResponse> getImages();

    @Multipart
    @POST("/api/v1/image/upload")
    Call<ImageResponse> postImage(
            @Part MultipartBody.Part image,
            @Part("upload") RequestBody name
    );
}
