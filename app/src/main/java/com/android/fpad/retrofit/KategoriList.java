package com.android.fpad.retrofit;

/**
 * Created by dimasnurpanca on 10/16/2017.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class KategoriList {


    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("nama_kategori")
    @Expose
    public String nama_kategori;


    public KategoriList(String id, String nama_kategori) {
        this.id = id;
        this.nama_kategori = nama_kategori;
    }

    public String getId() {
        return id;
    }
    public String getNama_kategori() {
        return nama_kategori;
    }

}
