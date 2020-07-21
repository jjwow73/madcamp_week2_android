package com.exercise.login_exercise.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.exercise.login_exercise.models.User;

/**
 * Created by jongwow on 2020-07-17.
 */
public class SharedPrefManager {
    private static String SHARED_PREF_NAME = "my_shared_pref";

    private static SharedPrefManager mInstance;
    private Context mCtx;

    private SharedPrefManager(Context mCtx) {
        this.mCtx = mCtx;
    }

    public static synchronized SharedPrefManager getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(mCtx);
        }
        return mInstance;
    }

    public void saveUser(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("id", "1");
        editor.putString("email", user.getEmail());
        editor.putString("name", user.getName());
        editor.putString("phone", user.getPhone());
        editor.putString("temperature", user.getTemperature());
        editor.putString("lastChecked", user.getLastChecked());
//        editor.putString("imageUrl", user.getImageUrl());

        editor.apply();
    }

    public void saveId(String id){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("id", id);

        editor.apply();
    }

    public void saveTemperature(String temp){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("temperature", temp);

        editor.apply();

    }

    public void saveLastChecked(String lastChecked){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("lastChecked", lastChecked);

        editor.apply();
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return !sharedPreferences.getString("id", "-1").equals("-1");
    }

    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getString("email", null),
                sharedPreferences.getString("name", null),
                sharedPreferences.getString("phone", null),
                sharedPreferences.getString("temperature", null),
                sharedPreferences.getString("lastChecked", null)
        );
//                sharedPreferences.getString("id", "-1"),
//                sharedPreferences.getString("imageUrl", null)
    }

    public void clear() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public String getId() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("id", "-1");
    }
}
