package org.bozin.igor.popularmovies;

import org.json.JSONObject;

/**
 * Created by igorb on 08.05.2017.
 */

public class Movie {
    private String imageLink;
    private String movieTitle;
    private String overview;
    private String vote_average;
    private String releaseDate;

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public Movie(String movieName, String link, String description, String average_vote, String date){
        movieTitle = movieName;
        imageLink = link;
        overview = description;
        vote_average = average_vote;
        releaseDate = date;
    }
}
