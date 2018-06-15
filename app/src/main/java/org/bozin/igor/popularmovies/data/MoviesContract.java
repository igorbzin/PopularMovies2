package org.bozin.igor.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class MoviesContract  {

    public static final String AUTHORITY = "org.bozin.igor.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_MOVIES = "movies";


    public static final class MoviesEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build() ;

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_MOVIE_NAME = "name";
        public static final String COLUMN_MOVIE_POSTER = "poster";
        public static final String COLUMN_MOVIE_DESCRIPTION = "description";
        public static final String COLUMN_MOVIE_VOTE = "vote";
        public static final String COLUMN_MOVIE_RELEASE = "release";
        public static final String COLUMN_MOVIE_ID = "movieID";

    }


}
