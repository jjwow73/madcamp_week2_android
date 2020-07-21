package com.exercise.login_exercise.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.exercise.login_exercise.R;
import com.exercise.login_exercise.activities.CustomScannerActivity;
import com.exercise.login_exercise.activities.TempDialogActivity;
import com.exercise.login_exercise.api.RetrofitClient;
import com.exercise.login_exercise.models.DefaultResponse;
import com.exercise.login_exercise.models.User;
import com.exercise.login_exercise.storage.SharedPrefManager;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jongwow on 2020-07-19.
 */
public class ScannerFragment extends Fragment implements View.OnClickListener, NumberPicker.OnValueChangeListener {
    private static final String TAG = "ScannerFragment";
    private Context mContext;
    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;

    private TextView userName;
    private TextView userPhone;
    private TextView userEmail;
    private Button tempBtn;
    private Button attendanceBtn;

    // server로 보낼 info
    private double tempValue = 0;
    private String QRString;

    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        startScanner();
        return inflater.inflate(R.layout.fragment_scanner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        User user;

        userName = getActivity().findViewById(R.id.userName);
        userPhone = getActivity().findViewById(R.id.userPhoneNumber);
        userEmail = getActivity().findViewById(R.id.userEmail);
        tempBtn = getActivity().findViewById(R.id.tempBtn);
        attendanceBtn = getActivity().findViewById(R.id.attendanceBtn);
        // setting progress bar
        progressBar = view.findViewById(R.id.loading_spinner);
        progressBar.setVisibility(View.VISIBLE);

        // 1. 버튼 둘다 enable false
        // 2. request 200 이 오는 경우 체온 측정 버튼 clickable
        // 3. 체온 측정 ok 하면 attendance 버튼 clickable
        // 4. attendance 버튼을 누르면 tempvalue, email 서버로 보냄


        tempBtn.setOnClickListener(this);
        attendanceBtn.setOnClickListener(this);
    }

    private void startScanner() {
        Log.d(TAG, "startScanner: 클릭했는데 왜 안되지");
        Toast.makeText(mContext, "벝은 클릑", Toast.LENGTH_SHORT).show();
        // QR Scanner 실행
        IntentIntegrator.forSupportFragment(this).setCaptureActivity(CustomScannerActivity.class).initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // QR scanner 결과
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(mContext, "QR 코드에 암것도 엄서용", Toast.LENGTH_SHORT).show();
            } else {
                QRString = intentResult.getContents();
                Toast.makeText(mContext, "QR code[ " + QRString + " ]", Toast.LENGTH_SHORT).show();
                // GET
                Call<DefaultResponse> call1 = RetrofitClient.getInstance().getApi().getQR(QRString);
                call1.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                        progressBar.setVisibility(View.GONE);
                        if (response.code() == 200) {
                            // 학생의 정보 setting
                            userName.setText(SharedPrefManager.getInstance(getActivity()).getUser().getName());
                            userPhone.setText(SharedPrefManager.getInstance(getActivity()).getUser().getPhone());
                            userEmail.setText(SharedPrefManager.getInstance(getActivity()).getUser().getEmail());

                            // QR 코드 잘 찍었으면 체온 측정 버튼 켜짐
                            tempBtn.setEnabled(true);
                        } else {
                            Toast.makeText(getContext(), "[실패]", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DefaultResponse> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "요청 실패", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
        tempValue = 36 + numberPicker.getValue() * 0.1;
        Toast.makeText(mContext, Double.toString(tempValue), Toast.LENGTH_SHORT).show();
    }

    public void showTempPicker(View view, String title, String subtitle, int maxvalue, int minvalue, int step, int defvalue) {
        TempDialogActivity newFragment = new TempDialogActivity();

        //Dialog에는 bundle을 이용해서 파라미터를 전달한다
        Bundle bundle = new Bundle(6); // 파라미터는 전달할 데이터 개수
        bundle.putString("title", title);
        bundle.putString("subtitle", subtitle);
        bundle.putInt("maxvalue", maxvalue);
        bundle.putInt("minvalue", minvalue);
        bundle.putInt("step", step);
        bundle.putInt("defvalue", defvalue);
        newFragment.setArguments(bundle);
        //class 자신을 Listener로 설정한다
        newFragment.setValueChangeListener(this);
        newFragment.show(getFragmentManager(), "number picker");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.tempBtn):
                showTempPicker(view, "체온 측정", "체온을 정확히 입력해주세요", 38, 36, 1, 36);
                break;
            case (R.id.attendanceBtn):
                progressBar.setVisibility(View.VISIBLE);
                // POST, tempValue, email 값 서버로 전달
                Call<DefaultResponse> call3 = RetrofitClient.getInstance().getApi().tempUser(Double.toString(tempValue), SharedPrefManager.getInstance(getActivity()).getUser().getEmail());
                call3.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                        progressBar.setVisibility(View.GONE);
                        DefaultResponse defaultResponse = response.body();
                        if (response.code() == 200) {
                            SharedPrefManager.getInstance(getActivity()).saveLastChecked(new Date().toString());
                            SharedPrefManager.getInstance(getActivity()).saveTemperature(Double.toString(tempValue));


                            Toast.makeText(getActivity(), "[수신]" + defaultResponse.getMsg(), Toast.LENGTH_LONG).show();
                        } else if (response.code() == 409) {
                            Log.d(TAG, "onResponse: Body: Error" + response.errorBody().toString());
                            Toast.makeText(getActivity(), "[실패] 이미 출석체크함", Toast.LENGTH_LONG).show();
                        } else if (response.code() == 400) {
                            Log.d(TAG, "onResponse: Body: Error" + response.errorBody().toString());
                            Toast.makeText(getActivity(), "[실패] 잘못된 요청(이메일or온도없음)", Toast.LENGTH_LONG).show();
                        } else if (response.code() == 500) {
                            Log.d(TAG, "onResponse: Body: Error" + response.errorBody().toString());
                            Toast.makeText(getActivity(), "[실패] 서버문제. 관리자한테 문의하세요.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DefaultResponse> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "[요청실패]", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: 요청실패");
                    }
                });
                break;
        }
    }
}
