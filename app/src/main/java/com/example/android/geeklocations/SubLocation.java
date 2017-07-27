package com.example.android.geeklocations;

import android.os.Parcel;
import android.os.Parcelable;

class SubLocation implements Parcelable {
    private String subLocationName;
    private final String parentCategory;
    private String fandomLocationName;
    private int iconResource;

    private int cursorLocation;

    SubLocation(String parentCategory, int cursorLocation) {
        this.parentCategory = parentCategory;
        this.cursorLocation = cursorLocation;
    }

    SubLocation(String subLocationName, String parentCategory, int iconResource, int cursorLocation) {
        this.subLocationName = subLocationName;
        this.parentCategory = parentCategory;
        this.iconResource = iconResource;
        this.cursorLocation = cursorLocation;
    }

    private SubLocation(Parcel in) {
        subLocationName = in.readString();
        parentCategory = in.readString();
        fandomLocationName = in.readString();
        iconResource = in.readInt();
    }

    int getCursorLocation() {
        return cursorLocation;
    }

    String getSubLocationName() {
        return subLocationName;
    }

    int getIconResource() {
        return iconResource;
    }

    String getParentCategory() {
        return parentCategory;
    }

    public static final Creator<SubLocation> CREATOR = new Creator<SubLocation>() {
        @Override
        public SubLocation createFromParcel(Parcel in) {
            return new SubLocation(in);
        }

        @Override
        public SubLocation[] newArray(int size) {
            return new SubLocation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(parentCategory);
        dest.writeString(fandomLocationName);
        dest.writeInt(iconResource);
    }
}
