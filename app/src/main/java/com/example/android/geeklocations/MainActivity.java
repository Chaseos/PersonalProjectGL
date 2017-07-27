package com.example.android.geeklocations;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.geeklocations.data.FandomContract.MainListEntry;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String FANDOM_NAME_ID = "fandom_id";
    public static final String ARG_ITEM_ID = "item_id";
    private static final int ID_MAIN_LIST_LOADER = 123;
    private ActionBarDrawerToggle drawerToggle;
    private RecyclerView mRecyclerView;
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Drawer setup
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        mDrawerLayout.addDrawerListener(drawerToggle);
        NavigationView nvDrawer = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(nvDrawer);

        //List setup
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_fandoms);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

        //Loader setup
        getSupportLoaderManager().initLoader(ID_MAIN_LIST_LOADER, null, this);
    }

    //Loader which uses CursorLoader with @FandomsContentProvider to return relevant Cursor
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, MainListEntry.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        FandomAdapter mFandomAdapter = new FandomAdapter(this, data);
        mRecyclerView.setAdapter(mFandomAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    //Custom RecyclerView Adapter which sets Views after Cursor is loaded
    class FandomAdapter
            extends RecyclerView.Adapter<FandomAdapter.MainListAdapterViewHolder> {

        private final Context mContext;
        private final Cursor mCursor;

        FandomAdapter(@NonNull Context context, Cursor cursor) {
            mContext = context;
            mCursor = cursor;
        }

        @Override
        public MainListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.activity_main_list_item, parent, false);
            view.setFocusable(true);
            return new MainListAdapterViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MainListAdapterViewHolder mainListAdapterViewHolder, int position) {
            mCursor.moveToPosition(position);
            String fandomName = mCursor.getString
                    (mCursor.getColumnIndexOrThrow(MainListEntry.MAIN_COLUMN_FANDOMS));
            String fandomBackgroundImage = mCursor.getString
                    (mCursor.getColumnIndexOrThrow(MainListEntry.MAIN_COLUMN_BACKGROUNDS));
            int fandomBackgroundId = mContext.getResources()
                    .getIdentifier(fandomBackgroundImage, "drawable", mContext.getPackageName());
            String font = mCursor.getString(
                    mCursor.getColumnIndexOrThrow(MainListEntry.MAIN_COLUMN_FONT));
            Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/" + font + ".TTF");
            int fontSize = mCursor.getInt(mCursor.getColumnIndexOrThrow(MainListEntry.MAIN_COLUMN_FONT_SIZE));

            mainListAdapterViewHolder.fandomNameView.setText(fandomName);
            mainListAdapterViewHolder.fandomNameView.setTypeface(custom_font);
            mainListAdapterViewHolder.fandomNameView.setTextSize(fontSize);
            Picasso.with(mContext).load(fandomBackgroundId).into(mainListAdapterViewHolder.backgroundView);
        }

        @Override
        public int getItemCount() {
            if (mCursor == null) return 0;
            return mCursor.getCount();
        }

        class MainListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            final ImageView backgroundView;
            final TextView fandomNameView;

            MainListAdapterViewHolder(View itemView) {
                super(itemView);

                backgroundView = (ImageView) itemView.findViewById(R.id.fandom_background_imageview);
                fandomNameView = (TextView) itemView.findViewById(R.id.fandom_name_textview);

                itemView.setOnClickListener(this);
            }

            //onClick will send to @FandomListActivity. A list of the specified Fandom. Uses Content Provider
            @Override
            public void onClick(View v) {
                Intent fandomChoiceIntent = new Intent(MainActivity.this, FandomListActivity.class);
                int adapterPosition = getAdapterPosition();
                mCursor.moveToPosition(adapterPosition);
                int fandomNameId = mCursor.getInt(mCursor.getColumnIndexOrThrow(MainListEntry._ID));
                fandomChoiceIntent.putExtra(FANDOM_NAME_ID, fandomNameId);
                startActivity(fandomChoiceIntent);
            }
        }
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        selectDrawerItem(item);
                        return true;
                    }
                }
        );
    }

    private void selectDrawerItem(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.favorites:
                intent = new Intent(MainActivity.this, FavoriteListActivity.class);
                break;
            case R.id.nav_suggestions:
                intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, getString(R.string.my_email));
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject_line));
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_body_text));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                break;
            case R.id.nav_copyrights:
                intent = new Intent(MainActivity.this, CopyrightsActivity.class);
                break;
            case R.id.nav_about:
                intent = new Intent(MainActivity.this, AboutActivity.class);
                break;
            case R.id.nav_exit:
                System.exit(0);
                break;
            default:
                mDrawerLayout.closeDrawers();
                return;
        }

        item.setChecked(true);
        mDrawerLayout.closeDrawers();
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
}
