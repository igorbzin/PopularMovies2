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
    private String runtime;
    private int id;
    private int dbID;

    public Movie(String movieName, String link, String description, String average_vote, String date, int movieId){
        movieTitle = movieName;
        imageLink = link;
        overview = description;
        vote_average = average_vote;
        releaseDate = date;
        id = movieId;
        dbID = -1;
    }

    public int getDbID() {
        return dbID;
    }

    public void setDbID(int dbID) {
        this.dbID = dbID;
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }
}
