package org.bozin.igor.popularmovies;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;


/**
 * Created by igorb on 13.05.2017.
 */

public class PopularMoviesJSONUtils {


    public static ArrayList<Movie> getMoviesFromJSON(Context context, String JSONMovieResponse) throws JSONException {

        final String PM_RESULTS = MainActivity.resources.getString(R.string.json_results);
        ArrayList<Movie> movies;
        String movieName;
        String imageLink;
        String overview;
        String vote_average;
        String releaseDate;
        String PM_MESSAGE_CODE = MainActivity.resources.getString(R.string.pm_message_code);
        String json_original_title = MainActivity.resources.getString(R.string.json_response_original_title);
        String json_image_link = MainActivity.resources.getString(R.string.json_response_image_link);
        String json_overview = MainActivity.resources.getString(R.string.json_response_ovierview);
        String json_vote_average = MainActivity.resources.getString(R.string.json_response_vote_average);
        String json_release_date = MainActivity.resources.getString(R.string.json_response_release_date);


        JSONObject movieJSON = new JSONObject(JSONMovieResponse);

        if (movieJSON.has(PM_MESSAGE_CODE)) {
            int errorCode = movieJSON.getInt(PM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray movieArray = movieJSON.getJSONArray(PM_RESULTS);
        movies = new ArrayList<>();

        for (int i = 0; i < movieArray.length(); i++) {
            JSONObject movie = movieArray.getJSONObject(i);
            movieName = movie.getString(json_original_title);
            imageLink = movie.getString(json_image_link);
            overview = movie.getString(json_overview);
            vote_average = movie.getString(json_vote_average);
            releaseDate = movie.getString(json_release_date);

            Movie movieItem = new Movie(movieName, imageLink, overview, vote_average, releaseDate);
            movies.add(movieItem);
        }


        return movies;
    }
}
