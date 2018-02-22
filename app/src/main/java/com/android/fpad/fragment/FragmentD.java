package com.android.fpad.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.fpad.adapter.RecyclerAdapter;
import com.android.fpad.retrofit.APIClient;
import com.android.fpad.retrofit.APIInterface;
import com.android.fpad.retrofit.StoryList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tr.xip.errorview.ErrorView;

import com.android.fpad.R;
public class FragmentD extends Fragment {


    RecyclerView list;
    ProgressBar spinner;
    ErrorView errorView;
    APIInterface apiInterface;
    ArrayList<StoryList> dataModels;

    public FragmentD() {
        apiInterface = APIClient.getClient().create(APIInterface.class); //retrofit

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment, container, false);
        dataModels= new ArrayList<>();
        list = (RecyclerView) view.findViewById(R.id.recycler_view);
        errorView = (ErrorView) view.findViewById(R.id.error_view);
        spinner = (ProgressBar) view.findViewById(R.id.progressBar1);
        spinner.setVisibility(View.VISIBLE);
        Call<List<StoryList>> call3 = apiInterface.doGetStoryType("new");
        call3.enqueue(new Callback<List<StoryList>>() {
            @Override
            public void onResponse(Call<List<StoryList>> call, Response<List<StoryList>> response) {
                int totals= 0;
                List<StoryList> jsonResponse = response.body();
                for (int i = 0; i < jsonResponse.size(); i++) {
                    dataModels.add(new StoryList(jsonResponse.get(i).getId(),jsonResponse.get(i).getEmail(),jsonResponse.get(i).getKategori_id(),jsonResponse.get(i).getTitle(),jsonResponse.get(i).getDescription(),jsonResponse.get(i).getImage(),jsonResponse.get(i).getContent(),jsonResponse.get(i).getDate(),jsonResponse.get(i).getLast_update(),jsonResponse.get(i).getStatus(),jsonResponse.get(i).getRead(),jsonResponse.get(i).getLike(),jsonResponse.get(i).getComment()));
                    totals++;
                }

                RecyclerAdapter adapter = new RecyclerAdapter(dataModels,getActivity());
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                list.setLayoutManager(layoutManager);
                list.setAdapter(adapter);
                spinner.setVisibility(View.GONE);
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
                spinner.setVisibility(View.GONE);
                errorView.setVisibility(View.VISIBLE);
                initErrorView();
            }
        });

        //CustomAdapter adapter = new CustomAdapter(stringList,getActivity());
        //list.setAdapter(adapter);

        return view;
    }
    private void initErrorView() {
        errorView.setTitle("Not Found\n");

        errorView.setSubtitleVisible(true);
        errorView.setRetryText("Reload");
        errorView.setRetryVisible(true);
        errorView.setRetryListener(new ErrorView.RetryListener() {
            @Override
            public void onRetry() {
                Fragment frg = null;
                frg = getFragmentManager().getFragments().get(3);
                final android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();
            }
        });
    }
}