package com.example.android.geeklocations;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

class CategoryAdapter extends FragmentStatePagerAdapter {

    private final String[] tabTitles = new String[]{"Fandom Location", "Visit This Location"};
    private final Bundle fragmentBundle;

    CategoryAdapter(FragmentManager fm, Bundle data) {
        super(fm);
        fragmentBundle = data;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            FandomLocationDetailFragment fandomLocationDetailFragment = new FandomLocationDetailFragment();
            fandomLocationDetailFragment.setArguments(fragmentBundle);
            return fandomLocationDetailFragment;
        } else {
            RealLocationDetailFragment realLocationDetailFragment = new RealLocationDetailFragment();
            realLocationDetailFragment.setArguments(fragmentBundle);
            return realLocationDetailFragment;
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

