package com.android.fpad.adapter.stories;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.fpad.R;
import com.android.fpad.retrofit.StoryList;

import java.util.ArrayList;

/**
 * Created by anupamchugh on 05/10/16.
 */

public class DraftsAdapter extends RecyclerView.Adapter<DraftsViewHolder> {

    private ArrayList<StoryList> dataSet;
    Context mContext;

    public DraftsAdapter(ArrayList<StoryList> data, Context context) {
        this.dataSet=data;
        this.mContext = context;
    }

    @Override
    public DraftsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drafts_list_item, parent, false);
        return new DraftsViewHolder(view, mContext, dataSet);
    }

    @Override
    public void onBindViewHolder(DraftsViewHolder holder, int position) {
        //holder.id(dataSet.get(position).getId());
        holder.title(dataSet.get(position).getTitle());
        holder.lastupdate(dataSet.get(position).getLast_update());
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
