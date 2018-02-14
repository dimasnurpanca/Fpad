package com.android.fpad.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.android.fpad.fragment.FragmentA;
import com.android.fpad.fragment.FragmentB;
import com.android.fpad.fragment.FragmentC;
import com.android.fpad.fragment.FragmentD;

/**
 * Created by Priyabrat on 21-08-2015.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new FragmentA();
        }
        else if (position == 1)
        {
            fragment = new FragmentB();
        }
        else if (position == 2)
        {
            fragment = new FragmentC();
        }
        else if (position == 3)
        {
            fragment = new FragmentD();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "Explore";
        }
        else if (position == 1)
        {
            title = "Hot";
        }
        else if (position == 2)
        {
            title = "Rising";
        }
        else if (position == 3)
        {
            title = "New";
        }
        return title;
    }
}
