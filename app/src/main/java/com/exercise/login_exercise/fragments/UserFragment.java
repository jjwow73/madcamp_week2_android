package com.exercise.login_exercise.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.exercise.login_exercise.R;
import com.exercise.login_exercise.adapters.UserAdapter;
import com.exercise.login_exercise.api.RetrofitClient;
import com.exercise.login_exercise.models.User;
import com.exercise.login_exercise.models.UsersResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jongwow on 2020-07-18.
 */
public class UserFragment extends Fragment{

    private static final String TAG = "UserFragment";

    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private ArrayList<User> userList;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Set up progress before call
        progressBar = view.findViewById(R.id.loading_spinner);
        progressBar.setVisibility(View.VISIBLE);


        Call<UsersResponse> call = RetrofitClient.getInstance().getApi().getUsers();

        call.enqueue(new Callback<UsersResponse>() {
            @Override
            public void onResponse(Call<UsersResponse> call, Response<UsersResponse> response) {
                progressBar.setVisibility(View.GONE);

                if (response.code() == 200) {
                    userList = response.body().getUsers();
                    Log.d(TAG, "onResponse: getUsers"+userList);
                    adapter = new UserAdapter(getActivity(), userList);
                    recyclerView.setAdapter(adapter);
                }
                else{
                    Toast.makeText(getContext(), "잘못된 요청["+response.code()+"]", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UsersResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "요청 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
