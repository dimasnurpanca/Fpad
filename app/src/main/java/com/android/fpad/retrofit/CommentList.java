package com.android.fpad.retrofit;

/**
 * Created by dimasnurpanca on 10/16/2017.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CommentList {


    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("email_user")
    @Expose
    public String email_user;

    @SerializedName("fullname")
    @Expose
    public String fullname;

    @SerializedName("comment")
    @Expose
    public String comment;

    @SerializedName("date")
    @Expose
    public String date;


    public CommentList(String id, String email_user, String fullname, String comment, String date) {
        this.id = id;
        this.email_user = email_user;
        this.fullname = fullname;
        this.comment = comment;
        this.date = date;
    }

    public String getId() {
        return id;
    }
    public String getEmail_user() {
        return email_user;
    }
    public String getFullname() {
        return fullname;
    }
    public String getComment() {
        return comment;
    }
    public String getDate() {
        return date;
    }

}
