package com.exercise.login_exercise.models;

/**
 * Created by jongwow on 2020-07-17.
 */
public class User {
    private String email, name, phone;

    public User(String email, String name, String phone) {
//        this.id = id;
        this.email = email;
        this.name = name;
        this.phone = phone;
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

    public String getPhone() {
        return phone;
    }
}
