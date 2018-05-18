package org.bozin.igor.popularmovies;

/**
 * Created by igor on 27.03.18.
 */

public class MovieReview {

    private String author;
    private String review;

    public MovieReview(String author, String review) {
        this.author = author;
        this.review = review;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
