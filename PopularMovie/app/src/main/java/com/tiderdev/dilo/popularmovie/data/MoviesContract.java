package com.tiderdev.dilo.popularmovie.data;

import android.net.Uri;
import android.provider.BaseColumns;


public class MoviesContract {

    public final static String CONTENT_AUTHORITY = "com.tiderdev.dilo.popularmovie";
    public final static String PATH_MOVIES = "movies";
    public final static Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public final static class MoviesTable implements BaseColumns {

        public final static Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
        public final static String TABLE_NAME = "movies";

        public final static String COLUMN_POSTER_PATH = "poster_path";
        public final static String COLUMN_VOTE_AVARAGE = "vote_average";
        public final static String COLUMN_ORIGINAL_TITLE = "original_title";
        public final static String COLUMN_OVERVIEW = "overview";

        public static long getIDFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(1));
        }

        public final static String[] MOVIES_PROJECTIONS = {
                TABLE_NAME + "." + _ID,
                COLUMN_ORIGINAL_TITLE,
                COLUMN_POSTER_PATH,
                COLUMN_OVERVIEW,
                COLUMN_VOTE_AVARAGE
        };

        public final static int COL_ID = 0;
        public final static int COL_TITLE = 1;
        public final static int COL_POSTER = 2;
        public final static int COL_OVERVIEW = 3;
        public final static int COL_VOTE = 4;
    }
}
