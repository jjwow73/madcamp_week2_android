package com.exercise.login_exercise.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.exercise.login_exercise.R;
import com.exercise.login_exercise.fragments.GalleryFragment;
import com.exercise.login_exercise.fragments.UserFragment;
import com.exercise.login_exercise.fragments.SettingFragment;
import com.exercise.login_exercise.storage.SharedPrefManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Created by jongwow on 2020-07-17.
 */

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                fragment = new UserFragment();
                break;
            case R.id.menu_gallery:
                fragment = new GalleryFragment();
                break;
            case R.id.menu_qr:
                fragment = new SettingFragment();
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
