package com.tiderdev.dilo.popularmovie.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;


public class MoviesProvider extends ContentProvider {

    MoviesOpenHelper nMoviesOpenHelper;

    private final static int MOVIES = 100;
    private final static int MOVIES_WITH_ID = 101;

    private final static String mSelection = MoviesContract.MoviesTable.TABLE_NAME +
            " . " + MoviesContract.MoviesTable._ID + "=?";

    final UriMatcher mUriMatcher = buildUriMatcher();
    private final static SQLiteQueryBuilder mMoviesQueryBuilder;

    static {
        mMoviesQueryBuilder = new SQLiteQueryBuilder();
        mMoviesQueryBuilder.setTables(MoviesContract.MoviesTable.TABLE_NAME);
    }

    @Override
    public boolean onCreate() {

        nMoviesOpenHelper = new MoviesOpenHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {


        Cursor retCursor;

        int match = mUriMatcher.match(uri);
        Log.d("Provider", String.valueOf(match));
        switch (match) {
            case MOVIES:
                retCursor = mMoviesQueryBuilder.query(
                        nMoviesOpenHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case MOVIES_WITH_ID:
                retCursor = getMovieByID(uri, projection, sortOrder);
            default:
                throw new UnsupportedOperationException("Uri : " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return null;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {

        SQLiteDatabase db = nMoviesOpenHelper.getWritableDatabase();
        int returnCount = 0;
        int match = mUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                db.beginTransaction();

                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesContract.MoviesTable.TABLE_NAME, null, value);

                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();

                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;

            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    private UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_MOVIES + "/*", MOVIES_WITH_ID);
        return uriMatcher;
    }

    private Cursor getMovieByID(Uri uri, String[] projection, String sortOrder) {

        long id = MoviesContract.MoviesTable.getIDFromUri(uri);


        Cursor retCursor = mMoviesQueryBuilder.query(
                nMoviesOpenHelper.getReadableDatabase(),
                projection,
                mSelection,
                new String[]{String.valueOf(id)},
                null,
                null,
                sortOrder
        );

        return retCursor;
    }
}
