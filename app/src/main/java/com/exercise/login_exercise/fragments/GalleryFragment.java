package com.exercise.login_exercise.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.exercise.login_exercise.R;
import com.exercise.login_exercise.activities.LoginActivity;
import com.exercise.login_exercise.adapters.GalleryAdapter;
import com.exercise.login_exercise.adapters.UserAdapter;
import com.exercise.login_exercise.api.RetrofitClient;
import com.exercise.login_exercise.models.ImagesResponse;
import com.exercise.login_exercise.models.UsersResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jongwow on 2020-07-18.
 */
public class GalleryFragment extends Fragment {
    private static final String TAG = "GalleryFragment";

    private RecyclerView recyclerView;
    private GalleryAdapter adapter;
    private ArrayList<String> urls;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        FloatingActionButton fab = view.findViewById(R.id.image_fab);
        fab.setOnClickListener(new FABClickListener());

        Call<ImagesResponse> call = RetrofitClient.getInstance().getApi().getImages();

        // Set up progress before call
        progressBar = view.findViewById(R.id.loading_spinner);
        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<ImagesResponse>() {
            @Override
            public void onResponse(Call<ImagesResponse> call, Response<ImagesResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.code() == 200) {
                    urls = response.body().getUrls();
                    Log.d(TAG, "onResponse: getUrls"+urls);
                    adapter = new GalleryAdapter(getActivity(), urls);
                    recyclerView.setAdapter(adapter);
                }
                else{
                    Toast.makeText(getContext(), "요청 실패["+response.code()+"]", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ImagesResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "요청 실패!", Toast.LENGTH_LONG).show();
            }
        });
    }
    class FABClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            try {
                Toast.makeText(getContext(), "우왕 눌렸따", Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
