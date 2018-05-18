package org.bozin.igor.popularmovies;

/**
 * Created by igor on 26.03.18.
 */

public class MovieTrailer {

    private String name;
    private String key;

    public MovieTrailer(String name, String key) {
        this.name = name;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public void setName(String name) {

        this.name = name;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
