package com.exercise.login_exercise.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.exercise.login_exercise.R;
import com.exercise.login_exercise.api.RetrofitClient;
import com.exercise.login_exercise.models.LoginResponse;
import com.exercise.login_exercise.storage.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jongwow on 2020-07-17.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private EditText editText;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editText = findViewById(R.id.editText);

        findViewById(R.id.buttonLogin).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            finish();
            startActivity(intent);
        }
    }

    private void userLogin() {
        String id = editText.getText().toString().trim();
        if (id.isEmpty()) {
            editText.setError("빈칸임");
            editText.requestFocus();
            return;
        }

        String name = "kim"; // TODO: 바꾸기
        String email = "test"; // TODO: 바꾸기
        Call<LoginResponse> call = RetrofitClient
                .getInstance().getApi().login(name,email, id );

        // Set up progress before call
        progressBar = findViewById(R.id.loading_spinner);
        progressBar.setVisibility(View.VISIBLE);



        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressBar.setVisibility(View.GONE);


                LoginResponse loginResponse = response.body();
                if (response.code() == 200) {
                    Toast.makeText(LoginActivity.this, "[수신]" + loginResponse.getMessage(), Toast.LENGTH_LONG).show();
                    SharedPrefManager.getInstance(LoginActivity.this)
                            .saveUser(loginResponse.getUser());//TODO: Test용
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    finish();
                    startActivity(intent);
                } else if (response.code() == 404){
                    Log.d(TAG, "onResponse: Body: Error"+response.errorBody());
                    Toast.makeText(LoginActivity.this, "없는 아이디", Toast.LENGTH_LONG).show();
                }else {
                    Log.d(TAG, "onResponse: Body: Error"+response.errorBody());
                    Toast.makeText(LoginActivity.this, "요청 실패!", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "[요청실패]", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: 요청실패");
            }

        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonLogin) {
            userLogin();
        }
    }
}
