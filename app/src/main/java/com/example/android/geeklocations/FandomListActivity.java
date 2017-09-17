package com.example.android.geeklocations;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import static com.example.android.geeklocations.data.FandomContract.FandomWorldTableEntries;

public class FandomListActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ID_FANDOM_LIST_LOADER = 29;
    public static final int FAVORITE_CHANGED = 343;
    private Toolbar toolbar;
    private int fandomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fandom_list);

        toolbar = (Toolbar) findViewById(R.id.toolbar_list);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState != null) {
            fandomId = savedInstanceState.getInt(MainActivity.FANDOM_NAME_ID);
        } else {
            fandomId = getIntent().getExtras().getInt(MainActivity.FANDOM_NAME_ID);
        }

        setupViewPager();

        getSupportLoaderManager().initLoader(ID_FANDOM_LIST_LOADER, null, this);
    }

    private void setupViewPager() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.lists_viewpager);

        Bundle arguments = new Bundle();
        arguments.putInt(MainActivity.FANDOM_NAME_ID, fandomId);

        LocationsListPagerAdapter locationsListPagerAdapter = new LocationsListPagerAdapter(getSupportFragmentManager(), arguments);

        viewPager.setAdapter(locationsListPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.lists_tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = new String[]{
                FandomWorldTableEntries.COLUMN_FANDOM_NAME,
                FandomWorldTableEntries.COLUMN_FANDOM_ID};
        String selection = FandomWorldTableEntries.COLUMN_FANDOM_ID + " = ?";
        Uri fandomUri = FandomWorldTableEntries.buildFandomUriInfoWithId(fandomId);
        return new CursorLoader(this, fandomUri, projection, selection, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        String titleName = data.getString(data.getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FANDOM_NAME));
        toolbar.setTitle(titleName + " Locations");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(MainActivity.FANDOM_NAME_ID, fandomId);
        super.onSaveInstanceState(outState);
    }

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
}
