package org.bozin.igor.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MoviesDbHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "movies.db";
    private static final int VERSION = 1;

    MoviesDbHelper (Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String CREATE_TABLE = "CREATE TABLE " + MoviesContract.MoviesEntry.TABLE_NAME + " (" +
                MoviesContract.MoviesEntry._ID + " INTEGER PRIMARY KEY, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_NAME + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_POSTER + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_DESCRIPTION + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_RELEASE + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_VOTE + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL);";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + MoviesContract.MoviesEntry.TABLE_NAME );
        onCreate(sqLiteDatabase);
    }
}
