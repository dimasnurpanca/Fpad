package com.android.fpad.ui.stories;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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
import com.android.fpad.adapter.stories.LibraryPagerAdapter;
import com.android.fpad.helper.BottomNavigationViewHelper;
import com.android.fpad.ui.HomeActivity;
import com.android.fpad.ui.LoginActivity;
import com.android.fpad.ui.MainSettingsActivity;
import com.android.fpad.ui.ProfileActivity;
import com.android.fpad.ui.SettingsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LibraryStoryActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;


    LibraryPagerAdapter libraryPagerAdapter;
    String usrmail;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // back button
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.wattpad_logo_thumb);
        setTitle(getResources().getString(R.string.library_title_activity));

        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomNavigationView.setSelectedItemId(R.id.action_library);

        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
        usrmail = sharedpreferences.getString("email", null);

        libraryPagerAdapter = new LibraryPagerAdapter(getSupportFragmentManager(), LibraryStoryActivity.this);
        viewPager.setAdapter(libraryPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    public String getEmail() {
        return usrmail;
    }




    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(LibraryStoryActivity.this, HomeActivity.class));
        finish();

    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        libraryPagerAdapter = new LibraryPagerAdapter(getSupportFragmentManager(), LibraryStoryActivity.this);
        viewPager.setAdapter(libraryPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        //Refresh your stuff here
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.action_home:
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                    return true;
                case R.id.action_library:
                    return true;
                case R.id.action_post_story:
                    startActivity(new Intent(getApplicationContext(), StoryActivity.class));
                    finish();
                    return true;
                case R.id.action_profile:
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    finish();
                    return true;
                case R.id.action_setting:
                    startActivity(new Intent(getApplicationContext(), MainSettingsActivity.class));
                    finish();
                    return true;
            }
            return false;
        }
    };

}
