package com.android.fpad.retrofit;

/**
 * Created by dimasnurpanca on 10/1/2017.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginList {

    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("username")
    @Expose
    public String username;

    @SerializedName("email")
    @Expose
    public String email;

    @SerializedName("namalengkap")
    @Expose
    public String namalengkap;


    @SerializedName("role")
    @Expose
    public String role;

    @SerializedName("aktif")
    @Expose
    public String aktif;


    @SerializedName("android_id")
    @Expose
    public String android_id;

    @SerializedName("gender")
    @Expose
    public String gender;

    @SerializedName("birthday")
    @Expose
    public String birthday;
}
