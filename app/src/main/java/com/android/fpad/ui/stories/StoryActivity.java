package com.android.fpad.ui.stories;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.fpad.R;
import com.android.fpad.adapter.ViewPagerAdapter;
import com.android.fpad.adapter.stories.StoryPagerAdapter;
import com.android.fpad.ui.HomeActivity;
import com.android.fpad.ui.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StoryActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;


    StoryPagerAdapter storyPagerAdapter;
    String usrmail;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // back button
        setTitle(getResources().getString(R.string.create_title_activity));

        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
        usrmail = sharedpreferences.getString("email", null);

        storyPagerAdapter = new StoryPagerAdapter(getSupportFragmentManager(), StoryActivity.this);
        viewPager.setAdapter(storyPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    public String getEmail() {
        return usrmail;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main_story, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.newstory) {
            Intent i = new Intent(StoryActivity.this, AddStoryActivity.class);
            startActivityForResult(i,1);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(StoryActivity.this, HomeActivity.class));
        finish();

    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        storyPagerAdapter = new StoryPagerAdapter(getSupportFragmentManager(), StoryActivity.this);
        viewPager.setAdapter(storyPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        //Refresh your stuff here
    }

}
