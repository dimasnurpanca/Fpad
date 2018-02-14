package com.android.fpad.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.fpad.R;
import com.android.fpad.adapter.ViewPagerAdapter;
import com.android.fpad.helper.BottomNavigationViewHelper;
import com.android.fpad.ui.stories.LibraryStoryActivity;
import com.android.fpad.ui.stories.SearchStoryActivity;
import com.android.fpad.ui.stories.StoryActivity;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    Activity context = this;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    ViewPagerAdapter viewPagerAdapter;
    private boolean canExit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // back button
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.wattpad_logo_thumb);

        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        Configuration config = getBaseContext().getResources().getConfiguration();

        String lang = settings.getString("LANG", "");
        if (!"".equals(lang) && !config.locale.getLanguage().equals(lang)) {
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
        FirebaseMessaging.getInstance().subscribeToTopic(getResources().getString(R.string.topics));

    }

    @Override
    public void onBackPressed() {
        if(canExit){
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        }
        else{
            canExit = true;
            Toast.makeText(getApplicationContext(), "Press again to exit!", Toast.LENGTH_SHORT).show();
        }
        mHandler.sendEmptyMessageDelayed(1, 2000/*time interval to next press in milli second*/);// if not pressed within 2seconds then will be setted(canExit) as false

    }

   public Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    canExit = false;
                    break;
                default:
                    break;
            }
            return canExit;
        }
    });

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.trim().length() >= 3) {
                    Intent i1 = new Intent(context, SearchStoryActivity.class);
                    i1.putExtra("text", query);
                    context.startActivity(i1);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 3) {
                    Intent i1 = new Intent(context, SearchStoryActivity.class);
                    i1.putExtra("text", newText);
                    context.startActivity(i1);
                }
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    public void setLangRecreate(String langval) {
        Configuration config = getBaseContext().getResources().getConfiguration();
        Locale locale = new Locale(langval);
        Locale.setDefault(locale);
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        recreate();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.action_home:
                    return true;
                case R.id.action_library:
                    startActivity(new Intent(getApplicationContext(), LibraryStoryActivity.class));
                    return true;
                case R.id.action_post_story:
                    startActivity(new Intent(getApplicationContext(), StoryActivity.class));
                    return true;
                case R.id.action_profile:
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    return true;
                case R.id.action_setting:
                    startActivity(new Intent(getApplicationContext(), MainSettingsActivity.class));
                    return true;
            }
            return false;
        }
    };


}
