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
import com.android.fpad.adapter.stories.KategoriPagerAdapter;
import com.android.fpad.ui.HomeActivity;
import com.android.fpad.ui.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


public class KategoriStoryActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;


    KategoriPagerAdapter kategoriPagerAdapter;
    String usrmail,id_kategori,nama_kategori;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategoristory);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // back button
        Intent intent = getIntent();
        id_kategori = intent.getExtras().getString("id_kategori");
        nama_kategori = intent.getExtras().getString("nama_kategori");
        
        setTitle(nama_kategori);

        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
        usrmail = sharedpreferences.getString("email", null);

        kategoriPagerAdapter = new KategoriPagerAdapter(getSupportFragmentManager(), KategoriStoryActivity.this);
        viewPager.setAdapter(kategoriPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    public String getEmail() {
        return usrmail;
    }

    public String getKategoriId() {
        return id_kategori;
    }

    

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(KategoriStoryActivity.this, HomeActivity.class));
        finish();

    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        kategoriPagerAdapter = new KategoriPagerAdapter(getSupportFragmentManager(), KategoriStoryActivity.this);
        viewPager.setAdapter(kategoriPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        //Refresh your stuff here
    }

}
