package com.android.fpad.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.fpad.R;
import com.android.fpad.retrofit.KategoriList;
import com.android.fpad.ui.stories.KategoriStoryActivity;

import java.util.ArrayList;

public class KategoriViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView textView1;
    private ArrayList<KategoriList> dataSet;
    Context mContext;

    public KategoriViewHolder(View itemView, Context context, ArrayList<KategoriList> data) {
        super(itemView);
        this.dataSet=data;
        this.mContext = context;
        textView1 = (TextView) itemView.findViewById(R.id.namakategori);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //Toast.makeText(view.getContext(), "id kategori = " + dataSet.get(getAdapterPosition()).getId(), Toast.LENGTH_SHORT).show();
        Intent i = new Intent(mContext,KategoriStoryActivity.class);
        i.putExtra("id_kategori", dataSet.get(getAdapterPosition()).getId());
        i.putExtra("nama_kategori", dataSet.get(getAdapterPosition()).getNama_kategori());
        mContext.startActivity(i);

    }

    public void namakategori(String text) {
        textView1.setText(text);
    }

}
