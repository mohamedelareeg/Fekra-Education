package com.rovaind.academy.model;

import java.io.Serializable;

public class User  implements Serializable {


    private int id;
    private String name;
    private String email;
    private String password;
    private String thumb_image;
    private String gender;
    private String gcmtoken;

    public User()
    {

    }
    public User(int id, String gcmtoken) {
        this.id = id;
        this.gcmtoken = gcmtoken;
    }

    public User(String name, String email, String password,String thumb_image , String gender , String gcmtoken) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.thumb_image = thumb_image;
        this.gender = gender;
        this.gcmtoken = gcmtoken;
    }

    public User(int id, String name, String email,String thumb_image , String gender , String gcmtoken){
        this.id = id;
        this.name = name;
        this.email = email;
        this.thumb_image = thumb_image;
        this.gender = gender;
        this.gcmtoken = gcmtoken;
    }

    public User(int id, String name, String email, String password ,String thumb_image, String gender , String gcmtoken) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.thumb_image = thumb_image;
        this.gender = gender;
        this.gcmtoken = gcmtoken;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGcmtoken() {
        return gcmtoken;
    }

    public void setGcmtoken(String gcmtoken) {
        this.gcmtoken = gcmtoken;
    }
}