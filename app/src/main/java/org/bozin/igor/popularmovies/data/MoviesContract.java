package org.bozin.igor.popularmovies.data;

import android.provider.BaseColumns;

public class MoviesContract  {

    public static final class MoviesEntry implements BaseColumns{

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_MOVIE_NAME = "name";
        public static final String COLUMN_MOVIE_POSTER = "poster";
        public static final String COLUMN_MOVIE_DESCRIPTION = "description";
        public static final String COLUMN_MOVIE_VOTE = "vote";
        public static final String COLUMN_MOVIE_RELEASE = "release";
        public static final String COLUMN_MOVIE_ID = "movieID";

    }


}
