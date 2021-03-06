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

import com.android.fpad.adapter.KategoriAdapter;
import com.android.fpad.adapter.RecyclerAdapter;
import com.android.fpad.retrofit.APIClient;
import com.android.fpad.retrofit.APIInterface;
import com.android.fpad.retrofit.KategoriList;
import com.android.fpad.retrofit.UserList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tr.xip.errorview.ErrorView;

import com.android.fpad.R;
public class FragmentA extends Fragment {


    RecyclerView list;
    ProgressBar spinner;
    ErrorView errorView;
    APIInterface apiInterface;
    ArrayList<KategoriList> dataModels;
    private static KategoriAdapter adapter;

    public FragmentA() {
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
        Call<List<KategoriList>> call3 = apiInterface.doGetKategoriList();
        call3.enqueue(new Callback<List<KategoriList>>() {
            @Override
            public void onResponse(Call<List<KategoriList>> call, Response<List<KategoriList>> response) {
                int totals= 0;
                List<KategoriList> jsonResponse = response.body();
                for (int i = 0; i < jsonResponse.size(); i++) {
               dataModels.add(new KategoriList(jsonResponse.get(i).getId(),jsonResponse.get(i).getNama_kategori()));
                    totals++;
                    }




                KategoriAdapter adapter = new KategoriAdapter(dataModels,getActivity());
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
            public void onFailure(Call<List<KategoriList>> call, Throwable t) {
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
                frg = getFragmentManager().getFragments().get(0);
                final android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();
            }
        });
    }
}