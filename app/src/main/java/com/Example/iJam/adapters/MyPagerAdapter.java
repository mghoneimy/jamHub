package com.Example.iJam.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.Example.iJam.fragments.BandsFragment;
import com.Example.iJam.fragments.ProfileFragment;
import com.Example.iJam.fragments.SearchFragment;
import com.Example.iJam.fragments.TopTracksFragment;

/**
 * Created by Khodary on 7/9/15.
 */
public class MyPagerAdapter extends FragmentStatePagerAdapter {

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        if(position==0)
            return TopTracksFragment.newInstance("TopTracksFragment, Instance 1");

        else if(position==1)
            return SearchFragment.newInstance("SearchFragment, Instance 1");
        else if (position==2)
            return BandsFragment.newInstance("BandsFragment, Instance 1");
        else if (position==3)
            return ProfileFragment.newInstance("ProfileFragment, Instance 1");

        //MainActivity.MyFragment myFragment = MainActivity.MyFragment.newInstance(position);
        //return myFragment;
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0)
            return "Top Tracks";
        else if(position==1)
            return "Search";
        else if(position==2)
            return "Bands";
        else if(position==3)
            return "Profile";

        return "Tab " + (position + 1);
    }
}