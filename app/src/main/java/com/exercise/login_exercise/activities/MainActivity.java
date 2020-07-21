package com.exercise.login_exercise.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.exercise.login_exercise.R;
import com.exercise.login_exercise.fragments.GalleryFragment2;
import com.exercise.login_exercise.fragments.ScannerFragment;
import com.exercise.login_exercise.fragments.SettingFragment;
import com.exercise.login_exercise.fragments.UserFragment;
import com.exercise.login_exercise.storage.SharedPrefManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;

/**
 * Created by jongwow on 2020-07-17.
 */

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private TextView savedUserName, savedUserTemp, savedUserEmail, savedUserPhone;
    private ImageView savedUserImage;
    private Button logoutButton;
    private LinearLayout topTab;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ////////// Top tab ///////////
        savedUserName = findViewById(R.id.savedUserName);
        savedUserTemp = findViewById(R.id.savedUserTemperature);
        savedUserEmail = findViewById(R.id.savedUserEmail);
        savedUserPhone = findViewById(R.id.savedUserPhone);

        savedUserImage = findViewById(R.id.savedUserImage);
        logoutButton = findViewById(R.id.buttonLogout);

        topTab = findViewById(R.id.topTab);
        relativeLayout = findViewById(R.id.relativeLayout);

        // get info from login
        savedUserName.setText(SharedPrefManager.getInstance(this).getUser().getName());
        savedUserTemp.setText(SharedPrefManager.getInstance(this).getUser().getTemperature() + "℃");
        savedUserEmail.setText(SharedPrefManager.getInstance(this).getUser().getEmail());
        savedUserPhone.setText(SharedPrefManager.getInstance(this).getUser().getPhone());

        savedUserImage.setImageResource(R.drawable.studnet_image);

        BottomNavigationView navigationView = findViewById(R.id.bottom_nav);
        navigationView.setOnNavigationItemSelectedListener(this);

        displayFragment(new UserFragment());
    }
    private void displayFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.relativeLayout, fragment)
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            finish();
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;

        switch(item.getItemId()){
            case R.id.menu_people:
                topTab.setVisibility(View.VISIBLE);
                fragment = new UserFragment();
                break;
            case R.id.menu_gallery:
                topTab.setVisibility(View.VISIBLE);
                fragment = new GalleryFragment2();
                break;
            case R.id.menu_qr:
                topTab.setVisibility(View.GONE);
                fragment = new ScannerFragment();
                break;
            case R.id.menu_setting:
                fragment = new SettingFragment();
                break;
        }

        if(fragment != null){
            displayFragment(fragment);
        }

        return false;
    }

}
