package com.example.android.geeklocations;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class FullScreenImageFragment extends Fragment {

    public FullScreenImageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_full_screen_image, container, false);
        ImageView image = (ImageView) rootView.findViewById(R.id.fullscreen_imageview);
        String imageString = getArguments().getString("image");
        int imageResource = getContext().getResources()
                .getIdentifier(imageString, "drawable", getContext().getPackageName());
        Picasso.with(getContext()).load(imageResource).into(image);
        return rootView;
    }
}
