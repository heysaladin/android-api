package com.tiderdev.dilo.popularmovie.data;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MoviesOpenHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "dilo_db";
    private final static int DATABASE_VERSION = 1;

    public MoviesOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public MoviesOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_MOVIES_TABLE = "CREATE TABLE " + MoviesContract.MoviesTable.TABLE_NAME + " ( " +
                MoviesContract.MoviesTable._ID + " INTEGER NOT NULL, " +
                MoviesContract.MoviesTable.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                MoviesContract.MoviesTable.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MoviesContract.MoviesTable.COLUMN_VOTE_AVARAGE + " REAL NOT NULL, " +
                MoviesContract.MoviesTable.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                " UNIQUE ( " + MoviesContract.MoviesTable._ID + " ) ON CONFLICT REPLACE );";


        db.execSQL(CREATE_MOVIES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXIST " + MoviesContract.MoviesTable.TABLE_NAME);
        onCreate(db);
    }
}
