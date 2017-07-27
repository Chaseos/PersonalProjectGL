package com.example.android.geeklocations;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.Space;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.ArrayList;
import java.util.List;

import static android.view.animation.Animation.RELATIVE_TO_SELF;
import static com.example.android.geeklocations.data.FandomContract.FandomWorldTableEntries;

public class RealLocationsListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private ArrayList<SubLocation> subLocationsForParentLocations;
    private static final int ID_REAL_LOCATIONS_LOADER = 991;
    private ArrayList<ParentLocation> parentLocations;
    private RecyclerView mLocationsRecyclerView;
    private ArrayList<SubLocation> subLocations;
    private ArrayList<Integer> mLocationImages;
    private LocationsAdapter locationsAdapter;
    private ArrayList<String> mLocationNames;
    private boolean itemChanged;
    private int clickedPosition;
    private Context mContext;
    private Cursor mCursor;

    public RealLocationsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getContext();
        mLocationNames = new ArrayList<>();
        mLocationImages = new ArrayList<>();
        subLocations = new ArrayList<>();
        subLocationsForParentLocations = new ArrayList<>();
        parentLocations = new ArrayList<>();

        getLoaderManager().initLoader(ID_REAL_LOCATIONS_LOADER, null, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_locations_list, container, false);
        rootview.findViewById(R.id.empty_list_view).setVisibility(View.INVISIBLE);
        mLocationsRecyclerView = (RecyclerView) rootview.findViewById(R.id.locations_list_recyclerview);
        return rootview;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = new String[]{
                FandomWorldTableEntries._ID,
                FandomWorldTableEntries.COLUMN_FANDOM_ID,
                FandomWorldTableEntries.COLUMN_FANDOM_LOCATION_NAME,
                FandomWorldTableEntries.COLUMN_FANDOM_FONT,
                FandomWorldTableEntries.COLUMN_PARENT_FONT_SIZE,
                FandomWorldTableEntries.COLUMN_LOCATIONS_CHILD_FONT_SIZE,
                FandomWorldTableEntries.COLUMN_FONT_SPACING,
                FandomWorldTableEntries.COLUMN_VIEW_SPACING,
                FandomWorldTableEntries.COLUMN_FANDOM_ICON,
                FandomWorldTableEntries.COLUMN_FANDOM_IMAGE_ONE,
                FandomWorldTableEntries.COLUMN_ADAPTATION_LOCATION_NAME,
                FandomWorldTableEntries.COLUMN_ADAPTATION_LOCATION_CITY,
                FandomWorldTableEntries.COLUMN_ADAPTATION_CITY_IMAGE,
                FandomWorldTableEntries.COLUMN_FILMING_LOCATION_NAME,
                FandomWorldTableEntries.COLUMN_FILMING_LOCATION_CITY,
                FandomWorldTableEntries.COLUMN_FILMING_CITY_IMAGE,
                FandomWorldTableEntries.COLUMN_INSPIRATION_LOCATION_NAME,
                FandomWorldTableEntries.COLUMN_INSPIRATION_LOCATION_CITY,
                FandomWorldTableEntries.COLUMN_INSPIRATION_CITY_IMAGE};

        String selection = FandomWorldTableEntries.COLUMN_FANDOM_ID + " = ?";
        int position = getArguments().getInt(MainActivity.FANDOM_NAME_ID);
        Uri locationsUri = FandomWorldTableEntries.buildFandomUriInfoWithId(position);
        return new CursorLoader(mContext, locationsUri, projection, selection, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor = data;
        mCursor.moveToFirst();

        if (itemChanged) {
            locationsAdapter.notifyItemChanged(clickedPosition);
        }

        if (locationsAdapter == null) {
            do {
                putAdaptationLocation();
                putFilmingLocation();
                putInspirationLocation();
            } while (mCursor.moveToNext());

            fillParentLocationsList();
            createRecyclerView();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private void putAdaptationLocation() {
        String adaptationLocationCity = mCursor.getString(mCursor.getColumnIndexOrThrow
                (FandomWorldTableEntries.COLUMN_ADAPTATION_LOCATION_CITY));

        if (!TextUtils.isEmpty(adaptationLocationCity) && !mLocationNames.contains(adaptationLocationCity)) {
            mLocationNames.add(adaptationLocationCity);

            String adaptationCityImage = mCursor.getString(mCursor.getColumnIndexOrThrow(
                    FandomWorldTableEntries.COLUMN_ADAPTATION_CITY_IMAGE));
            int adaptationImageInt = mContext.getResources()
                    .getIdentifier(adaptationCityImage, "drawable", mContext.getPackageName());
            mLocationImages.add(adaptationImageInt);
        }

        if (!TextUtils.isEmpty(adaptationLocationCity)) {
            String adaptationLocationName = mCursor.getString(mCursor
                    .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_ADAPTATION_LOCATION_NAME));
            int adaptationIcon = R.drawable.adaptation_icon;
            subLocations.add(new SubLocation(adaptationLocationName, adaptationLocationCity,
                    adaptationIcon, mCursor.getPosition()));
        }
    }

    private void putFilmingLocation() {
        String filmingLocationCity = mCursor.getString(mCursor.getColumnIndexOrThrow(
                FandomWorldTableEntries.COLUMN_FILMING_LOCATION_CITY));

        if (!TextUtils.isEmpty(filmingLocationCity) && !mLocationNames.contains(filmingLocationCity)) {
            mLocationNames.add(filmingLocationCity);

            String filmingCityImage = mCursor.getString(mCursor.getColumnIndexOrThrow(
                    FandomWorldTableEntries.COLUMN_FILMING_CITY_IMAGE));
            int filmingImageInt = mContext.getResources()
                    .getIdentifier(filmingCityImage, "drawable", mContext.getPackageName());
            mLocationImages.add(filmingImageInt);
        }

        if (!TextUtils.isEmpty(filmingLocationCity)) {
            String filmingLocationName = mCursor.getString(mCursor
                    .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FILMING_LOCATION_NAME));
            int filmingIcon = R.drawable.film_icon;
            subLocations.add(new SubLocation(filmingLocationName, filmingLocationCity,
                    filmingIcon, mCursor.getPosition()));
        }
    }

    private void putInspirationLocation() {
        String inspirationLocationCity = mCursor.getString(mCursor.getColumnIndexOrThrow(
                FandomWorldTableEntries.COLUMN_INSPIRATION_LOCATION_CITY));
        if (!TextUtils.isEmpty(inspirationLocationCity) && !mLocationNames.contains(inspirationLocationCity)) {
            mLocationNames.add(inspirationLocationCity);

            String inspirationCityImage = mCursor.getString(mCursor.getColumnIndexOrThrow(
                    FandomWorldTableEntries.COLUMN_INSPIRATION_CITY_IMAGE));
            int inspirationImageInt = mContext.getResources()
                    .getIdentifier(inspirationCityImage, "drawable", mContext.getPackageName());
            mLocationImages.add(inspirationImageInt);
        }

        if (!TextUtils.isEmpty(inspirationLocationCity)) {
            String inspirationLocationName = mCursor.getString(mCursor
                    .getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_INSPIRATION_LOCATION_NAME));
            int inspirationIcon = R.drawable.inspiration_icon;
            subLocations.add(new SubLocation(inspirationLocationName, inspirationLocationCity,
                    inspirationIcon, mCursor.getPosition()));
        }
    }

    private void fillParentLocationsList() {
        for (int i = 0; i < mLocationNames.size(); i++) {
            String parentLocation = mLocationNames.get(i);
            int cursorLocation = 0;
            int parentLocationImageRes = mLocationImages.get(i);
            subLocationsForParentLocations = new ArrayList<>();
            SubLocation subLocation;
            for (int j = 0; j < subLocations.size(); j++) {
                subLocation = subLocations.get(j);
                if (parentLocation.equals(subLocation.getParentCategory())) {
                    subLocationsForParentLocations.add(new SubLocation
                            (subLocation.getSubLocationName(), subLocation.getParentCategory(),
                                    subLocation.getIconResource(), subLocation.getCursorLocation()));
                    cursorLocation = subLocation.getCursorLocation();
                }
            }
            parentLocations.add(new ParentLocation(parentLocation,
                    parentLocationImageRes, cursorLocation, subLocationsForParentLocations));
        }
    }

    private void createRecyclerView() {
        locationsAdapter = new LocationsAdapter(parentLocations);
        mLocationsRecyclerView.setLayoutManager(new
                LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mLocationsRecyclerView.setHasFixedSize(true);
        mLocationsRecyclerView.setAdapter(locationsAdapter);
    }

    private class LocationsAdapter extends ExpandableRecyclerViewAdapter<ParentLocationViewHolder, SubLocationViewHolder> {

        LocationsAdapter(List<? extends ExpandableGroup> groups) {
            super(groups);
        }

        @Override
        public ParentLocationViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_location_list_item, parent, false);
            return new ParentLocationViewHolder(view);
        }

        @Override
        public SubLocationViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.real_sub_location_item, parent, false);
            return new SubLocationViewHolder(view);
        }

        @Override
        public void onBindChildViewHolder(SubLocationViewHolder holder, int flatPosition,
                                          ExpandableGroup group, int childIndex) {
            final SubLocation subLocation = ((ParentLocation) group).getItems().get(childIndex);
            holder.onBind(subLocation);
        }

        @Override
        public void onBindGroupViewHolder(ParentLocationViewHolder holder, int flatPosition, ExpandableGroup group) {
            holder.setViews(group);
        }
    }

    class ParentLocationViewHolder extends GroupViewHolder {

        private final TextView parentLocationName;
        private final ImageView parentLocationBackground;
        private final ImageView arrow;

        ParentLocationViewHolder(View itemView) {
            super(itemView);
            parentLocationName = (TextView) itemView.findViewById(R.id.location_list_name_text);
            parentLocationBackground = (ImageView) itemView.findViewById(R.id.location_list_image);
            arrow = (ImageView) itemView.findViewById(R.id.dropdown_arrow);
        }

        void setViews(ExpandableGroup location) {
            mCursor.moveToPosition(((ParentLocation) location).getCursorLocation());

            String font = mCursor.getString(mCursor.getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FANDOM_FONT));
            Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/" + font + ".TTF");
            int fontSize = mCursor.getInt(mCursor.getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_PARENT_FONT_SIZE));
            float fontSpacing = mCursor.getFloat(mCursor.getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FONT_SPACING));

            parentLocationName.setText(location.getTitle());
            parentLocationName.setTypeface(customFont);
            parentLocationName.setTextSize(fontSize);
            parentLocationName.setLineSpacing(0, fontSpacing);
            Picasso.with(mContext).load(((ParentLocation) location).getImageResource()).into(parentLocationBackground);
        }

        @Override
        public void expand() {
            animateExpand();
        }

        @Override
        public void collapse() {
            animateCollapse();
        }

        private void animateExpand() {
            RotateAnimation rotate =
                    new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            arrow.setAnimation(rotate);
        }

        private void animateCollapse() {
            RotateAnimation rotate =
                    new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            arrow.setAnimation(rotate);
        }
    }

    class SubLocationViewHolder extends ChildViewHolder implements View.OnClickListener {
        private int locationID;
        private final TextView subLocationName;
        private final TextView subLocationFandomLocation;
        private final ImageView subLocationBackground;
        private final ImageView subLocationIcon;
        private final ImageView subLocationFandomIcon;
        private final Space space;

        SubLocationViewHolder(View itemView) {
            super(itemView);

            subLocationName = (TextView) itemView.findViewById(R.id.sublocation_name);
            subLocationFandomLocation = (TextView) itemView.findViewById(R.id.sublocation_fandom_location);
            subLocationBackground = (ImageView) itemView.findViewById(R.id.sublocation_background);
            subLocationIcon = (ImageView) itemView.findViewById(R.id.sublocation_icon);
            subLocationFandomIcon = (ImageView) itemView.findViewById(R.id.sublocation_fandom_icon);
            space = (Space) itemView.findViewById(R.id.marginSpace);
            itemView.setOnClickListener(this);
        }

        void onBind(SubLocation subLocation) {
            mCursor.moveToPosition(subLocation.getCursorLocation());

            String fandomLocationName = mCursor.getString(mCursor.getColumnIndexOrThrow(
                    FandomWorldTableEntries.COLUMN_FANDOM_LOCATION_NAME));
            String font = mCursor.getString(mCursor.getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FANDOM_FONT));
            Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/" + font + ".TTF");
            int fontSize = mCursor.getInt(mCursor.getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_LOCATIONS_CHILD_FONT_SIZE));
            float fontSpacing = mCursor.getFloat(mCursor.getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FONT_SPACING));
            int viewSpacing = mCursor.getInt(mCursor.getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_VIEW_SPACING));
            String fandomBackgroundImage = mCursor.getString(mCursor.getColumnIndexOrThrow(
                    FandomWorldTableEntries.COLUMN_FANDOM_IMAGE_ONE));
            int fandomBackgroundResource = mContext.getResources()
                    .getIdentifier(fandomBackgroundImage, "drawable", mContext.getPackageName());
            locationID = mCursor.getInt(mCursor.getColumnIndexOrThrow(FandomWorldTableEntries._ID));

            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) space.getLayoutParams();
            layoutParams.setMargins(0, viewSpacing, 0, 0);
            space.setLayoutParams(layoutParams);
            subLocationName.setText(subLocation.getSubLocationName());
            subLocationName.setTypeface(customFont);
            subLocationName.setTextSize(fontSize);
            subLocationName.setLineSpacing(0, fontSpacing);
            subLocationFandomLocation.setText(fandomLocationName);
            subLocationFandomLocation.setTypeface(customFont);
            subLocationFandomLocation.setTextSize(fontSize);
            subLocationFandomLocation.setLineSpacing(0, fontSpacing);
            Picasso.with(mContext).load(fandomBackgroundResource).into(subLocationBackground);
            Picasso.with(mContext).load(subLocation.getIconResource()).into(subLocationIcon);

            subLocationFandomIcon.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View v) {
            clickedPosition = getAdapterPosition();
            Intent intent = new Intent(mContext, FandomDetailActivity.class);
            intent.putExtra(MainActivity.FANDOM_NAME_ID, getArguments().getInt(MainActivity.FANDOM_NAME_ID));
            intent.putExtra(MainActivity.ARG_ITEM_ID, locationID);
            startActivityForResult(intent, FandomListActivity.FAVORITE_CHANGED);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FandomListActivity.FAVORITE_CHANGED) {
            if (resultCode == Activity.RESULT_OK) {
                itemChanged = true;
                getLoaderManager().restartLoader(ID_REAL_LOCATIONS_LOADER, null, this);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
