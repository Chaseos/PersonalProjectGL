package com.example.android.geeklocations.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class FandomContract implements BaseColumns {

    static final String AUTHORITY = "com.example.android.geeklocations.data.FandomsContentProvider";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private static final String PATH_MAIN_LIST_FANDOMS = "fandoms";
    private static final String PATH_FANDOM_LOCATIONS_TABLE_NAME = "fandom_locations";

    public static final class MainListEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MAIN_LIST_FANDOMS)
                .build();

        static final String MAIN_TABLE_NAME = "fandoms";
        public static final String MAIN_COLUMN_FANDOMS = "fandom_name";
        public static final String MAIN_COLUMN_BACKGROUNDS = "fandom_background_image";
        public static final String MAIN_COLUMN_FONT = "fandom_font";
        public static final String MAIN_COLUMN_FONT_SIZE = "font_size";
    }

    public static final class FandomWorldTableEntries implements BaseColumns {
        static final String FANDOM_TABLE_NAME = "fandom_locations";
        public static final String COLUMN_FANDOM_ID = "fandom_id";
        public static final String COLUMN_FANDOM_NAME = "fandom_name";
        public static final String COLUMN_FANDOM_DESCRIPTION = "fandom_location_description";
        public static final String COLUMN_FANDOM_TRIVIA = "fandom_trivia";
        public static final String COLUMN_SCENE_DESCRIPTION = "scene_description";
        public static final String COLUMN_FANDOM_FONT = "fandom_font";
        public static final String COLUMN_PARENT_FONT_SIZE = "parent_font_size";
        public static final String COLUMN_LOCATIONS_CHILD_FONT_SIZE = "locations_child_font_size";
        public static final String COLUMN_FANDOM_CHILD_FONT_SIZE = "fandom_child_font_size";
        public static final String COLUMN_FONT_SPACING = "font_spacing";
        public static final String COLUMN_VIEW_SPACING = "view_spacing";
        public static final String COLUMN_FANDOM_SUBCATEGORY = "fandom_subcategory";
        public static final String COLUMN_FANDOM_SUBCATEGORY_IMAGE = "subcategory_image";
        public static final String COLUMN_FANDOM_LOCATION_NAME = "fandom_location_name";
        public static final String COLUMN_FANDOM_IMAGE_ONE = "image_one";
        public static final String COLUMN_FANDOM_IMAGE_TWO = "image_two";
        public static final String COLUMN_FANDOM_IMAGE_THREE = "image_three";
        public static final String COLUMN_FANDOM_IMAGE_FOUR = "image_four";
        public static final String COLUMN_FANDOM_IMAGE_FIVE = "image_five";
        public static final String COLUMN_FANDOM_IMAGE_SIX = "image_six";
        public static final String COLUMN_FANDOM_SCENE_VIDEO = "fandom_scene_video";
        public static final String COLUMN_FANDOM_ICON = "fandom_icon";
        public static final String COLUMN_FANDOM_LINK_ONE = "fandom_link_one";
        public static final String COLUMN_FANDOM_LINK_TWO = "fandom_link_two";
        public static final String COLUMN_FANDOM_LINK_THREE = "fandom_link_three";
        public static final String COLUMN_FANDOM_FAVORITE = "favorite";

        //Recreated Location Column Names
        public static final String COLUMN_ADAPTATION_LOCATION_NAME = "adaptation_location_name";
        public static final String COLUMN_ADAPTATION_DESCRIPTION = "adaptation_description";
        public static final String COLUMN_ADAPTATION_LOCATION_CITY = "adaptation_location_city";
        public static final String COLUMN_ADAPTATION_CITY_IMAGE = "adaptation_city_image";
        public static final String COLUMN_ADAPTATION_IMAGE_ONE = "adaptation_image_one";
        public static final String COLUMN_ADAPTATION_IMAGE_TWO = "adaptation_image_two";
        public static final String COLUMN_ADAPTATION_IMAGE_THREE = "adaptation_image_three";
        public static final String COLUMN_ADAPTATION_IMAGE_FOUR = "adaptation_image_four";
        public static final String COLUMN_ADAPTATION_IMAGE_FIVE = "adaptation_image_five";
        public static final String COLUMN_ADAPTATION_IMAGE_SIX = "adaptation_image_six";
        public static final String COLUMN_ADAPTATION_LONGITUDE = "adaptation_longitude";
        public static final String COLUMN_ADAPTATION_LATITUDE = "adaptation_latitude";
        public static final String COLUMN_ADAPTATION_LINK_ONE = "adaptation_link_one";
        public static final String COLUMN_ADAPTATION_LINK_TWO = "adaptation_link_two";
        public static final String COLUMN_ADAPTATION_LINK_THREE = "adaptation_link_three";

        //Filming Location Column Names
        public static final String COLUMN_FILMING_LOCATION_NAME = "filming_location_name";
        public static final String COLUMN_FILMING_DESCRIPTION = "filming_description";
        public static final String COLUMN_FILMING_LOCATION_CITY = "filming_location_city";
        public static final String COLUMN_FILMING_CITY_IMAGE = "filming_city_image";
        public static final String COLUMN_FILMING_IMAGE_ONE = "filming_image_one";
        public static final String COLUMN_FILMING_IMAGE_TWO = "filming_image_two";
        public static final String COLUMN_FILMING_IMAGE_THREE = "filming_image_three";
        public static final String COLUMN_FILMING_IMAGE_FOUR = "filming_image_four";
        public static final String COLUMN_FILMING_IMAGE_FIVE = "filming_image_five";
        public static final String COLUMN_FILMING_IMAGE_SIX = "filming_image_six";
        public static final String COLUMN_FILMING_LONGITUDE = "filming_longitude";
        public static final String COLUMN_FILMING_LATITUDE = "filming_latitude";
        public static final String COLUMN_FILMING_LINK_ONE = "filming_link_one";
        public static final String COLUMN_FILMING_LINK_TWO = "filming_link_two";
        public static final String COLUMN_FILMING_LINK_THREE = "filming_link_three";

        //Inspiration Location Column Names
        public static final String COLUMN_INSPIRATION_LOCATION_NAME = "inspiration_location_name";
        public static final String COLUMN_INSPIRATION_DESCRIPTION = "inspiration_description";
        public static final String COLUMN_INSPIRATION_LOCATION_CITY = "inspiration_location_city";
        public static final String COLUMN_INSPIRATION_CITY_IMAGE = "inspiration_city_image";
        public static final String COLUMN_INSPIRATION_IMAGE_ONE = "inspiration_image_one";
        public static final String COLUMN_INSPIRATION_IMAGE_TWO = "inspiration_image_two";
        public static final String COLUMN_INSPIRATION_IMAGE_THREE = "inspiration_image_three";
        public static final String COLUMN_INSPIRATION_IMAGE_FOUR = "inspiration_image_four";
        public static final String COLUMN_INSPIRATION_IMAGE_FIVE = "inspiration_image_five";
        public static final String COLUMN_INSPIRATION_IMAGE_SIX = "inspiration_image_six";
        public static final String COLUMN_INSPIRATION_LONGITUDE = "inspiration_longitude";
        public static final String COLUMN_INSPIRATION_LATITUDE = "inspiration_latitude";
        public static final String COLUMN_INSPIRATION_LINK_ONE = "inspiration_link_one";
        public static final String COLUMN_INSPIRATION_LINK_TWO = "inspiration_link_two";
        public static final String COLUMN_INSPIRATION_LINK_THREE = "inspiration_link_three";

        public static Uri buildFandomUriInfoWithId(int fandomNameId) {
            return BASE_CONTENT_URI.buildUpon()
                    .appendPath(PATH_FANDOM_LOCATIONS_TABLE_NAME)
                    .appendPath("false")
                    .appendPath(String.valueOf(fandomNameId))
                    .build();
        }

        public static Uri buildFandomUriInfoWithFavorites() {
            return BASE_CONTENT_URI.buildUpon()
                    .appendPath(PATH_FANDOM_LOCATIONS_TABLE_NAME)
                    .appendPath("true")
                    .build();
        }
    }
}
