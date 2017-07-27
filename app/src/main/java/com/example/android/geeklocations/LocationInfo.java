package com.example.android.geeklocations;

class LocationInfo {
    private final String icon;
    private final String header;
    private String locationName;
    private String identifier;
    private String longitude;
    private String latitude;
    private final String description;
    private String linkOne;
    private String linkTwo;
    private String linkThree;

    LocationInfo(String icon, String header, String locationName, String identifier,
                 String longitude, String latitude, String description, String linkOne,
                 String linkTwo, String linkThree) {
        this.icon = icon;
        this.header = header;
        this.locationName = locationName;
        this.identifier = identifier;
        this.longitude = longitude;
        this.latitude = latitude;
        this.description = description;
        this.linkOne = linkOne;
        this.linkTwo = linkTwo;
        this.linkThree = linkThree;
    }

    LocationInfo(String icon, String header, String description) {
        this.icon = icon;
        this.header = header;
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    String getHeader() {
        return header;
    }

    String getLocationName() {
        return locationName;
    }

    public String getIdentifier() {
        return identifier;
    }

    String getLongitude() {
        return longitude;
    }

    String getLatitude() {
        return latitude;
    }

    String getDescription() {
        return description;
    }

    String getLinkOne() {
        return linkOne;
    }

    String getLinkTwo() {
        return linkTwo;
    }

    String getLinkThree() {
        return linkThree;
    }
}
