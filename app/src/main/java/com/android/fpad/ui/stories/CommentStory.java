package com.android.fpad.ui.stories;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.fpad.R;
import com.android.fpad.adapter.stories.CommentsAdapter;
import com.android.fpad.retrofit.APIClient;
import com.android.fpad.retrofit.APIInterface;
import com.android.fpad.retrofit.CommentList;
import com.android.fpad.retrofit.KategoriList;
import com.android.fpad.retrofit.StoryRespond;
import com.android.fpad.ui.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CommentStory extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_views)
    RecyclerView recyclerView;
    @BindView(R.id.var_comment)
    EditText var_comment;

    String usrmail,story_id;
    SharedPreferences sharedpreferences;
    APIInterface apiInterface;
    ArrayList<CommentList> dataModels;

    private CommentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // back button


        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
        usrmail = sharedpreferences.getString("email", null);
        apiInterface = APIClient.getClient().create(APIInterface.class); //retrofit
        Intent intent = getIntent();
        story_id = intent.getExtras().getString("story_id");


        initViews();
    }

    private void initViews(){
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        loadJSON();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void post(View v) {
        if(var_comment.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_post_comment), Toast.LENGTH_LONG).show();
        }else{
            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(CommentStory.this);
            progressDialog.setMessage(getResources().getString(R.string.loading));
            progressDialog.show();

            RequestBody val_id = RequestBody.create(MediaType.parse("multipart/form-data"), story_id);
            RequestBody val_email = RequestBody.create(MediaType.parse("multipart/form-data"), usrmail);
            RequestBody val_comment = RequestBody.create(MediaType.parse("multipart/form-data"), var_comment.getText().toString());
            RequestBody val_type = RequestBody.create(MediaType.parse("multipart/form-data"), "postcomment");


            Call<StoryRespond> resultCall = apiInterface.comment(val_type,val_email,val_comment,val_id);
            resultCall.enqueue(new Callback<StoryRespond>() {
                @Override
                public void onResponse(Call<StoryRespond> call, Response<StoryRespond> response) {

                    progressDialog.dismiss();

                    // Response Success or Fail




                }

                @Override
                public void onFailure(Call<StoryRespond> call, Throwable t) {
                    progressDialog.dismiss();
                }
            });
            Intent i = new Intent(this,CommentStory.class);
            i.putExtra("story_id", story_id);
            this.startActivity(i);
            finish();


        }
    }

    private void loadJSON(){
        dataModels= new ArrayList<>();
        Call<List<CommentList>> call3 = apiInterface.doGetCommentList(story_id);
        call3.enqueue(new Callback<List<CommentList>>() {
            @Override
            public void onResponse(Call<List<CommentList>> call, Response<List<CommentList>> response) {

                List<CommentList> jsonResponse = response.body();
                for (int i = 0; i < jsonResponse.size(); i++) {
                   dataModels.add(new CommentList(jsonResponse.get(i).getId(),jsonResponse.get(i).getEmail_user(),jsonResponse.get(i).getFullname(),jsonResponse.get(i).getComment(),jsonResponse.get(i).getDate()));
                }
                setTitle(jsonResponse.size()+" Comments");
                adapter = new CommentsAdapter(dataModels);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<CommentList>> call, Throwable t) {
                call.cancel();
            }
        });
    }
    
    
    
    
    public String getEmail() {
        return usrmail;
    }





    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();

    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        //Refresh your stuff here
    }

}
