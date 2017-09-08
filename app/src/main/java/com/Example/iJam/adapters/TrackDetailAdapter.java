package com.Example.iJam.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.Example.iJam.fragments.TrackDetailFragment;
import com.Example.iJam.fragments.TrackParentFragment;

/**
 * Created by Khodary on 7/9/15.
 */
public class TrackDetailAdapter extends FragmentStatePagerAdapter {

    public TrackDetailAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        if(position==0)
            return TrackDetailFragment.newInstance("TrackDetailFragment, Instance 1");

        else if(position==1)
            return TrackParentFragment.newInstance("TrackParentFragment, Instance 1");


        //MainActivity.MyFragment myFragment = MainActivity.MyFragment.newInstance(position);
        //return myFragment;
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0)
            return "Track Detail";
        else if(position==1)
            return "Track Parents";


        return "Tab " + (position + 1);
    }
}