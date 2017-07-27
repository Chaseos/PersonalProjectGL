package com.example.android.geeklocations;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import static com.example.android.geeklocations.data.FandomContract.FandomWorldTableEntries;

public class FavoriteListActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private Toolbar toolbar;

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

        getSupportActionBar().setTitle("Favorites");

        setupViewPager();
    }

    private void setupViewPager() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.lists_viewpager);
        FavoritesListPagerAdapter favoritesListPagerAdapter = new FavoritesListPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(favoritesListPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.lists_tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = new String[]{
                FandomWorldTableEntries.COLUMN_FANDOM_NAME,
                FandomWorldTableEntries.COLUMN_FANDOM_ID};
        String selection = FandomWorldTableEntries.COLUMN_FANDOM_ID + " = ?";
        Uri fandomUri = FandomWorldTableEntries.buildFandomUriInfoWithFavorites();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                navigateUpTo(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
