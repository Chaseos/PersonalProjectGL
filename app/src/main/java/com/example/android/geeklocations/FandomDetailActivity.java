package com.example.android.geeklocations;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import static com.example.android.geeklocations.data.FandomContract.FandomWorldTableEntries;

public class FandomDetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout appBarLayout;
    private static final int ID_FANDOM_ACTIVITY_LOADER = 667;
    public static final String ARG_IDENTIFIER = "identifier";
    private Drawable locationBackgroundDrawable;
    private String fandomLocationName;
    private boolean orientationChange;
    private boolean favoriteChanged;
    private ContentValues values;
    private ViewPager viewPager;
    private int favoriteStatus;
    private Uri locationUri;
    private Toolbar toolbar;
    private int locationId;
    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fandom_detail);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide(Gravity.BOTTOM);
            slide.addTarget(R.id.viewpager);
            getWindow().setEnterTransition(slide);
        }

        toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        appBarLayout = (net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        if (savedInstanceState != null) {
            locationId = savedInstanceState.getInt(MainActivity.ARG_ITEM_ID);
            favoriteStatus = savedInstanceState.getInt("favoriteStatus");
            favoriteChanged = savedInstanceState.getBoolean("favoriteChanged");
            orientationChange = true;
        } else {
            locationId = getIntent().getExtras().getInt(MainActivity.ARG_ITEM_ID);
        }

        getSupportLoaderManager().initLoader(ID_FANDOM_ACTIVITY_LOADER, null, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Updates if favorite is changed then returns to last activity
            case android.R.id.home:
                Intent returnIntent = new Intent();
                if (favoriteChanged) {
                    setResult(Activity.RESULT_OK, returnIntent);
                    values = new ContentValues();
                    values.put(FandomWorldTableEntries.COLUMN_FANDOM_LOCATION_NAME, fandomLocationName);
                    values.put(FandomWorldTableEntries.COLUMN_FANDOM_FAVORITE, favoriteStatus);
                    getContentResolver().update(locationUri, values, null, null);
                } else {
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                }
                finish();
                return true;
            case R.id.action_favorite:
                if (favoriteChanged) {
                    favoriteChanged = false;
                } else {
                    Toast.makeText(this, "Removed from Favorites", Toast.LENGTH_SHORT).show();
                    favoriteChanged = true;
                }
                favoriteStatus = 0;
                invalidateOptionsMenu();
                onPrepareOptionsMenu(mMenu);
                break;
            case R.id.action_not_favorite:
                if (favoriteChanged) {
                    favoriteChanged = false;
                } else {
                    Toast.makeText(this, "Added to Favorites", Toast.LENGTH_SHORT).show();
                    favoriteChanged = true;
                }
                favoriteStatus = 1;
                invalidateOptionsMenu();
                onPrepareOptionsMenu(mMenu);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = new String[]{
                FandomWorldTableEntries._ID,
                FandomWorldTableEntries.COLUMN_FANDOM_ID,
                FandomWorldTableEntries.COLUMN_FANDOM_LOCATION_NAME,
                FandomWorldTableEntries.COLUMN_FANDOM_FONT,
                FandomWorldTableEntries.COLUMN_FANDOM_IMAGE_ONE,
                FandomWorldTableEntries.COLUMN_FANDOM_FAVORITE};
        String selection = FandomWorldTableEntries._ID + " = ?";
        locationUri = FandomWorldTableEntries.buildFandomUriInfoWithId(locationId);
        return new CursorLoader(this, locationUri, projection, selection, null, null);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();

        setupData(data);
        setupAppBar(data);
        setupViewPager();
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
    }

    //Collects data from Cursor for later use
    private void setupData(Cursor data) {
        if (!orientationChange) {
            favoriteStatus = data.getInt(data.getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FANDOM_FAVORITE));
        }
        fandomLocationName = data.getString(data
                .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FANDOM_LOCATION_NAME));
        String locationBackgroundImage = data.getString(data
                .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FANDOM_IMAGE_ONE));
        int locationBackgroundId = getResources()
                .getIdentifier(locationBackgroundImage, "drawable", getPackageName());
        locationBackgroundDrawable = getResources().getDrawable(locationBackgroundId);
    }

    // Creates a collapsing app bar
    private void setupAppBar(Cursor data) {
        String font = data.getString(
                data.getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FANDOM_FONT));
        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/" + font + ".TTF");

        appBarLayout.setBackground(locationBackgroundDrawable);
        appBarLayout.setTitle(fandomLocationName);
        appBarLayout.setExpandedTitleTypeface(customFont);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    //Sets up viewpager for detailed fragments
    private void setupViewPager() {
        Bundle arguments = new Bundle();
        arguments.putInt(MainActivity.ARG_ITEM_ID, locationId);
        CategoryAdapter categoryAdapter = new CategoryAdapter(getSupportFragmentManager(), arguments);

        if (viewPager == null) {
            viewPager = (ViewPager) findViewById(R.id.viewpager);
            TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
            viewPager.setAdapter(categoryAdapter);
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    //Menu shows favorited if favorited and vica versa
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (favoriteStatus == 0) {
            getMenuInflater().inflate(R.menu.menu_not_favorite, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_favorite, menu);
        }
        mMenu = menu;
        return super.onPrepareOptionsMenu(menu);
    }

    //Saves arguments on orientation change
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(MainActivity.ARG_ITEM_ID, locationId);
        outState.putInt("favoriteStatus", favoriteStatus);
        outState.putBoolean("favoriteChanged", favoriteChanged);
        super.onSaveInstanceState(outState);
    }

    //Updates DB if favorite changed and returns to last activity
    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        if (favoriteChanged) {
            setResult(Activity.RESULT_OK, returnIntent);
            values = new ContentValues();
            values.put(FandomWorldTableEntries.COLUMN_FANDOM_LOCATION_NAME, fandomLocationName);
            values.put(FandomWorldTableEntries.COLUMN_FANDOM_FAVORITE, favoriteStatus);
            locationUri = FandomWorldTableEntries.buildFandomUriInfoWithId(locationId);
            getContentResolver().update(locationUri, values, null, null);
        } else {
            setResult(Activity.RESULT_CANCELED, returnIntent);
        }
        finish();
    }
}


