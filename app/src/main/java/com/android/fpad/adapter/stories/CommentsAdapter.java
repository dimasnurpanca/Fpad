package com.android.fpad.adapter.stories;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.fpad.R;
import com.android.fpad.retrofit.APIInterface;
import com.android.fpad.retrofit.CommentList;

import java.util.ArrayList;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    APIInterface apiInterface;
    ArrayList<CommentList> dataModels;

    public CommentsAdapter(ArrayList<CommentList> data) {
        this.dataModels = data;
    }

    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentsAdapter.ViewHolder viewHolder, int i) {

        viewHolder.var_name.setText(dataModels.get(i).getFullname());
        viewHolder.var_message.setText(dataModels.get(i).getComment());
        viewHolder.var_date.setText(dataModels.get(i).getDate());
    }

    @Override
    public int getItemCount() {
        return dataModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView var_name,var_message,var_date;
        public ViewHolder(View view) {
            super(view);

            var_name = (TextView)view.findViewById(R.id.var_name);
            var_message = (TextView)view.findViewById(R.id.var_message);
            var_date = (TextView)view.findViewById(R.id.var_date);

        }
    }

}