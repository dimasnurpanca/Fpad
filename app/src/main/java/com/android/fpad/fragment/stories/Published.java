package com.android.fpad.fragment.stories;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.fpad.R;
import com.android.fpad.adapter.stories.PublishedAdapter;
import com.android.fpad.retrofit.APIClient;
import com.android.fpad.retrofit.APIInterface;
import com.android.fpad.retrofit.StoryList;
import com.android.fpad.ui.stories.StoryActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class Published extends Fragment {


    RecyclerView list;
    ProgressBar spinner;
    APIInterface apiInterface;
    ArrayList<StoryList> dataModels;
    private static PublishedAdapter adapter;

    public Published() {
        apiInterface = APIClient.getClient().create(APIInterface.class); //retrofit

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment, container, false);
        dataModels= new ArrayList<>();
        list = (RecyclerView) view.findViewById(R.id.recycler_view);
        spinner = (ProgressBar) view.findViewById(R.id.progressBar1);
        spinner.setVisibility(View.VISIBLE);
        StoryActivity activity = (StoryActivity) getActivity();
        Call<List<StoryList>> call3 = apiInterface.doGetStoryUserList(activity.getEmail(),"Published");
        call3.enqueue(new Callback<List<StoryList>>() {
            @Override
            public void onResponse(Call<List<StoryList>> call, Response<List<StoryList>> response) {

                List<StoryList> jsonResponse = response.body();
                for (int i = 0; i < jsonResponse.size(); i++) {
               dataModels.add(new StoryList(jsonResponse.get(i).getId(),jsonResponse.get(i).getEmail(),jsonResponse.get(i).getKategori_id(),jsonResponse.get(i).getTitle(),jsonResponse.get(i).getDescription(),jsonResponse.get(i).getImage(),jsonResponse.get(i).getContent(),jsonResponse.get(i).getDate(),jsonResponse.get(i).getLast_update(),jsonResponse.get(i).getStatus(),jsonResponse.get(i).getRead(),jsonResponse.get(i).getLike(),jsonResponse.get(i).getComment()));
                    }

                PublishedAdapter adapter = new PublishedAdapter(dataModels,getActivity());
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                list.setLayoutManager(layoutManager);
                list.setAdapter(adapter);
                spinner.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<StoryList>> call, Throwable t) {
                spinner.setVisibility(View.GONE);
                call.cancel();
            }
        });

        //CustomAdapter adapter = new CustomAdapter(stringList,getActivity());
       //list.setAdapter(adapter);

        return view;
    }

}