package com.exercise.login_exercise.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jongwow on 2020-07-17.
 */
public class User {
    private String email;
    private String name;
    private String phone;
    private String temperature;
    private String lastChecked;

    @SerializedName("profileImage")
    private String imageUrl;

    public User(String email, String name, String phone, String temperature, String lastChecked, String imageUrl) {
//        this.id = id;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.temperature = temperature;
        this.lastChecked = lastChecked;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

//    public String getId() {
//        return id;
//    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getLastChecked() {
        return lastChecked;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public void setLastChecked(String lastChecked) {
        this.lastChecked = lastChecked;
    }
}
