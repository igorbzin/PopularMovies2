package org.bozin.igor.popularmovies;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by igorb on 08.05.2017.
 */

public class Movie implements Serializable {
    private String imageLink;
    private String movieTitle;
    private String overview;
    private String vote_average;
    private String releaseDate;


    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

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
