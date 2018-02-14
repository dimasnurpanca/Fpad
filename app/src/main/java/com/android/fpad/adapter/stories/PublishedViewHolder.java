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

public class PublishedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    private TextView textView6;
    private TextView textViewOptions;
    APIInterface apiInterface;
    Context mContext;
    private ArrayList<StoryList> dataSet;

    public PublishedViewHolder(View itemView, Context context, ArrayList<StoryList> data) {
        super(itemView);
        textView1 = (TextView) itemView.findViewById(R.id.title);
        textView2 = (TextView) itemView.findViewById(R.id.total_read);
        textView3 = (TextView) itemView.findViewById(R.id.total_like);
        textView4 = (TextView) itemView.findViewById(R.id.total_comment);
        textView5 = (TextView) itemView.findViewById(R.id.lastupdate);
        textView6 = (TextView) itemView.findViewById(R.id.status);
        textViewOptions = (TextView) itemView.findViewById(R.id.textViewOptions);
        this.mContext = context;
        this.dataSet=data;
        apiInterface = APIClient.getClient().create(APIInterface.class); //retrofit

        itemView.setOnClickListener(this);



        textViewOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(mContext, textViewOptions);
                //inflating menu from xml resource
                popup.inflate(R.menu.menu_option_list_story);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.view:
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
                                break;
                            case R.id.edit:
                                Intent i = new Intent(mContext,EditStoryActivity.class);
                                i.putExtra("story_id", dataSet.get(getAdapterPosition()).getId());
                                i.putExtra("story_title", dataSet.get(getAdapterPosition()).getTitle());
                                i.putExtra("story_description", dataSet.get(getAdapterPosition()).getDescription());
                                i.putExtra("story_kategori", dataSet.get(getAdapterPosition()).getKategori_id());
                                i.putExtra("story_content", dataSet.get(getAdapterPosition()).getContent());
                                i.putExtra("story_status", dataSet.get(getAdapterPosition()).getStatus());
                                mContext.startActivity(i);
                                break;
                            case R.id.delete:
                                delete(dataSet.get(getAdapterPosition()).getId(),dataSet.get(getAdapterPosition()).getEmail());
                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();

            }
        });
    }

    @Override
    public void onClick(View view) {
        //Toast.makeText(view.getContext(), "id-draft-story = " + dataSet.get(getAdapterPosition()).getId(), Toast.LENGTH_SHORT).show();
        Intent i = new Intent(mContext,EditStoryActivity.class);
        i.putExtra("story_id", dataSet.get(getAdapterPosition()).getId());
        i.putExtra("story_title", dataSet.get(getAdapterPosition()).getTitle());
        i.putExtra("story_description", dataSet.get(getAdapterPosition()).getDescription());
        i.putExtra("story_kategori", dataSet.get(getAdapterPosition()).getKategori_id());
        i.putExtra("story_content", dataSet.get(getAdapterPosition()).getContent());
        i.putExtra("story_status", dataSet.get(getAdapterPosition()).getStatus());
        mContext.startActivity(i);
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
    public void status(String text) {

        if(text.equals("Published")){
            textView6.setText(mContext.getResources().getString(R.string.waiting_approval));
        }else if(text.equals("Approved")) {
            textView6.setText(mContext.getResources().getString(R.string.approved));
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


    private void delete(final String id, final String email) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getResources().getString(R.string.delete_header_builder));
        builder.setMessage(mContext.getResources().getString(R.string.delete_body_builder));

        //Yes Button
        builder.setPositiveButton(mContext.getResources().getString(R.string.yes_builder), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final ProgressDialog progressDialog;
                progressDialog = new ProgressDialog(mContext);
                progressDialog.setMessage(mContext.getResources().getString(R.string.deleteprogress));
                progressDialog.show();
                String android_id = Settings.Secure.getString(mContext.getContentResolver(),
                        Settings.Secure.ANDROID_ID);

                RequestBody val_id = RequestBody.create(MediaType.parse("multipart/form-data"), id);
                RequestBody val_uid = RequestBody.create(MediaType.parse("multipart/form-data"), android_id);
                RequestBody val_email = RequestBody.create(MediaType.parse("multipart/form-data"), email);
                Call<StoryRespond> resultCall = apiInterface.delete(val_id,val_uid,val_email);

                resultCall.enqueue(new Callback<StoryRespond>() {
                    @Override
                    public void onResponse(Call<StoryRespond> call, Response<StoryRespond> response) {

                        progressDialog.dismiss();

                        // Response Success or Fail

                        if (!response.body().getError()) {
                            Toast.makeText(mContext.getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                            ((Activity) mContext).finish();
                            mContext.startActivity(((Activity) mContext).getIntent());
                        }
                        else{
                            Toast.makeText(mContext.getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();}
                        progressDialog.dismiss();

                    }

                    @Override
                    public void onFailure(Call<StoryRespond> call, Throwable t) {
                        progressDialog.dismiss();
                    }
                });



            }
        });

        //No Button
        builder.setNegativeButton(mContext.getResources().getString(R.string.no_builder), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
