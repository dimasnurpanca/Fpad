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
import com.android.fpad.fragment.stories.Drafts;
import com.android.fpad.fragment.stories.Published;

/**
 * Created by Priyabrat on 21-08-2015.
 */
public class StoryPagerAdapter extends FragmentPagerAdapter {
    Context context;
    public StoryPagerAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        this.context = mContext;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new Published();
        }
        else if (position == 1)
        {
            fragment = new Drafts();
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = context.getResources().getString(R.string.published_tab_title);
        }
        else if (position == 1)
        {
            title = context.getResources().getString(R.string.drafts_tab_title);
        }
        return title;
    }
}
