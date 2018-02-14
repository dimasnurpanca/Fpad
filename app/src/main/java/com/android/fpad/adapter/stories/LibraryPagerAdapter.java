package com.android.fpad.adapter.stories;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.android.fpad.R;
import com.android.fpad.fragment.stories.ReadingLists;

/**
 * Created by Priyabrat on 21-08-2015.
 */
public class LibraryPagerAdapter extends FragmentPagerAdapter {
    Context context;
    public LibraryPagerAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        this.context = mContext;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new ReadingLists();
        }


        return fragment;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = context.getResources().getString(R.string.readinglists_tab_title);
        }
        return title;
    }
}
