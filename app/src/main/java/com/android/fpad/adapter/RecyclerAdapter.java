package com.android.fpad.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.fpad.R;
import com.android.fpad.retrofit.StoryList;
import com.android.fpad.retrofit.UserList;

import java.util.ArrayList;

/**
 * Created by anupamchugh on 05/10/16.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<TextItemViewHolder> {

    private ArrayList<StoryList> dataSet;
    Context mContext;

    public RecyclerAdapter(ArrayList<StoryList> data, Context context) {
        this.dataSet=data;
        this.mContext = context;
    }

    @Override
    public TextItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_list_item, parent, false);
        return new TextItemViewHolder(view, mContext, dataSet);
    }

    @Override
    public void onBindViewHolder(TextItemViewHolder holder, int position) {
        holder.title(dataSet.get(position).getTitle());
        holder.lastupdate(dataSet.get(position).getLast_update());
        holder.read(dataSet.get(position).getRead());
        holder.like(dataSet.get(position).getLike());
        holder.comment(dataSet.get(position).getComment());

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
