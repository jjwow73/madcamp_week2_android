package com.exercise.login_exercise.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
        checkSelfPermission();

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
                .getInstance().getApi().login(name, email, id);

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

                } else if (response.code() == 404) {
                    Log.d(TAG, "onResponse: Body: Error" + response.errorBody());
                    Toast.makeText(LoginActivity.this, "없는 아이디", Toast.LENGTH_LONG).show();
                } else {
                    Log.d(TAG, "onResponse: Body: Error" + response.errorBody());
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

    public void checkSelfPermission() {
        String tmp = "";
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            tmp += Manifest.permission.READ_EXTERNAL_STORAGE + " ";
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            tmp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            tmp += Manifest.permission.CAMERA + " ";
        }
        if (!TextUtils.isEmpty(tmp)) { // 권한 요청
            ActivityCompat.requestPermissions(this, tmp.trim().split(" "), 1);
        } else { // 모두 허용 상태
            Toast.makeText(this, "권한을 모두 허용", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {//권한을 허용 했을 경우
        if (requestCode == 1) {
            int length = permissions.length;
            for (int i = 0; i < length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {// 동의
                    Log.d("LoginActivity", "권한 허용 : " + permissions[i]);
                }
            }

        }
    }
}
