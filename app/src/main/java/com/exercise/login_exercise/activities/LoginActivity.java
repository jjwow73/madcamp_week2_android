package com.exercise.login_exercise.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.exercise.login_exercise.R;
import com.exercise.login_exercise.api.RetrofitClient;
import com.exercise.login_exercise.models.DefaultResponse;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logic);

        editText = findViewById(R.id.editText);

        findViewById(R.id.buttonLogin).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void userLogin() {
        String testString = editText.getText().toString().trim();
        if (testString.isEmpty()) {
            editText.setError("빈칸임");
            editText.requestFocus();
            return;
        }
        Call<DefaultResponse> call = RetrofitClient
                .getInstance().getApi().testLogin(testString);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse defaultResponse = response.body();
                if (response.code() == 200) {
                    Toast.makeText(LoginActivity.this, "[수신]" + defaultResponse.getMsg(), Toast.LENGTH_LONG).show();
                    SharedPrefManager.getInstance(LoginActivity.this)
                            .saveId(1);//TODO: Test용
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else if (response.code() == 400){
                    Log.d(TAG, "onResponse: Body: Error");
                    Toast.makeText(LoginActivity.this, "Not Park!", Toast.LENGTH_LONG).show();
                }else {
                    Log.d(TAG, "onResponse: Body: Error");
                    Toast.makeText(LoginActivity.this, "요청 실패!", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

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
