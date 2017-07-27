package com.example.android.geeklocations.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.example.android.geeklocations.data.FandomContract.*;

public class FandomsContentProvider extends ContentProvider {

    private static final int MAIN_LIST = 100;
    private static final int FANDOMS_LIST_WITH_FAVORITE = 201;
    private static final int FANDOMS_LIST_WITH_ID = 202;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AUTHORITY, MainListEntry.MAIN_TABLE_NAME, MAIN_LIST);
        uriMatcher.addURI(AUTHORITY, FandomWorldTableEntries.FANDOM_TABLE_NAME + "/true", FANDOMS_LIST_WITH_FAVORITE);
        uriMatcher.addURI(AUTHORITY, FandomWorldTableEntries.FANDOM_TABLE_NAME + "/false" + "/#", FANDOMS_LIST_WITH_ID);
        return uriMatcher;
    }

    private FandomDb mFandomDb;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mFandomDb = new FandomDb(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = mFandomDb.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor returnCursor;

        switch (match) {
            case MAIN_LIST:
                returnCursor = db.query(MainListEntry.MAIN_TABLE_NAME,
                        null, null, null, null, null, null);
                break;
            case FANDOMS_LIST_WITH_ID:
                String fandomId = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{fandomId};
                returnCursor = db.query(FandomWorldTableEntries.FANDOM_TABLE_NAME,
                        projection,
                        selection,
                        selectionArguments,
                        null, null, null);
                break;
            case FANDOMS_LIST_WITH_FAVORITE:
                selectionArguments = new String[]{String.valueOf(1)};
                returnCursor = db.query(FandomWorldTableEntries.FANDOM_TABLE_NAME,
                        projection,
                        selection,
                        selectionArguments,
                        null, null, null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = mFandomDb.getWritableDatabase();
        String selectionID = FandomWorldTableEntries.COLUMN_FANDOM_LOCATION_NAME + "=?";
        String[] selectionArguments = new String[]{values != null ? values.getAsString(FandomWorldTableEntries.COLUMN_FANDOM_LOCATION_NAME) : null};
        int rowsUpdated = database.update(FandomWorldTableEntries.FANDOM_TABLE_NAME, values, selectionID, selectionArguments);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }
}
