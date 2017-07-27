package com.example.android.geeklocations;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.android.geeklocations.data.FandomContract.*;

public class ImageGalleryActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ID_GALLERY_LOADER = 288;
    private RecyclerView mRecyclerView;
    private String identifier;
    private Uri galleryUri;
    private int galleryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_image_gallery);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState != null) {
            identifier = savedInstanceState.getString("identifier");
            galleryId = savedInstanceState.getInt(MainActivity.ARG_ITEM_ID);
        } else {
            identifier = getIntent().getExtras().getString(FandomDetailActivity.ARG_IDENTIFIER);
            galleryId = getIntent().getExtras().getInt(MainActivity.ARG_ITEM_ID);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.image_gallery_recyclerview);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setHasFixedSize(true);

        getSupportLoaderManager().initLoader(ID_GALLERY_LOADER, null, this);
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
        String[] projection = null;
        String selection = null;

        //Projection setup pertaining to passed in Intent identifier
        switch (identifier) {
            case "fandom":
                projection = new String[]{
                        FandomWorldTableEntries._ID,
                        FandomWorldTableEntries.COLUMN_FANDOM_IMAGE_ONE,
                        FandomWorldTableEntries.COLUMN_FANDOM_IMAGE_TWO,
                        FandomWorldTableEntries.COLUMN_FANDOM_IMAGE_THREE,
                        FandomWorldTableEntries.COLUMN_FANDOM_IMAGE_FOUR,
                        FandomWorldTableEntries.COLUMN_FANDOM_IMAGE_FIVE,
                        FandomWorldTableEntries.COLUMN_FANDOM_IMAGE_SIX};
                selection = FandomWorldTableEntries._ID + " = ?";
                galleryUri = FandomWorldTableEntries.buildFandomUriInfoWithId(galleryId);
                break;
            case "adaptation":
                projection = new String[]{
                        FandomWorldTableEntries._ID,
                        FandomWorldTableEntries.COLUMN_ADAPTATION_IMAGE_ONE,
                        FandomWorldTableEntries.COLUMN_ADAPTATION_IMAGE_TWO,
                        FandomWorldTableEntries.COLUMN_ADAPTATION_IMAGE_THREE,
                        FandomWorldTableEntries.COLUMN_ADAPTATION_IMAGE_FOUR,
                        FandomWorldTableEntries.COLUMN_ADAPTATION_IMAGE_FIVE,
                        FandomWorldTableEntries.COLUMN_ADAPTATION_IMAGE_SIX};
                selection = FandomWorldTableEntries._ID + " = ?";
                galleryUri = FandomWorldTableEntries.buildFandomUriInfoWithId(galleryId);
                break;
            case "filming":
                projection = new String[]{
                        FandomWorldTableEntries._ID,
                        FandomWorldTableEntries.COLUMN_FILMING_IMAGE_ONE,
                        FandomWorldTableEntries.COLUMN_FILMING_IMAGE_TWO,
                        FandomWorldTableEntries.COLUMN_FILMING_IMAGE_THREE,
                        FandomWorldTableEntries.COLUMN_FILMING_IMAGE_FOUR,
                        FandomWorldTableEntries.COLUMN_FILMING_IMAGE_FIVE,
                        FandomWorldTableEntries.COLUMN_FILMING_IMAGE_SIX};
                selection = FandomWorldTableEntries._ID + " = ?";
                galleryUri = FandomWorldTableEntries.buildFandomUriInfoWithId(galleryId);
                break;
            case "inspiration":
                projection = new String[]{
                        FandomWorldTableEntries._ID,
                        FandomWorldTableEntries.COLUMN_INSPIRATION_IMAGE_ONE,
                        FandomWorldTableEntries.COLUMN_INSPIRATION_IMAGE_TWO,
                        FandomWorldTableEntries.COLUMN_INSPIRATION_IMAGE_THREE,
                        FandomWorldTableEntries.COLUMN_INSPIRATION_IMAGE_FOUR,
                        FandomWorldTableEntries.COLUMN_INSPIRATION_IMAGE_FIVE,
                        FandomWorldTableEntries.COLUMN_INSPIRATION_IMAGE_SIX};
                selection = FandomWorldTableEntries._ID + " = ?";
                galleryUri = FandomWorldTableEntries.buildFandomUriInfoWithId(galleryId);
                break;
        }
        return new CursorLoader(this, galleryUri, projection, selection, null, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        ArrayList<String> imageList = new ArrayList<>();

        //Images setup for Fandom Location
        setupImageData(data, imageList);

        ImageAdapter mImageAdapter = new ImageAdapter(this, imageList);
        mRecyclerView.setAdapter(mImageAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private void setupImageData(Cursor data, ArrayList<String> imageList) {
        switch (identifier) {
            case "fandom": {
                imageList.add(data.getString(data
                        .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FANDOM_IMAGE_ONE)));
                String imageTwo = data.getString(data
                        .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FANDOM_IMAGE_TWO));
                if (!TextUtils.isEmpty(imageTwo)) imageList.add(imageTwo);
                String imageThree = data.getString(data
                        .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FANDOM_IMAGE_THREE));
                if (!TextUtils.isEmpty(imageThree)) imageList.add(imageThree);
                String imageFour = data.getString(data
                        .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FANDOM_IMAGE_FOUR));
                if (!TextUtils.isEmpty(imageFour)) imageList.add(imageFour);
                String imageFive = data.getString(data
                        .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FANDOM_IMAGE_FIVE));
                if (!TextUtils.isEmpty(imageFive)) imageList.add(imageFive);
                String imageSix = data.getString(data
                        .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FANDOM_IMAGE_SIX));
                if (!TextUtils.isEmpty(imageSix)) imageList.add(imageSix);

                break;
            }
                //Images setup for Adaptation Location
            case "adaptation": {
                imageList.add(data.getString(data
                        .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_ADAPTATION_IMAGE_ONE)));
                String imageTwo = data.getString(data
                        .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_ADAPTATION_IMAGE_TWO));
                if (!TextUtils.isEmpty(imageTwo)) imageList.add(imageTwo);
                String imageThree = data.getString(data
                        .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_ADAPTATION_IMAGE_THREE));
                if (!TextUtils.isEmpty(imageThree)) imageList.add(imageThree);
                String imageFour = data.getString(data
                        .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_ADAPTATION_IMAGE_FOUR));
                if (!TextUtils.isEmpty(imageFour)) imageList.add(imageFour);
                String imageFive = data.getString(data
                        .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_ADAPTATION_IMAGE_FIVE));
                if (!TextUtils.isEmpty(imageFive)) imageList.add(imageFive);
                String imageSix = data.getString(data
                        .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_ADAPTATION_IMAGE_SIX));
                if (!TextUtils.isEmpty(imageSix)) imageList.add(imageSix);

                break;
            }
                //Images setup for Filming Location
            case "filming": {
                imageList.add(data.getString(data
                        .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FILMING_IMAGE_ONE)));
                String imageTwo = data.getString(data
                        .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FILMING_IMAGE_TWO));
                if (!TextUtils.isEmpty(imageTwo)) imageList.add(imageTwo);
                String imageThree = data.getString(data
                        .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FILMING_IMAGE_THREE));
                if (!TextUtils.isEmpty(imageThree)) imageList.add(imageThree);
                String imageFour = data.getString(data
                        .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FILMING_IMAGE_FOUR));
                if (!TextUtils.isEmpty(imageFour)) imageList.add(imageFour);
                String imageFive = data.getString(data
                        .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FILMING_IMAGE_FIVE));
                if (!TextUtils.isEmpty(imageFive)) imageList.add(imageFive);
                String imageSix = data.getString(data
                        .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FILMING_IMAGE_SIX));
                if (!TextUtils.isEmpty(imageSix)) imageList.add(imageSix);

                break;
            }
                //Images Setup for Inspiration Location
            case "inspiration": {
                imageList.add(data.getString(data
                        .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_INSPIRATION_IMAGE_ONE)));
                String imageTwo = data.getString(data
                        .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_INSPIRATION_IMAGE_TWO));
                if (!TextUtils.isEmpty(imageTwo)) imageList.add(imageTwo);
                String imageThree = data.getString(data
                        .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_INSPIRATION_IMAGE_THREE));
                if (!TextUtils.isEmpty(imageThree)) imageList.add(imageThree);
                String imageFour = data.getString(data
                        .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_INSPIRATION_IMAGE_FOUR));
                if (!TextUtils.isEmpty(imageFour)) imageList.add(imageFour);
                String imageFive = data.getString(data
                        .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_INSPIRATION_IMAGE_FIVE));
                if (!TextUtils.isEmpty(imageFive)) imageList.add(imageFive);
                String imageSix = data.getString(data
                        .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_INSPIRATION_IMAGE_SIX));
                if (!TextUtils.isEmpty(imageSix)) imageList.add(imageSix);
                break;
            }
        }
    }

    class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
        private final Context mContext;
        private final ArrayList<String> mImageList;

        ImageAdapter(Context context, ArrayList<String> imageList) {
            mContext = context;
            mImageList = imageList;
        }

        @Override
        public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.image_gallery_item, parent, false);
            view.setFocusable(true);
            return new ImageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ImageViewHolder holder, int position) {
            int imageId = mContext.getResources()
                    .getIdentifier(mImageList.get(position), "drawable", mContext.getPackageName());
            Picasso.with(mContext).load(imageId).into(holder.img);
        }

        @Override
        public int getItemCount() {
            return mImageList.size();
        }

        class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            final ImageView img;

            public ImageViewHolder(View itemView) {
                super(itemView);
                img = (ImageView) itemView.findViewById(R.id.image_gallery_image);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("images", mImageList);
                bundle.putInt("position", position);

                Intent intent = new Intent(mContext, FullScreenImageActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("identifier", getIntent().getExtras().getString(FandomDetailActivity.ARG_IDENTIFIER));
        super.onSaveInstanceState(outState);
    }
}
