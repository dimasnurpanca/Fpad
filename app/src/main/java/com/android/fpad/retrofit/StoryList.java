package com.android.fpad.retrofit;

/**
 * Created by dimasnurpanca on 10/16/2017.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class StoryList {


    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("kategori_id")
    @Expose
    public String kategori_id;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("content")
    @Expose
    public String content;
    @SerializedName("date")
    @Expose
    public String date;
    @SerializedName("last_update")
    @Expose
    public String last_update;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("read")
    @Expose
    public String read;
    @SerializedName("like")
    @Expose
    public String like;
    @SerializedName("comment")
    @Expose
    public String comment;


    public StoryList(String id, String email, String kategori_id, String title, String description, String image, String content, String date, String last_update, String status, String read, String like, String comment) {
        this.id = id;
        this.email = email;
        this.kategori_id = kategori_id;
        this.title = title;
        this.description = description;
        this.image = image;
        this.content = content;
        this.date = date;
        this.last_update = last_update;
        this.status = status;
        this.read = read;
        this.like = like;
        this.comment = comment;
    }

    public String getId() {
        return id;
    }
    public String getEmail() {
        return email;
    }
    public String getKategori_id() {
        return kategori_id;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public String getImage() {
        return image;
    }
    public String getContent() {
        return content;
    }
    public String getDate() {
        return date;
    }
    public String getLast_update() {
        return last_update;
    }
    public String getStatus() {
        return status;
    }
    public String getRead() {
        return read;
    }
    public String getLike() {
        return like;
    }
    public String getComment() {
        return comment;
    }
}
