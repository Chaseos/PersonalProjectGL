package com.example.android.geeklocations;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

public class FullScreenImageActivity extends FragmentActivity {

    private int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_image);

        ViewPager viewPager = (ViewPager) findViewById(R.id.full_screen_image_viewpager);

        if (savedInstanceState != null) {
            position = savedInstanceState.getInt("position");
        } else {
            position = getIntent().getExtras().getInt("position");
        }

        ImageGalleryAdapter imageGalleryAdapter = new ImageGalleryAdapter(getSupportFragmentManager(), getIntent().getExtras());
        viewPager.setAdapter(imageGalleryAdapter);
        viewPager.setCurrentItem(position);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("position", position);
        super.onSaveInstanceState(outState);
    }

    private class ImageGalleryAdapter extends FragmentStatePagerAdapter {
        private final ArrayList<String> mImages;

        ImageGalleryAdapter(FragmentManager fm, Bundle data) {
            super(fm);
            mImages = data.getStringArrayList("images");
        }

        @Override
        public int getCount() {
            return mImages.size();
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            FullScreenImageFragment fullScreenImageFragment = new FullScreenImageFragment();
            bundle.putString("image", mImages.get(position));
            fullScreenImageFragment.setArguments(bundle);
            return fullScreenImageFragment;
        }
    }
}
