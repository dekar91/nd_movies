package com.dekar.popularmovies.provider;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import static com.dekar.popularmovies.provider.MoviesContract.*;

final class MoviesDatabase extends SQLiteOpenHelper {
    private static final String DB_NAME = "movies.db";
    private static final int DB_VERSION = 2;


    interface Tables {
        String MOVIES = "movies";
    }

    public MoviesDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Tables.MOVIES + "("
                + BaseColumns._ID + " INTEGER NOT NULL PRIMARY KEY,"
                + MoviesColumns.MOVIE_ID + " TEXT NOT NULL,"
                + MoviesColumns.MOVIE_TITLE + " TEXT NOT NULL,"
                + MoviesColumns.MOVIE_OVERVIEW + " TEXT,"
                + MoviesColumns.MOVIE_VOTE_AVERAGE + " TEXT,"
                + MoviesColumns.MOVIE_POSTER_PATH + " TEXT,"
                + MoviesColumns.MOVIE_RELEASE_DATE + " TEXT,"
                + "UNIQUE (" + MoviesColumns.MOVIE_ID + ") ON CONFLICT REPLACE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    public static void deleteDatabase(Context context) {
        context.deleteDatabase(DB_NAME);
    }
}