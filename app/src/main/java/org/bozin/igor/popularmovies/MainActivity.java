package org.bozin.igor.popularmovies;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private MovieAdapter movieAdapter;
    private String sortBy;
    private Spinner dropdown;
    private String random;
    private String popular;
    private String rating;
    public static Resources resources;
    public ArrayList<Movie> movieList;


    public void setMovieList(ArrayList<Movie> movieList) {
        this.movieList = movieList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resources = getResources();
        movieList = new ArrayList<Movie>();
        random = getString(R.string.random);
        popular = getString(R.string.popular);
        rating = getString(R.string.rating);
        String[] items = new String[]{random, popular, rating};
        recyclerView = (RecyclerView) findViewById(R.id.rv_film_list);
        dropdown = (Spinner) findViewById(R.id.spinner_dropdown);
        ArrayAdapter<String> dropDownAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(dropDownAdapter);

        gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        movieAdapter = new MovieAdapter(movieList);
        recyclerView.setAdapter(movieAdapter);
        sortBy = resources.getString(R.string.sort_by_popular);
        loadMovieData(sortBy);
        movieAdapter = new MovieAdapter(movieList);
        recyclerView.setAdapter(movieAdapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        sortBy = getString(R.string.sort_by_popular);
                        loadMovieData(sortBy);
                        movieAdapter = new MovieAdapter(movieList);
                        recyclerView.setAdapter(movieAdapter);
                        Log.v("Dropdown", "random got selected");
                        return;
                    case 1:
                        sortBy = getString(R.string.sort_by_popular);
                        loadMovieData(sortBy);
                        movieAdapter = new MovieAdapter(movieList);
                        recyclerView.setAdapter(movieAdapter);
                        Log.v("Dropdown", "popular got selected");
                        return;
                    case 2:
                        sortBy = getString(R.string.sort_by_rating);
                        loadMovieData(sortBy);
                        movieAdapter = new MovieAdapter(movieList);
                        recyclerView.setAdapter(movieAdapter);
                        Log.v("Dropdown", "rating got selected");
                        return;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void loadMovieData(String sortCriterium) {
        new FetchMoviesTask().execute(sortCriterium);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>>{

        private ArrayList<Movie> movieArray;

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            if(params.length == 0){
                return null;
            }

            String sortCriterium = params[0];
            URL movieRequestURL = NetworkUtils.buildURL(sortCriterium);
            movieArray = new ArrayList<Movie>();
            try{
                String JSONMovieResponse = NetworkUtils.getResponseFromHttpURL(movieRequestURL);
                movieArray = PopularMoviesJSONUtils.getMoviesFromJSON(MainActivity.this, JSONMovieResponse);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return movieArray;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movieArray) {
            if(movieArray!= null){
               setMovieList(movieArray);
            }
            }
    }
}
