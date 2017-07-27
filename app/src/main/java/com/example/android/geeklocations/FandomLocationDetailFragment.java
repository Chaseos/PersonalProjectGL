package com.example.android.geeklocations;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.android.geeklocations.data.FandomContract.FandomWorldTableEntries;

public class FandomLocationDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        View.OnClickListener {

    private static final int ID_FANDOM_FRAGMENT = 614;
    private ArrayList<LocationInfo> arrayOfDetails;
    private Context mContext;
    private Cursor mCursor;
    private View rootView;

    public FandomLocationDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        arrayOfDetails = new ArrayList<>();
        getLoaderManager().initLoader(ID_FANDOM_FRAGMENT, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fandom_detail, container, false);
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = new String[]{
                FandomWorldTableEntries._ID,
                FandomWorldTableEntries.COLUMN_FANDOM_ID,
                FandomWorldTableEntries.COLUMN_FANDOM_LOCATION_NAME,
                FandomWorldTableEntries.COLUMN_FANDOM_DESCRIPTION,
                FandomWorldTableEntries.COLUMN_SCENE_DESCRIPTION,
                FandomWorldTableEntries.COLUMN_FANDOM_TRIVIA,
                FandomWorldTableEntries.COLUMN_FANDOM_SCENE_VIDEO,
                FandomWorldTableEntries.COLUMN_FANDOM_ICON,
                FandomWorldTableEntries.COLUMN_FANDOM_LINK_ONE,
                FandomWorldTableEntries.COLUMN_FANDOM_LINK_TWO,
                FandomWorldTableEntries.COLUMN_FANDOM_LINK_THREE};
        String selection = FandomWorldTableEntries._ID + " = ?";
        int position = getArguments().getInt(MainActivity.ARG_ITEM_ID);
        Uri fandomUri = FandomWorldTableEntries.buildFandomUriInfoWithId(position);
        return new CursorLoader(mContext, fandomUri, projection, selection, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        mCursor = data;
        buildArrays();
        ButtonsAndLinksViewAdapter();
        createRecyclerView();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    /**
     * Location info is put into LocationInfo objects and regurgitated later into recyclerView.
     * The reason this is done is using setVisibility.GONE did not always work correctly, however
     * this solution always works right.
     */
    private void buildArrays() {
        String headerIcon = mCursor.getString(mCursor
                .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FANDOM_ICON));
        String fandomDescription = mCursor.getString(mCursor
                .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FANDOM_DESCRIPTION));
        arrayOfDetails.add(new LocationInfo(headerIcon, "Location Details:", fandomDescription));

        String sceneDescription = mCursor.getString(mCursor
                .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_SCENE_DESCRIPTION));
        arrayOfDetails.add(new LocationInfo(headerIcon, "Scene Description:", sceneDescription));

        //Possible that Location will not have Trivia info
        String fandomTrivia = mCursor.getString(mCursor
                .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FANDOM_TRIVIA));
        if (!TextUtils.isEmpty(fandomTrivia)) {
            arrayOfDetails.add(new LocationInfo(headerIcon, "Trivia:", fandomTrivia));
        }
    }

    private void ButtonsAndLinksViewAdapter() {
        String linkOne = mCursor.getString(mCursor
                .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FANDOM_LINK_ONE));
        ((NestedScrollView) rootView.findViewById(R.id.locations_scroll_view)).setFillViewport(true);
        TextView linkOneTV = ((TextView) rootView.findViewById(R.id.fandom_link_one));
        linkOneTV.setMovementMethod(LinkMovementMethod.getInstance());
        linkOneTV.setText(Html.fromHtml(linkOne));

        //Possible second link
        String fandomLinkTwoString = mCursor.getString(mCursor
                .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FANDOM_LINK_TWO));
        TextView fandomLinkTwo = (TextView) rootView.findViewById(R.id.fandom_link_two);
        if (!TextUtils.isEmpty(fandomLinkTwoString)) {
            fandomLinkTwo.setMovementMethod(LinkMovementMethod.getInstance());
            fandomLinkTwo.setText(Html.fromHtml(fandomLinkTwoString));
        } else {
            fandomLinkTwo.setVisibility(View.GONE);
        }

        //Possible third link
        String fandomLinkThreeString = mCursor.getString(mCursor
                .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FANDOM_LINK_THREE));
        TextView fandomLinkThree = (TextView) rootView.findViewById(R.id.fandom_link_three);
        if (!TextUtils.isEmpty(fandomLinkThreeString)) {
            fandomLinkThree.setMovementMethod(LinkMovementMethod.getInstance());
            fandomLinkThree.setText(Html.fromHtml(fandomLinkThreeString));
        } else {
            fandomLinkThree.setVisibility(View.GONE);
        }

        rootView.findViewById(R.id.fandom_image_button).setOnClickListener(this);
        rootView.findViewById(R.id.fandom_video_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fandom_image_button:
                Intent imageGalleryIntent = new Intent(getActivity(), ImageGalleryActivity.class);
                int adapterPosition = mCursor.getInt(mCursor
                        .getColumnIndexOrThrow(FandomWorldTableEntries._ID));
                String fandomIdentifier = "fandom";
                imageGalleryIntent.putExtra(MainActivity.ARG_ITEM_ID, adapterPosition);
                imageGalleryIntent.putExtra(FandomDetailActivity.ARG_IDENTIFIER, fandomIdentifier);
                startActivity(imageGalleryIntent);
                break;
            case R.id.fandom_video_button:
                String youtubePath = mCursor.getString(mCursor
                        .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FANDOM_SCENE_VIDEO));
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/" + youtubePath)));
                break;
        }
    }

    private void createRecyclerView() {
        FandomDetailViewAdapter fandomDetailViewAdapter = new FandomDetailViewAdapter();
        RecyclerView fandomDetailRecyclerView = (RecyclerView) rootView.findViewById(R.id.fandom_recycler_view);
        fandomDetailRecyclerView.setNestedScrollingEnabled(false);
        fandomDetailRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        fandomDetailRecyclerView.setHasFixedSize(true);
        fandomDetailRecyclerView.setAdapter(fandomDetailViewAdapter);
    }

    class FandomDetailViewAdapter extends RecyclerView.Adapter<FandomDetailViewAdapter.FandomDetailViewHolder> {
        @Override
        public FandomDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_fandom_detail_item, parent, false);
            view.setFocusable(true);
            return new FandomDetailViewHolder(view);
        }

        @Override
        public void onBindViewHolder(FandomDetailViewHolder holder, int position) {
            final LocationInfo location = arrayOfDetails.get(position);

            int fandomIcon = mContext.getResources().getIdentifier(location.getIcon(), "drawable", mContext.getPackageName());
            Picasso.with(mContext).load(fandomIcon).into(holder.detailIcon);
            holder.detailHeader.setText(location.getHeader());
            holder.detailsText.setText(location.getDescription());
        }

        @Override
        public int getItemCount() {
            return arrayOfDetails.size();
        }

        class FandomDetailViewHolder extends RecyclerView.ViewHolder {
            final ImageView detailIcon;
            final TextView detailHeader;
            final TextView detailsText;

            FandomDetailViewHolder(View itemView) {
                super(itemView);

                detailIcon = (ImageView) itemView.findViewById(R.id.fandom_recycler_icon);
                detailHeader = (TextView) itemView.findViewById(R.id.fandom_recycler_header);
                detailsText = (TextView) itemView.findViewById(R.id.fandom_recycler_detail);
            }
        }
    }
}
