package com.android.fpad.ui;

/**
 * Created by dimasnurpanca on 10/2/2017.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.android.fpad.R;
import com.android.fpad.helper.BottomNavigationViewHelper;
import com.android.fpad.retrofit.APIClient;
import com.android.fpad.retrofit.APIInterface;
import com.android.fpad.retrofit.StoryRespond;
import com.android.fpad.ui.stories.LibraryStoryActivity;
import com.android.fpad.ui.stories.StoryActivity;
import com.google.firebase.auth.FirebaseAuth;

import static com.android.fpad.ui.LoginActivity.TAG_USERNAME;

public class AboutActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;


    APIInterface apiInterface;
    Activity context = this;
    String usrmail,usrname,nama,hp;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // back button
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.wattpad_logo_thumb);

        setTitle(getResources().getString(R.string.about));
        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);

        usrmail = sharedpreferences.getString("email", null);
        usrname = sharedpreferences.getString("username", null);
        nama = sharedpreferences.getString("namalengkap", null);
        hp = sharedpreferences.getString("nomorhp", null);









    }




    @Override
    public void onBackPressed()
    {
        // moveTaskToBack(true);
        super.onBackPressed();
        startActivity(new Intent(AboutActivity.this, MainSettingsActivity.class));
        finish();

    }


}