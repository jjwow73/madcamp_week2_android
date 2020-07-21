package com.exercise.login_exercise.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.exercise.login_exercise.api.GalleryService;
import com.exercise.login_exercise.models.ImageResponse;
import com.exercise.login_exercise.models.ImagesResponse;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jongwow on 2020-07-21.
 */
public class GalleryRepository {
    private static final String TAG = "GalleryRepository";
    private static final String BASE_URL = "http://192.249.19.243:8780";

    private GalleryService galleryService;
    private MutableLiveData<ImagesResponse> imagesResponseLiveData;

    public GalleryRepository() {
        imagesResponseLiveData = new MutableLiveData<>();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        galleryService = new retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GalleryService.class);
    }

    public void getImages() {
        galleryService.getImages()
                .enqueue(new Callback<ImagesResponse>() {
                    @Override
                    public void onResponse(Call<ImagesResponse> call, Response<ImagesResponse> response) {
                        if (response.body() != null) {
                            imagesResponseLiveData.postValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<ImagesResponse> call, Throwable t) {
                        imagesResponseLiveData.postValue(null);
                    }
                });
    }

    public void postImage(MultipartBody.Part image,
                            RequestBody name) {
        galleryService.postImage(image, name)
                .enqueue(new Callback<ImageResponse>() {
                    @Override
                    public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                        if(response.body() != null){
                            Log.d(TAG, "onResponse: 업로드 성공"+response.body().getUrl());
                            getImages();
                        }
                    }

                    @Override
                    public void onFailure(Call<ImageResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: 업로드 실패");
                    }
                });
    }

    public LiveData<ImagesResponse> getImagesResponseLiveData() {
        return imagesResponseLiveData;
    }
}
