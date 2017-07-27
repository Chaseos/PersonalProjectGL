package com.example.android.geeklocations;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class FavoritesFandomLocationsListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ID_FANDOM_LOCATIONS_LOADER = 476;
    private ArrayList<ParentLocation> parentLocations;
    private ArrayList<String> parentCategoryNames;
    private ArrayList<SubLocation> subLocations;
    private RecyclerView mFandomRecyclerView;
    private TextView emptyTextView;
    private Context mContext;
    private Cursor mCursor;

    public FavoritesFandomLocationsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        getLoaderManager().initLoader(ID_FANDOM_LOCATIONS_LOADER, null, this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mCursor != null) {
            getLoaderManager().restartLoader(ID_FANDOM_LOCATIONS_LOADER, null, this);
        } else {
            getLoaderManager().initLoader(ID_FANDOM_LOCATIONS_LOADER, null, this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_fandom_locations_list, container, false);
        mFandomRecyclerView = (RecyclerView) rootview.findViewById(R.id.fandoms_list_recyclerview);
        emptyTextView = (TextView) rootview.findViewById(R.id.empty_list_view);
        return rootview;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = new String[]{
                FandomWorldTableEntries._ID,
                FandomWorldTableEntries.COLUMN_FANDOM_ID,
                FandomWorldTableEntries.COLUMN_FANDOM_NAME,
                FandomWorldTableEntries.COLUMN_FANDOM_FONT,
                FandomWorldTableEntries.COLUMN_PARENT_FONT_SIZE,
                FandomWorldTableEntries.COLUMN_FANDOM_CHILD_FONT_SIZE,
                FandomWorldTableEntries.COLUMN_FONT_SPACING,
                FandomWorldTableEntries.COLUMN_FANDOM_LOCATION_NAME,
                FandomWorldTableEntries.COLUMN_FANDOM_SUBCATEGORY,
                FandomWorldTableEntries.COLUMN_FANDOM_SUBCATEGORY_IMAGE,
                FandomWorldTableEntries.COLUMN_FANDOM_IMAGE_ONE,
                FandomWorldTableEntries.COLUMN_FANDOM_FAVORITE};
        String selection = FandomWorldTableEntries.COLUMN_FANDOM_FAVORITE + " = ?";
        Uri fandomUri = FandomWorldTableEntries.buildFandomUriInfoWithFavorites();
        return new CursorLoader(mContext, fandomUri, projection, selection, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor = data;
        mCursor.moveToFirst();

        if (mCursor.getCount() == 0 || !mCursor.moveToFirst()) {
            mFandomRecyclerView.setVisibility(View.INVISIBLE);
            emptyTextView.setVisibility(View.VISIBLE);
            return;
        } else {
            mFandomRecyclerView.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.INVISIBLE);
        }

        createSubLocationsList();
        createParentLocationsList();

        setupRecyclerView();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    /**
     * Expandable RecyclerView requires I pass in lists of objects and this was the simplest
     * Way I found possible, to simply put in the position of the cursor. The parentCategoryNames
     * and parentCategoryImages are used to ensure I only have 1 of each and used to build
     * the parent object list later. Arraylists are called new here because I need them to be new
     * each time I call the Loader
     **/
    private void createSubLocationsList() {
        subLocations = new ArrayList<>();
        parentCategoryNames = new ArrayList<>();

        do {
            String parentCategoryName = mCursor.getString(mCursor.getColumnIndexOrThrow(
                    FandomWorldTableEntries.COLUMN_FANDOM_SUBCATEGORY));
            subLocations.add(new SubLocation(parentCategoryName, mCursor.getPosition()));

            if (!parentCategoryNames.contains(parentCategoryName)) {
                parentCategoryNames.add(parentCategoryName);
            }
        } while (mCursor.moveToNext());
    }

    /**
     * Searches through the lists and takes out each sublocation object that corresponds to each
     * parent category location. Then puts these into a parent Object list which is used
     * for the Expandable RecyclerView.
     */
    private void createParentLocationsList() {
        parentLocations = new ArrayList<>();
        for (int i = 0; i < parentCategoryNames.size(); i++) {
            String parentCategoryString = parentCategoryNames.get(i);
            int cursorLocation = 0;
            ArrayList<SubLocation> subLocationsForParentLocations = new ArrayList<>();
            SubLocation subLocation;
            for (int j = 0; j < subLocations.size(); j++) {
                subLocation = subLocations.get(j);
                if (parentCategoryString.equals(subLocation.getParentCategory())) {
                    subLocationsForParentLocations.add(new SubLocation(
                            subLocation.getParentCategory(), subLocation.getCursorLocation()));
                    cursorLocation = subLocation.getCursorLocation();
                }
            }
            parentLocations.add(new ParentLocation(parentCategoryString, cursorLocation,
                    subLocationsForParentLocations));
        }
    }

    private void setupRecyclerView() {
        FavoritesFandomsAdapter mFavoritesFandomsAdapter = new FavoritesFandomsAdapter(parentLocations);
        mFandomRecyclerView.setAdapter(mFavoritesFandomsAdapter);
        mFandomRecyclerView.setLayoutManager(new
                LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mFandomRecyclerView.setHasFixedSize(true);
    }

    private class FavoritesFandomsAdapter extends ExpandableRecyclerViewAdapter<ParentViewHolder, SubLocationViewHolder> {

        FavoritesFandomsAdapter(List<? extends ExpandableGroup> groups) {
            super(groups);
        }

        @Override
        public ParentViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_location_list_item, parent, false);
            return new ParentViewHolder(view);
        }

        @Override
        public SubLocationViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.fandom_sub_location_item, parent, false);
            return new SubLocationViewHolder(view);
        }

        @Override
        public void onBindChildViewHolder(SubLocationViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
            final SubLocation subLocation = ((ParentLocation) group).getItems().get(childIndex);
            holder.onBind(subLocation);
        }

        @Override
        public void onBindGroupViewHolder(ParentViewHolder holder, int flatPosition, ExpandableGroup group) {
            holder.setViews(group);
        }
    }

    class ParentViewHolder extends GroupViewHolder {
        private final TextView parentCategoryName;
        private final ImageView parentCategoryBackground;
        private final ImageView arrow;

        ParentViewHolder(View itemView) {
            super(itemView);
            parentCategoryName = (TextView) itemView.findViewById(R.id.location_list_name_text);
            parentCategoryBackground = (ImageView) itemView.findViewById(R.id.location_list_image);
            arrow = (ImageView) itemView.findViewById(R.id.dropdown_arrow);
        }

        void setViews(ExpandableGroup location) {
            mCursor.moveToPosition(((ParentLocation) location).getCursorLocation());

            String backgroundImage = mCursor.getString(mCursor.getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FANDOM_SUBCATEGORY_IMAGE));
            int backgroundImageResource = mContext.getResources()
                    .getIdentifier(backgroundImage, "drawable", mContext.getPackageName());
            String font = mCursor.getString(mCursor.getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FANDOM_FONT));
            Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/" + font + ".TTF");
            int fontSize = mCursor.getInt(mCursor.getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_PARENT_FONT_SIZE));
            float fontSpacing = mCursor.getFloat(mCursor.getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FONT_SPACING));

            parentCategoryName.setText(location.getTitle());
            parentCategoryName.setTypeface(customFont);
            parentCategoryName.setTextSize(fontSize);
            parentCategoryName.setLineSpacing(0, fontSpacing);
            Picasso.with(mContext).load(backgroundImageResource).into(parentCategoryBackground);
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
        private final ImageView subLocationBackground;

        SubLocationViewHolder(View itemView) {
            super(itemView);

            subLocationName = (TextView) itemView.findViewById(R.id.fandom_sublocation_name);
            subLocationBackground = (ImageView) itemView.findViewById(R.id.fandom_sublocation_background);
            itemView.setOnClickListener(this);
        }

        void onBind(SubLocation subLocation) {
            mCursor.moveToPosition(subLocation.getCursorLocation());

            String fandomLocationName = mCursor.getString(mCursor.getColumnIndexOrThrow(
                    FandomWorldTableEntries.COLUMN_FANDOM_LOCATION_NAME));
            String fandomBackgroundImage = mCursor.getString(mCursor.getColumnIndexOrThrow(
                    FandomWorldTableEntries.COLUMN_FANDOM_IMAGE_ONE));
            String font = mCursor.getString(mCursor.getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FANDOM_FONT));
            Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/" + font + ".TTF");
            int fontSize = mCursor.getInt(mCursor.getColumnIndexOrThrow(FandomWorldTableEntries.COLUMN_FANDOM_CHILD_FONT_SIZE));
            int fandomBackgroundResource = mContext.getResources()
                    .getIdentifier(fandomBackgroundImage, "drawable", mContext.getPackageName());
            locationID = mCursor.getInt(mCursor.getColumnIndexOrThrow(FandomWorldTableEntries._ID));

            subLocationName.setText(fandomLocationName);
            subLocationName.setTypeface(customFont);
            subLocationName.setTextSize(fontSize);
            Picasso.with(mContext).load(fandomBackgroundResource).into(subLocationBackground);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, FandomDetailActivity.class);
            intent.putExtra(MainActivity.FANDOM_NAME_ID, 0);
            intent.putExtra(MainActivity.ARG_ITEM_ID, locationID);
            startActivityForResult(intent, FandomListActivity.FAVORITE_CHANGED);
        }
    }
}
