package com.exercise.login_exercise.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.exercise.login_exercise.R;
import com.exercise.login_exercise.api.RetrofitClient;
import com.exercise.login_exercise.models.LoginResponse;
import com.exercise.login_exercise.storage.SharedPrefManager;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.widget.LoginButton;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

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

    // login email and password
    MaterialEditText edt_logic_email, edt_login_password;
    Button btn_login;
    TextView txt_create_account;

    //facebook
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    public static final int sub = 1001; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkSelfPermission();

        progressBar = findViewById(R.id.loading_spinner);
        ////////////////////////// facebook /////////////////////////
        callbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));

        ////////////////////////// User login ///////////////////////
        edt_logic_email = (MaterialEditText) findViewById(R.id.edt_email);
        edt_login_password = (MaterialEditText) findViewById(R.id.edt_password);
        txt_create_account = (TextView) findViewById(R.id.txt_create_account);
        txt_create_account.setOnClickListener(this);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

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
        String email = edt_logic_email.getText().toString(); // TODO: 바꾸기
        String password = edt_login_password.getText().toString(); // TODO: 바꾸기

        Call<LoginResponse> call = RetrofitClient
                .getInstance().getApi().login(email, password);

        // Set up progress before call
        progressBar.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressBar.setVisibility(View.GONE);

                LoginResponse loginResponse = response.body();
                if (response.code() == 200) {
                    Toast.makeText(LoginActivity.this, "[수신]" + loginResponse.getMessage(), Toast.LENGTH_LONG).show();

                    SharedPrefManager.getInstance(LoginActivity.this)
                            .saveUser(loginResponse.getUser());//TODO: Test용insert

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    finish();
                    startActivity(intent);

                } else if (response.code() == 400) {
                    Log.d(TAG, "onResponse: Body: Error" + response.errorBody().toString());
                    Toast.makeText(LoginActivity.this, "없는 아이디", Toast.LENGTH_LONG).show();
                } else {
                    Log.d(TAG, "onResponse: Body: Error" + response.errorBody().toString());
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

    private void userRegister() {
        // create account 버튼을 누르면 register_layout.xml 이 inflate
        final View register_layout = LayoutInflater.from(LoginActivity.this).inflate(R.layout.register_layout, null);

        // register 창 library, MaterialStyledDialog.Builder(this) 가 기본 사용 방법
        new MaterialStyledDialog.Builder(LoginActivity.this)
                .setTitle("REGISTRATION")
                .setDescription("Please fill all fields")
                .setCustomView(register_layout)
                // cancel 버튼을 누르면 register 창이 꺼진다
                .setNegativeText("Cancel")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                // register 버튼, 예외조건 처리, 데이터베이스와 연결
                .setPositiveText("REGISTER")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        MaterialEditText edt_register_email = (MaterialEditText) register_layout.findViewById(R.id.edt_email);
                        MaterialEditText edt_register_name = (MaterialEditText) register_layout.findViewById(R.id.edt_name);
                        MaterialEditText edt_register_password = (MaterialEditText) register_layout.findViewById(R.id.edt_password);
                        MaterialEditText edt_register_phone = (MaterialEditText) register_layout.findViewById(R.id.edt_phone);

                        Call<LoginResponse> call = RetrofitClient
                                .getInstance().getApi().register(edt_register_email.getText().toString(), edt_register_name.getText().toString(), edt_register_password.getText().toString(), edt_register_phone.getText().toString());

                        if (TextUtils.isEmpty(edt_register_email.getText().toString())) {
                            Toast.makeText(LoginActivity.this, "email cannot be null or empty", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(edt_register_name.getText().toString())) {
                            Toast.makeText(LoginActivity.this, "name cannot be null or empty", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(edt_register_password.getText().toString())) {
                            Toast.makeText(LoginActivity.this, "password cannot be null or empty", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // POST
                        call.enqueue(new Callback<LoginResponse>() {
                            @Override
                            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                                progressBar.setVisibility(View.GONE);

                                LoginResponse loginResponse = response.body();
                                if (response.code() == 200) {
                                    Toast.makeText(LoginActivity.this, "[수신]" + loginResponse.getMessage(), Toast.LENGTH_LONG).show();

                                    SharedPrefManager.getInstance(LoginActivity.this)
                                            .saveUser(loginResponse.getUser());//TODO: Test용insert

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    finish();
                                    startActivity(intent);

                                } else if (response.code() == 404) {
                                    Log.d(TAG, "onResponse: Body: Error" + response.errorBody().toString());
                                    Toast.makeText(LoginActivity.this, "없는 아이디", Toast.LENGTH_LONG).show();
                                } else {
                                    Log.d(TAG, "onResponse: Body: Error" + response.errorBody().toString());
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
                }).show();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_login) {
            userLogin();
        } else if (view.getId() == R.id.txt_create_account) {
            // register function
            userRegister();
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

    //////////////////////////////// facebook //////////////////////////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {
                Toast.makeText(LoginActivity.this, "User Logged out", Toast.LENGTH_LONG).show();
            } else loaduserProfile(currentAccessToken);
        }
    };

    private void loaduserProfile(AccessToken newAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(intent, sub);

                String first_name = null;
                String last_name = null;
                String email = null;
                String id = null;
                try {

                    first_name = object.getString("first_name");
                    last_name = object.getString("last_name");
                    email = object.getString("email");
                    id = object.getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Call<LoginResponse> call = RetrofitClient
                        .getInstance().getApi().facebookLogin(email, last_name+first_name, id);
                // POST
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        progressBar.setVisibility(View.GONE);

                        LoginResponse loginResponse = response.body();
                        if (response.code() == 200) {
                            Toast.makeText(LoginActivity.this, "[수신]" + loginResponse.getMessage(), Toast.LENGTH_LONG).show();

                            SharedPrefManager.getInstance(LoginActivity.this)
                                    .saveUser(loginResponse.getUser());//TODO: Test용insert

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            finish();
                            startActivity(intent);

                        } else if (response.code() == 404) {
                            Log.d(TAG, "onResponse: Body: Error" + response.errorBody().toString());
                            Toast.makeText(LoginActivity.this, "없는 아이디", Toast.LENGTH_LONG).show();
                         } else {
                            Log.d(TAG, "onResponse: Body: Error" + response.errorBody().toString());
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
        });

//        tokenTracker.startTracking();
        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name, last_name, email, id");
        request.setParameters(parameters);
        request.executeAsync();
    }
}
