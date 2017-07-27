package com.example.android.geeklocations.data;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

class FandomDb extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "locations.db";
    private static final int DATABASE_VERSION = 1;

    FandomDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
