package org.bozin.igor.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by igorb on 08.05.2017.
 */

public class Movie implements Parcelable {
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

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDbID() {
        return dbID;
    }

    public void setDbID(int dbID) {
        this.dbID = dbID;
    }

    @Override

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageLink);
        dest.writeString(this.movieTitle);
        dest.writeString(this.overview);
        dest.writeString(this.vote_average);
        dest.writeString(this.releaseDate);
        dest.writeString(this.runtime);
        dest.writeInt(this.id);
        dest.writeInt(this.dbID);
    }

    protected Movie(Parcel in) {
        this.imageLink = in.readString();
        this.movieTitle = in.readString();
        this.overview = in.readString();
        this.vote_average = in.readString();
        this.releaseDate = in.readString();
        this.runtime = in.readString();
        this.id = in.readInt();
        this.dbID = in.readInt();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
