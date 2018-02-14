package com.android.fpad.adapter.stories;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.android.fpad.R;
import com.android.fpad.fragment.FragmentA;
import com.android.fpad.fragment.FragmentB;
import com.android.fpad.fragment.FragmentC;
import com.android.fpad.fragment.FragmentD;
import com.android.fpad.fragment.stories.Drafts;
import com.android.fpad.fragment.stories.Hot;
import com.android.fpad.fragment.stories.New;
import com.android.fpad.fragment.stories.Published;
import com.android.fpad.fragment.stories.Rising;

/**
 * Created by Priyabrat on 21-08-2015.
 */
public class KategoriPagerAdapter extends FragmentPagerAdapter {
    Context context;
    public KategoriPagerAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        this.context = mContext;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new Hot();
        }
        else if (position == 1)
        {
            fragment = new Rising();
        }
        else if (position == 2)
        {
            fragment = new New();
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "Hot";
        }
        else if (position == 1)
        {
            title = "Rising";
        }
        else if (position == 2)
        {
            title = "New";
        }
        return title;
    }
}
