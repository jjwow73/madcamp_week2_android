package com.exercise.login_exercise.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.exercise.login_exercise.models.ImagesResponse;
import com.exercise.login_exercise.repositories.GalleryRepository;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by jongwow on 2020-07-21.
 */
public class GalleryViewModel extends AndroidViewModel {
    private GalleryRepository galleryRepository;
    private LiveData<ImagesResponse> imagesResponseLiveData;

    public GalleryViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(){
        galleryRepository = new GalleryRepository();
        imagesResponseLiveData = galleryRepository.getImagesResponseLiveData();
    }

    public void getImages(){
        galleryRepository.getImages();
    }
    public LiveData<ImagesResponse> getImagesResponseLiveData(){
        return imagesResponseLiveData;
    }


    public void postImage(MultipartBody.Part parts, RequestBody name) {
        galleryRepository.postImage(parts, name);
    }
}
