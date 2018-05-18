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

    private static final String PM_RESULTS = MainActivity.resources.getString(R.string.json_results);

    public static ArrayList<Movie> getMoviesFromJSON(Context context, String JSONMovieResponse) throws JSONException {

        ArrayList<Movie> movies;
        String movieName;
        String imageLink;
        String overview;
        String vote_average;
        String releaseDate;
        int id;
        String PM_MESSAGE_CODE = MainActivity.resources.getString(R.string.pm_message_code);
        String json_original_title = MainActivity.resources.getString(R.string.json_response_original_title);
        String json_image_link = MainActivity.resources.getString(R.string.json_response_image_link);
        String json_overview = MainActivity.resources.getString(R.string.json_response_ovierview);
        String json_vote_average = MainActivity.resources.getString(R.string.json_response_vote_average);
        String json_release_date = MainActivity.resources.getString(R.string.json_response_release_date);
        String json_movieID = MainActivity.resources.getString(R.string.json_response_movie_id);


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
            vote_average = Double.toString(movie.getDouble(json_vote_average));
            releaseDate = movie.getString(json_release_date);
            id = movie.getInt(json_movieID);
            Movie movieItem = new Movie(movieName, imageLink, overview, vote_average, releaseDate, id);
            movies.add(movieItem);
        }


        return movies;
    }

    public static ArrayList<MovieTrailer> getMovieTrailers(Context context, String JSONResponse) throws JSONException{
        JSONObject movieTrailers = new JSONObject(JSONResponse);
        String json_trailerName = MainActivity.resources.getString(R.string.json_response_trailer_name);
        String json_trailerKey = MainActivity.resources.getString(R.string.json_response_trailer_key);
        String trailerName;
        String trailerKey;
        JSONArray trailerArray = movieTrailers.getJSONArray(PM_RESULTS);
        ArrayList<MovieTrailer> trailers = new ArrayList<>();
        for (int i = 0; i< trailerArray.length(); i++){
            JSONObject trailer = trailerArray.getJSONObject(i);
            trailerName = trailer.getString(json_trailerName);
            trailerKey = trailer.getString(json_trailerKey);
            MovieTrailer movieTrailer = new MovieTrailer(trailerName, trailerKey);
            trailers.add(movieTrailer);
        }
        return  trailers;
    }

    public static ArrayList<MovieReview> getMovieReviews(Context context, String JSONResponse) throws JSONException{
        JSONObject movieReviews = new JSONObject(JSONResponse);
        String json_reviewAuthor = MainActivity.resources.getString(R.string.json_response_review_author);
        String json_reviewContent = MainActivity.resources.getString(R.string.json_response_review_content);
        String author;
        String reviewContent;
        JSONArray reviewArray = movieReviews.getJSONArray(PM_RESULTS);
        ArrayList<MovieReview> reviews = new ArrayList<>();
        for (int i = 0 ; i< reviewArray.length(); i++){
            JSONObject movieReview = reviewArray.getJSONObject(i);
            author = movieReview.getString(json_reviewAuthor);
            reviewContent = movieReview.getString(json_reviewContent);
            MovieReview  review = new MovieReview(author, reviewContent);
            reviews.add(review);
        }
        return reviews;
    }

    public static String getMovieDetails(Context context, String JSONResponse){
        String json_runtime = MainActivity.resources.getString(R.string.json_response_runtime);
        String movieDetail = null;

        try {
            JSONObject jsonObject = new JSONObject(JSONResponse);
            int runtime = jsonObject.getInt(json_runtime);
            int hours = runtime / 60;
            int minutes = runtime % 60;
            movieDetail = hours + " h " + minutes + " min";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieDetail;
    }





}
