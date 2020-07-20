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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.exercise.login_exercise.R;
import com.exercise.login_exercise.activities.CustomScannerActivity;
import com.exercise.login_exercise.activities.TempDialogActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import org.w3c.dom.Text;

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
        userName = getActivity().findViewById(R.id.userName);
        userLevel = getActivity().findViewById(R.id.userLevel);
        userPhone = getActivity().findViewById(R.id.userPhoneNumber);
        userEmail = getActivity().findViewById(R.id.userEmail);
        tempBtn = getActivity().findViewById(R.id.tempBtn);
        attendanceBtn = getActivity().findViewById(R.id.attendanceBtn);

        // scanner 이후 view 가 열리면 listener 설정
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tempBtn:
                // 체온 기록 alert dialog
                showTempPicker(view, "온도 측정", "온도를 기록하세요", 37,36,1,36);
                break;
            case R.id.attendanceBtn:
                // 출석체크 기록으로 연결
                break;
        }
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
        // temp picker 의 결과값은 setting_value 에 저장됨
        double setting_value = 36 + numberPicker.getValue()*0.1;
        Toast.makeText(mContext, Double.toString(setting_value), Toast.LENGTH_SHORT).show();

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
}
