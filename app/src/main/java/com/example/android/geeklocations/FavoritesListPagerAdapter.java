package com.example.android.geeklocations;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

class FavoritesListPagerAdapter extends FragmentStatePagerAdapter {

    private final String[] tabTitles = new String[]{"Fandom Locations", "Real Locations"};

    FavoritesListPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new FavoritesFandomLocationsListFragment();
        } else {
            return new FavoritesRealLocationsListFragment();
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