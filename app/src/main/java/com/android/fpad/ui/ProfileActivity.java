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

public class ProfileActivity extends AppCompatActivity {
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.user_fullname)
    TextView fullname;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.user_email)
    TextView email;
    @BindView(R.id.total_readinglist)
    TextView total_readinglist;
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
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // back button
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.wattpad_logo_thumb);

        setTitle(getResources().getString(R.string.create_title_profile));
        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);

        usrmail = sharedpreferences.getString("email", null);
        usrname = sharedpreferences.getString("username", null);
        nama = sharedpreferences.getString("namalengkap", null);
        hp = sharedpreferences.getString("nomorhp", null);

        fullname.setText(nama);
        username.setText("@"+usrname);
        email.setText(usrmail);

        updatereadinglist();

        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.getMenu().getItem(3).setChecked(true);



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.action_home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finish();
                        break;
                    case R.id.action_library:
                        startActivity(new Intent(getApplicationContext(), LibraryStoryActivity.class));
                        finish();
                        break;
                    case R.id.action_post_story:
                        startActivity(new Intent(getApplicationContext(), StoryActivity.class));
                        finish();
                        break;
                    case R.id.action_profile:
                        break;
                    case R.id.action_setting:
                        startActivity(new Intent(getApplicationContext(), MainSettingsActivity.class));
                        finish();
                        break;
                }
                return true;
            }
        });

    }


    private void updatereadinglist() {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.loadingprogress));
        progressDialog.show();

        RequestBody val_email = RequestBody.create(MediaType.parse("multipart/form-data"), usrmail);
        RequestBody val_type = RequestBody.create(MediaType.parse("multipart/form-data"), "totalreadinglist");



        Call<StoryRespond> resultCall = apiInterface.etc(val_type, val_email);


        resultCall.enqueue(new Callback<StoryRespond>() {
            @Override
            public void onResponse(Call<StoryRespond> call, Response<StoryRespond> response) {

                progressDialog.dismiss();

                // Response Success or Fail

                if (!response.body().getError()) {
                    //Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                   total_readinglist.setText(response.body().getMessage());
                }
                else{
                    //Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }


            }

            @Override
            public void onFailure(Call<StoryRespond> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_profile, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_setting) {
            Intent i = new Intent(ProfileActivity.this, SettingsActivity.class);
            startActivityForResult(i,1);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
    public void logout(View v)
    {
        Toast.makeText(this, "Berhasil Logout!", Toast.LENGTH_LONG).show();
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(LoginActivity.session_status, false);
        editor.clear();
        editor.commit();
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        finish();
        startActivity(intent);
    }
    @Override
    public void onBackPressed()
    {
        // moveTaskToBack(true);
        super.onBackPressed();
          startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
        finish();

    }


}