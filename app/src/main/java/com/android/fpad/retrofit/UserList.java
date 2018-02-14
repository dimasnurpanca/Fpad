package com.android.fpad.retrofit;

/**
 * Created by dimasnurpanca on 10/16/2017.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class UserList {


    @SerializedName("username")
    @Expose
    public String username;

    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("role")
    @Expose
    public String role;
    @SerializedName("aktif")
    @Expose
    public String aktif;
    @SerializedName("android_id")
    @Expose
    public String android_id;


    public UserList(String username, String email, String role, String aktif, String android_id) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.aktif = aktif;
        this.android_id = android_id;
    }

    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
    public String getRole() {
        return role;
    }
    public String getAktif() {
        return aktif;
    }
    public String getAndroid_id() {
        return android_id;
    }

}
