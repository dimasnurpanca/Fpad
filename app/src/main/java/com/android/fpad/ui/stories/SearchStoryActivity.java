package com.android.fpad.ui.stories;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.fpad.R;
import com.android.fpad.adapter.stories.CommentsAdapter;
import com.android.fpad.adapter.stories.SearchAdapter;
import com.android.fpad.retrofit.APIClient;
import com.android.fpad.retrofit.APIInterface;
import com.android.fpad.retrofit.CommentList;
import com.android.fpad.retrofit.KategoriList;
import com.android.fpad.retrofit.StoryList;
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
import tr.xip.errorview.ErrorView;


public class SearchStoryActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.error_view)
    ErrorView errorView;
    @BindView(R.id.recycler_views)
    RecyclerView recyclerView;
    Activity context = this;
    String usrmail,text;
    SharedPreferences sharedpreferences;
    APIInterface apiInterface;
    ArrayList<StoryList> dataModels;

    private SearchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // back button


        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
        usrmail = sharedpreferences.getString("email", null);
        apiInterface = APIClient.getClient().create(APIInterface.class); //retrofit
        Intent intent = getIntent();
        text = intent.getExtras().getString("text");
        setTitle(text);

        initViews();
    }

    private void initViews(){
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        loadJSON(text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.trim().length() >= 3) {
                    loadJSON(query);
                    setTitle(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.trim().length() >= 3) {
                    loadJSON(newText);
                    setTitle(newText);
                }
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
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


    private void loadJSON(String text){
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(SearchStoryActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
        dataModels= new ArrayList<>();
        Call<List<StoryList>> call3 = apiInterface.doGetStorySearch("search",text);
        call3.enqueue(new Callback<List<StoryList>>() {
            @Override
            public void onResponse(Call<List<StoryList>> call, Response<List<StoryList>> response) {
                int totals= 0;
                progressDialog.dismiss();
                List<StoryList> jsonResponse = response.body();
                for (int i = 0; i < jsonResponse.size(); i++) {
                    dataModels.add(new StoryList(jsonResponse.get(i).getId(),jsonResponse.get(i).getEmail(),jsonResponse.get(i).getKategori_id(),jsonResponse.get(i).getTitle(),jsonResponse.get(i).getDescription(),jsonResponse.get(i).getImage(),jsonResponse.get(i).getContent(),jsonResponse.get(i).getDate(),jsonResponse.get(i).getLast_update(),jsonResponse.get(i).getStatus(),jsonResponse.get(i).getRead(),jsonResponse.get(i).getLike(),jsonResponse.get(i).getComment()));
                    totals++;
                }

                adapter = new SearchAdapter(dataModels,context);
                recyclerView.setAdapter(adapter);
                if(totals==0){
                    errorView.setVisibility(View.VISIBLE);
                    initErrorView();
                }else{
                    errorView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<StoryList>> call, Throwable t) {
                call.cancel();
                progressDialog.dismiss();
                errorView.setVisibility(View.VISIBLE);
                initErrorView();
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

    private void initErrorView() {
        errorView.setTitle("Not Found\n");
        errorView.setSubtitleVisible(true);
        errorView.setRetryVisible(false);

    }

}
