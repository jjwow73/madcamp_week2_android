package com.exercise.login_exercise.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.exercise.login_exercise.R;
import com.exercise.login_exercise.activities.CustomScannerActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import org.w3c.dom.Text;

/**
 * Created by jongwow on 2020-07-19.
 */
public class ScannerFragment extends Fragment {
    private static final String TAG = "ScannerFragment";
    private Context mContext;
    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;

    private TextView userName;
    private TextView userLevel;
    private TextView userPhone;
    private TextView userEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();

        userName = getActivity().findViewById(R.id.userName);
        userLevel = getActivity().findViewById(R.id.userLevel);
        userPhone = getActivity().findViewById(R.id.userPhoneNumber);
        userEmail = getActivity().findViewById(R.id.userEmail);

        startScanner();
        return inflater.inflate(R.layout.fragment_scanner, container, false);
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
}
