package com.android.fpad.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.android.fpad.R;
import com.android.fpad.retrofit.KategoriList;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by anupamchugh on 05/10/16.
 */

public class KategoriAdapter extends RecyclerView.Adapter<KategoriViewHolder>{

    private ArrayList<KategoriList> dataSet;
    Context mContext;

    public KategoriAdapter(ArrayList<KategoriList> data, Context context) {
        this.dataSet=data;
        this.mContext = context;
    }

    @Override
    public KategoriViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kategori_list_item, parent, false);
        return new KategoriViewHolder(view,mContext,dataSet);
    }

    @Override
    public void onBindViewHolder(KategoriViewHolder holder, int position) {
        holder.namakategori(dataSet.get(position).getNama_kategori());

    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
