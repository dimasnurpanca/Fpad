package com.android.fpad.adapter.stories;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.fpad.R;
import com.android.fpad.retrofit.StoryList;

import java.util.ArrayList;

/**
 * Created by anupamchugh on 05/10/16.
 */

public class PublishedAdapter extends RecyclerView.Adapter<PublishedViewHolder> {

    private ArrayList<StoryList> dataSet;
    Context mContext;

    public PublishedAdapter(ArrayList<StoryList> data, Context context) {
        this.dataSet=data;
        this.mContext = context;
    }

    @Override
    public PublishedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.published_list_item, parent, false);
        return new PublishedViewHolder(view, mContext, dataSet);
    }

    @Override
    public void onBindViewHolder(PublishedViewHolder holder, int position) {
        //holder.id(dataSet.get(position).getId());
        holder.title(dataSet.get(position).getTitle());
        holder.lastupdate(dataSet.get(position).getLast_update());
        holder.read(dataSet.get(position).getRead());
        holder.like(dataSet.get(position).getLike());
        holder.comment(dataSet.get(position).getComment());
        holder.status(dataSet.get(position).getStatus());
        //holder.description(dataSet.get(position).getDescription());
        //holder.image(dataSet.get(position).getImage());
        //holder.content(dataSet.get(position).getContent());
       // holder.date(dataSet.get(position).getDate());
        //holder.status(dataSet.get(position).getStatus());

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
