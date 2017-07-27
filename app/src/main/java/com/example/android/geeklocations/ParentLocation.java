package com.example.android.geeklocations;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

class ParentLocation extends ExpandableGroup<SubLocation>{

    private final int cursorLocation;
    private int imageResource;

    ParentLocation(String title, int cursorLoc, List<SubLocation> items) {
        super(title, items);
        cursorLocation = cursorLoc;
    }

    ParentLocation(String title, int imageRes, int cursorLoc, List<SubLocation> items) {
        super(title, items);
        imageResource = imageRes;
        cursorLocation = cursorLoc;
    }

    int getCursorLocation() {
        return cursorLocation;
    }

    public int getImageResource() {
        return imageResource;
    }
}
