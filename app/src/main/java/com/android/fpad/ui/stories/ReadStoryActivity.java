package com.android.fpad.ui.stories;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.fpad.R;
import com.android.fpad.retrofit.APIClient;
import com.android.fpad.retrofit.APIInterface;
import com.android.fpad.retrofit.StoryRespond;
import com.android.fpad.ui.HomeActivity;
import com.android.fpad.ui.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReadStoryActivity extends AppCompatActivity {
    @BindView(R.id.coordinatorlayout)
    CoordinatorLayout coordinatorlayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.total_read)
    TextView total_read;
    @BindView(R.id.total_like)
    TextView total_like;
    @BindView(R.id.total_comment)
    TextView total_comment;
    @BindView(R.id.vote)
    ImageButton var_vote;

    APIInterface apiInterface;
    Activity context = this;
    Boolean vote = false;
    String usrmail;
    String story_id,story_email,story_title,story_description,story_kategori,story_content,story_status,story_read,story_like,story_comment;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readstory);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // back button
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int UI_OPTIONS = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            getWindow().getDecorView().setSystemUiVisibility(UI_OPTIONS);
        }

        Intent intent = getIntent();
        story_id = intent.getExtras().getString("story_id");
        story_email = intent.getExtras().getString("story_email");
        story_title = intent.getExtras().getString("story_title");
        story_description = intent.getExtras().getString("story_description");
        story_kategori = intent.getExtras().getString("story_kategori");
        story_content = intent.getExtras().getString("story_content");
        story_status = intent.getExtras().getString("story_status");
        story_read = intent.getExtras().getString("story_read");
        story_like = intent.getExtras().getString("story_like");
        story_comment = intent.getExtras().getString("story_comment");


        setTitle(story_title);

        title.setText(story_title);
        total_read.setText(story_read);
        total_like.setText(story_like);
        total_comment.setText(story_comment);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            content.setText(Html.fromHtml(story_content, Html.FROM_HTML_MODE_LEGACY));
        } else{
            content.setText(Html.fromHtml(story_content));
        }

        apiInterface = APIClient.getClient().create(APIInterface.class); //retrofit
        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
        usrmail = sharedpreferences.getString("email", null);

        postvote("checkvote");


    }



    public String getEmail() {
        return usrmail;
    }

    public void vote(View v) {
        if(story_email.equals(usrmail)){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.notif_vote_owner), Toast.LENGTH_LONG).show();
        }else{
            if(vote){
               postvote("deletevote");
            }else{
                postvote("addvote");
            }

            reload();
        }
    }
    public void comment(View v) {
        if(story_status.equals("Drafts")){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.notif_draft_comment), Toast.LENGTH_LONG).show();
        }else {
            Intent i = new Intent(this, CommentStory.class);
            i.putExtra("story_id", story_id);
            this.startActivity(i);
        }
    }
    public void share(View v) {
        int applicationNameId = context.getApplicationInfo().labelRes;
        final String appPackageName = context.getPackageName();
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, context.getString(applicationNameId));
        String text = "Install this cool application: ";
        String link = "https://play.google.com/store/apps/details?id=" + appPackageName;
        i.putExtra(Intent.EXTRA_TEXT, text + " " + link);
        startActivity(Intent.createChooser(i, "Share link:"));
    }


    public void postvote(String type) {
        if(story_status.equals("Drafts")){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.notif_draft_comment), Toast.LENGTH_LONG).show();
        }else {
            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(ReadStoryActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.loading));
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
            RequestBody val_type = RequestBody.create(MediaType.parse("multipart/form-data"), type);
            RequestBody val_email = RequestBody.create(MediaType.parse("multipart/form-data"), usrmail);
            RequestBody val_id = RequestBody.create(MediaType.parse("multipart/form-data"), story_id);
            Call<StoryRespond> resultCall = apiInterface.vote(val_type,val_email,val_id);
            resultCall.enqueue(new Callback<StoryRespond>() {
                @Override
                public void onResponse(Call<StoryRespond> call, Response<StoryRespond> response) {



                    // Response Success or Fail


                    if(response.body().getMessage().equals("false")){
                        vote = false;
                        var_vote.setImageResource(R.drawable.ic_format_vote);
                    }else if(response.body().getMessage().equals("true")){
                        vote = true;
                        var_vote.setImageResource(R.drawable.ic_format_vote_true);
                    }



                }

                @Override
                public void onFailure(Call<StoryRespond> call, Throwable t) {
                    //progressDialog.dismiss();
                }
            });
            progressDialog.dismiss();

        }
    }



    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        //startActivity(new Intent(ReadStoryActivity.this, HomeActivity.class));
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void reload(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.recreate();
        } else {
            final Intent intent = this.getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            this.finish();
            this.overridePendingTransition(0, 0);
            this.startActivity(intent);
            this.overridePendingTransition(0, 0);
        }
    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int UI_OPTIONS = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            getWindow().getDecorView().setSystemUiVisibility(UI_OPTIONS);
        }
        //Refresh your stuff here
    }





}
