package com.exercise.login_exercise.models;

import java.util.Date;

/**
 * Created by jongwow on 2020-07-17.
 */
public class User {
    private String email;
    private String name;
    private String phone;
    private String temperature;
    private String lastChecked;

    public User(String email, String name, String phone, String temperature, String lastChecked) {
//        this.id = id;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.temperature = temperature;
        this.lastChecked = lastChecked;
//        this.imageUrl = imageUrl;
    }

//    public String getImageUrl() {        return imageUrl;    }

//    public String getId() {
//        return id;
//    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhone() { return phone; }

    public String getTemperature() { return temperature; }

    public String getLastChecked() { return lastChecked;}
}
