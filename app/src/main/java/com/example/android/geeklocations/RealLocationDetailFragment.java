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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.android.geeklocations.data.FandomContract.FandomWorldTableEntries;

public class RealLocationDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ID_LOCATION_FRAGMENT_LOADER = 383;
    private ArrayList<LocationInfo> arrayOfLocations;
    private Context mContext;
    private Cursor mCursor;
    private View rootView;

    public RealLocationDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        arrayOfLocations = new ArrayList<>();
        getLoaderManager().initLoader(ID_LOCATION_FRAGMENT_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_location_detail, container, false);
        ((NestedScrollView) rootView.findViewById(R.id.locations_detail_scroll_view)).setFillViewport(true);
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = FandomWorldTableEntries._ID + " = ?";
        int position = getArguments().getInt(MainActivity.ARG_ITEM_ID);
        Uri filmingUri = FandomWorldTableEntries.buildFandomUriInfoWithId(position);
        return new CursorLoader(mContext, filmingUri, null, selection, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        mCursor = data;
        buildArrays();
        setupRecyclerView();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    /**
     * Grabs info from cursor and places in Location Info Objects, then retrieves these objects
     * later to be put into views. Some locations don't have certain places so they're not always
     * all filled.
     */
    private void buildArrays() {
        String adaptationLocationName = mCursor.getString(mCursor
                .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_ADAPTATION_LOCATION_NAME));
        if (!TextUtils.isEmpty(adaptationLocationName)) {
            addAdaptationLocation();
        }

        String filmingLocationName = mCursor.getString(mCursor
                .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FILMING_LOCATION_NAME));
        if (!TextUtils.isEmpty(filmingLocationName)) {
            addFilmingLocation();
        }

        String inspirationLocationName = mCursor.getString(mCursor
                .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_INSPIRATION_LOCATION_NAME));
        if (!TextUtils.isEmpty(inspirationLocationName)) {
            addInspirationLocation();
        }
    }

    private void addAdaptationLocation() {
        String adaptationLocationName = mCursor.getString(mCursor
                .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_ADAPTATION_LOCATION_NAME));
        String adaptationLongitude = mCursor.getString(mCursor
                .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_ADAPTATION_LONGITUDE));
        String adaptationLatitude = mCursor.getString(mCursor
                .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_ADAPTATION_LATITUDE));
        String adaptationDescription = mCursor.getString(mCursor
                .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_ADAPTATION_DESCRIPTION));
        String adaptationLinkOne = mCursor.getString(mCursor
                .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_ADAPTATION_LINK_ONE));
        String adaptationLinkTwo = mCursor.getString(mCursor
                .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_ADAPTATION_LINK_TWO));
        String adaptationLinkThree = mCursor.getString(mCursor
                .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_ADAPTATION_LINK_THREE));

        arrayOfLocations.add(new LocationInfo("adaptation_icon", "Adaptation Location:", adaptationLocationName,
                "adaptation", adaptationLongitude, adaptationLatitude, adaptationDescription,
                adaptationLinkOne, adaptationLinkTwo, adaptationLinkThree));
    }

    private void addFilmingLocation() {
        String filmingLocationName = mCursor.getString(mCursor
                .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FILMING_LOCATION_NAME));
        String filmingLongitude = mCursor.getString(mCursor
                .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FILMING_LONGITUDE));
        String filmingLatitude = mCursor.getString(mCursor
                .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FILMING_LATITUDE));
        String filmingDescription = mCursor.getString(mCursor
                .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FILMING_DESCRIPTION));
        String filmingLinkOne = mCursor.getString(mCursor
                .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FILMING_LINK_ONE));
        String filmingLinkTwo = mCursor.getString(mCursor
                .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FILMING_LINK_TWO));
        String filmingLinkThree = mCursor.getString(mCursor
                .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FILMING_LINK_THREE));

        arrayOfLocations.add(new LocationInfo("film_icon", "Filming Location:", filmingLocationName,
                "filming", filmingLongitude, filmingLatitude, filmingDescription,
                filmingLinkOne, filmingLinkTwo, filmingLinkThree));
    }

    private void addInspirationLocation() {
        String inspirationLocationName = mCursor.getString(mCursor
                .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_INSPIRATION_LOCATION_NAME));
        String inspirationLongitude = mCursor.getString(mCursor
                .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_INSPIRATION_LONGITUDE));
        String inspirationLatitude = mCursor.getString(mCursor
                .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_INSPIRATION_LATITUDE));
        String inspirationDescription = mCursor.getString(mCursor
                .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_INSPIRATION_DESCRIPTION));
        String inspirationLinkOne = mCursor.getString(mCursor
                .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_INSPIRATION_LINK_ONE));
        String inspirationLinkTwo = mCursor.getString(mCursor
                .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_INSPIRATION_LINK_TWO));
        String inspirationLinkThree = mCursor.getString(mCursor
                .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_INSPIRATION_LINK_THREE));

        arrayOfLocations.add(new LocationInfo("inspiration_icon", "Inspiration Location:", inspirationLocationName,
                "inspiration", inspirationLongitude, inspirationLatitude, inspirationDescription,
                inspirationLinkOne, inspirationLinkTwo, inspirationLinkThree));
    }

    private void setupRecyclerView() {
        LocationDetailViewAdapter locationDetailViewAdapter = new LocationDetailViewAdapter();
        RecyclerView locationDetailRecyclerView = (RecyclerView) rootView.findViewById(R.id.location_detail_recyclerview);
        locationDetailRecyclerView.setNestedScrollingEnabled(false);
        locationDetailRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        locationDetailRecyclerView.setHasFixedSize(true);
        locationDetailRecyclerView.setAdapter(locationDetailViewAdapter);
    }

    class LocationDetailViewAdapter extends RecyclerView.Adapter<LocationDetailViewAdapter.LocationDetailViewHolder> {

        @Override
        public LocationDetailViewAdapter.LocationDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_location_detail_item, parent, false);
            view.setFocusable(true);
            return new LocationDetailViewHolder(view);
        }

        @Override
        public void onBindViewHolder(LocationDetailViewAdapter.LocationDetailViewHolder holder, int position) {
            final LocationInfo location = arrayOfLocations.get(position);

            int locationIcon = mContext.getResources().getIdentifier(location.getIcon(), "drawable", mContext.getPackageName());
            Picasso.with(mContext).load(locationIcon).into(holder.locationIcon);
            holder.locationHeader.setText(location.getHeader());
            holder.locationName.setText(location.getLocationName());
            holder.locationImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent imageGalleryIntent = new Intent(getActivity(), ImageGalleryActivity.class);
                    String filmingIdentifier = location.getIdentifier();
                    int adapterPosition = mCursor.getInt(mCursor.getColumnIndexOrThrow(FandomWorldTableEntries._ID));
                    imageGalleryIntent.putExtra(MainActivity.ARG_ITEM_ID, adapterPosition);
                    imageGalleryIntent.putExtra(FandomDetailActivity.ARG_IDENTIFIER, filmingIdentifier);
                    startActivity(imageGalleryIntent);
                }
            });
            holder.locationMapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String filmingLocationName = location.getLocationName();
                    String longitude = location.getLongitude();
                    String latitude = location.getLatitude();
                    Uri mapUri = Uri.parse("geo:0,0?q=" + longitude + "," + latitude + "(" + filmingLocationName + ")");
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW);
                    mapIntent.setData(mapUri);
                    startActivity(mapIntent);
                }
            });
            holder.location_description.setText(location.getDescription());
            holder.locationMoreInfoHeader.setText(R.string.more_info);
            holder.locationLinkOne.setMovementMethod(LinkMovementMethod.getInstance());
            holder.locationLinkOne.setText(Html.fromHtml(location.getLinkOne()));

            if (!TextUtils.isEmpty(location.getLinkTwo())) {
                holder.locationLinkTwo.setMovementMethod(LinkMovementMethod.getInstance());
                holder.locationLinkTwo.setText(Html.fromHtml(location.getLinkTwo()));
            } else {
                holder.locationLinkTwo.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(location.getLinkThree())) {
                holder.locationLinkThree.setMovementMethod(LinkMovementMethod.getInstance());
                holder.locationLinkThree.setText(Html.fromHtml(location.getLinkThree()));
            } else {
                holder.locationLinkThree.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return arrayOfLocations.size();
        }

        class LocationDetailViewHolder extends RecyclerView.ViewHolder {
            final ImageView locationIcon;
            final TextView locationHeader;
            final TextView locationName;
            final Button locationImageButton;
            final Button locationMapButton;
            final TextView location_description;
            final TextView locationMoreInfoHeader;
            final TextView locationLinkOne;
            final TextView locationLinkTwo;
            final TextView locationLinkThree;

            LocationDetailViewHolder(View itemView) {
                super(itemView);

                locationIcon = (ImageView) itemView.findViewById(R.id.location_type_icon);
                locationHeader = (TextView) itemView.findViewById(R.id.location_header);
                locationName = (TextView) itemView.findViewById(R.id.location_name);
                locationImageButton = (Button) itemView.findViewById(R.id.location_image_button);
                locationMapButton = (Button) itemView.findViewById(R.id.location_map_button);
                location_description = (TextView) itemView.findViewById(R.id.location_description);
                locationMoreInfoHeader = (TextView) itemView.findViewById(R.id.location_more_info_header);
                locationLinkOne = (TextView) itemView.findViewById(R.id.location_link_one);
                locationLinkTwo = (TextView) itemView.findViewById(R.id.location_link_two);
                locationLinkThree = (TextView) itemView.findViewById(R.id.location_link_three);
            }
        }
    }
}
