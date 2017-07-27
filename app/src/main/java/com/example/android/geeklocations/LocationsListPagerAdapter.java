package com.example.android.geeklocations;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

class LocationsListPagerAdapter  extends FragmentStatePagerAdapter {

    private final String[] tabTitles = new String[]{"Fandom Locations", "Real Locations"};
    private final Bundle fragmentBundle;

    LocationsListPagerAdapter(FragmentManager fm, Bundle data) {
        super(fm);
        fragmentBundle = data;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            FandomLocationsListFragment fandomLocationsListFragment= new FandomLocationsListFragment();
            fandomLocationsListFragment.setArguments(fragmentBundle);
            return fandomLocationsListFragment;
        } else {
            RealLocationsListFragment realLocationsListFragment = new RealLocationsListFragment();
            realLocationsListFragment.setArguments(fragmentBundle);
            return realLocationsListFragment;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}