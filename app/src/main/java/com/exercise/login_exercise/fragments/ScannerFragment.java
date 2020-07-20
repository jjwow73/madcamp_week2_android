package com.exercise.login_exercise.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.exercise.login_exercise.adapters.UserAdapter;
import com.exercise.login_exercise.api.RetrofitClient;
import com.exercise.login_exercise.models.User;
import com.exercise.login_exercise.models.UsersResponse;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jongwow on 2020-07-19.
 */
public class ScannerFragment extends Fragment implements View.OnClickListener, NumberPicker.OnValueChangeListener{
    private static final String TAG = "ScannerFragment";
    private Context mContext;
    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;

    private TextView userName;
    private TextView userLevel;
    private TextView userPhone;
    private TextView userEmail;
    private Button tempBtn;
    private Button attendanceBtn;

    private double tempValue = 0;

    private ArrayList<User> userList;
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
//        progressBar = view.findViewById(R.id.loading_spinner);
//        progressBar.setVisibility(View.VISIBLE);


        // 1. 버튼 둘다 enable false
        // 2. request 200 이 오는 경우 체온 측정 버튼 clickable
        // 3. 체온 측정 ok 하면 attendance 버튼 clickable
        // 4. attendance 버튼을 누르면 tempvalue, email 서버로 보냄

        // GET
        // QR code contents
        Call<UsersResponse> call = RetrofitClient.getInstance().getApi().getUsers();
        call.enqueue(new Callback<UsersResponse>() {
                         @Override
                         public void onResponse(Call<UsersResponse> call, Response<UsersResponse> response) {
                             progressBar.setVisibility(View.GONE);

                             if (response.code() == 200) {
//                                 userList = response.body().getUsers();
                                 Log.d(TAG, "onResponse: getUsers"+userList);
                                 // 학생의 정보 setting
//                                 userName.setText(user.getName());
//                                 userPhone.setText(user.getPhone());
//                                 userEmail.setText(user.getEmail());

                                 // QR 코드 잘 찍었으면 체온 측정 버튼 켜짐
                                 tempBtn.setEnabled(true);
                             } else {
                                 Toast.makeText(getContext(), "잘못된 요청["+response.code()+"]", Toast.LENGTH_SHORT).show();
                             }
                         }

                         @Override
                         public void onFailure(Call<UsersResponse> call, Throwable t) {
                             progressBar.setVisibility(View.GONE);
                             Toast.makeText(getContext(), "요청 실패", Toast.LENGTH_SHORT).show();
                         }
                     });

        tempBtn.setOnClickListener(this);
        attendanceBtn.setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String barcode = intentResult.getContents();
        Toast.makeText(mContext, "barcode[ " + barcode + " ]", Toast.LENGTH_SHORT).show();

    }

    private void startScanner() {
        Log.d(TAG, "startScanner: 클릭했는데 왜 안되지");
        Toast.makeText(mContext, "벝은 클릑", Toast.LENGTH_SHORT).show();
        IntentIntegrator.forSupportFragment(this).setCaptureActivity(CustomScannerActivity.class).initiateScan();
    }



    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
        // temp picker 의 결과값은 setting_value 에 저장됨
        tempValue = 36 + numberPicker.getValue()*0.1;
        Toast.makeText(mContext, Double.toString(tempValue), Toast.LENGTH_SHORT).show();

    }

    public void showTempPicker(View view, String title, String subtitle, int maxvalue, int minvalue, int step, int defvalue){
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
            case (R.id.tempBtn) :
                showTempPicker(view, "체온 측정", "체온을 정확히 입력해주세요", 38,36,1,36);
                break;
            case (R.id.attendanceBtn):
                Toast.makeText(mContext, "출석 체크 완료!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
