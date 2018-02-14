package com.android.fpad.adapter.stories;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.fpad.R;
import com.android.fpad.retrofit.APIClient;
import com.android.fpad.retrofit.APIInterface;
import com.android.fpad.retrofit.StoryList;
import com.android.fpad.retrofit.StoryRespond;
import com.android.fpad.ui.stories.EditStoryActivity;
import com.android.fpad.ui.stories.ReadStoryActivity;
import com.android.fpad.ui.stories.StoryDetailActivity;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    APIInterface apiInterface;
    Context mContext;
    private ArrayList<StoryList> dataSet;

    public SearchViewHolder(View itemView, Context context, ArrayList<StoryList> data) {
        super(itemView);
        textView1 = (TextView) itemView.findViewById(R.id.title);
        textView2 = (TextView) itemView.findViewById(R.id.total_read);
        textView3 = (TextView) itemView.findViewById(R.id.total_like);
        textView4 = (TextView) itemView.findViewById(R.id.total_comment);
        textView5 = (TextView) itemView.findViewById(R.id.lastupdate);
        this.mContext = context;
        this.dataSet=data;
        apiInterface = APIClient.getClient().create(APIInterface.class); //retrofit

        itemView.setOnClickListener(this);




    }

    @Override
    public void onClick(View view) {
        //Toast.makeText(view.getContext(), "id-draft-story = " + dataSet.get(getAdapterPosition()).getId(), Toast.LENGTH_SHORT).show();
        Intent i1 = new Intent(mContext,StoryDetailActivity.class);
        i1.putExtra("story_id", dataSet.get(getAdapterPosition()).getId());
        i1.putExtra("story_email", dataSet.get(getAdapterPosition()).getEmail());
        i1.putExtra("story_title", dataSet.get(getAdapterPosition()).getTitle());
        i1.putExtra("story_description", dataSet.get(getAdapterPosition()).getDescription());
        i1.putExtra("story_kategori", dataSet.get(getAdapterPosition()).getKategori_id());
        i1.putExtra("story_content", dataSet.get(getAdapterPosition()).getContent());
        i1.putExtra("story_status", dataSet.get(getAdapterPosition()).getStatus());
        i1.putExtra("story_read", dataSet.get(getAdapterPosition()).getRead());
        i1.putExtra("story_like", dataSet.get(getAdapterPosition()).getLike());
        i1.putExtra("story_comment", dataSet.get(getAdapterPosition()).getComment());
        mContext.startActivity(i1);
    }

    public void title(String text) {
        textView1.setText(text);
    }
    public void lastupdate(String text) {
        if(text.equals("")){
            textView5.setText(mContext.getResources().getString(R.string.default_last_update)+"-");
        }else {
            textView5.setText(mContext.getResources().getString(R.string.default_last_update)+text);
        }
    }

    public void read(String text) {
        textView2.setText(text);
    }
    public void like(String text) {
        textView3.setText(text);
    }
    public void comment(String text) {
        textView4.setText(text);
    }

}
