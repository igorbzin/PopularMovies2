package org.bozin.igor.popularmovies;


import android.content.Intent;
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

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieItemClickListener {



    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private MovieAdapter movieAdapter;
    private String sortByPopular;
    private String sortByRating;
    private Spinner dropdown;
    private String random;
    private String popular;
    private String rating;
    public static Resources resources;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sortByPopular = getString(R.string.sort_by_popular);
        sortByRating = getString(R.string.sort_by_rating);
        resources = getResources();
        random = getString(R.string.random);
        popular = getString(R.string.popular);
        rating = getString(R.string.rating);
        String[] items = new String[]{popular, rating};
        recyclerView = (RecyclerView) findViewById(R.id.rv_film_list);
        dropdown = (Spinner) findViewById(R.id.spinner_dropdown);
        ArrayAdapter<String> dropDownAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(dropDownAdapter);
        gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        loadMovieData(sortByPopular);


        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String message;

                switch (position) {
                    case 0:
                        loadMovieData(sortByPopular);
                        message = "popular got selected";
                        break;
                    case 1:
                        loadMovieData(sortByRating);
                        message = "rating got selected";
                        break;
                    default:
                        loadMovieData(sortByPopular);
                        message = "default selection";
                        break;
                }
                Log.v("Dropdown", message);

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//maybe some check here, fill some list or whatever what prepares some data
            }
        });

    }

    public void loadMovieData(String sortCriterium) {
        recyclerView.invalidate();
        new FetchMoviesTask().execute(sortCriterium);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {
        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            ArrayList<Movie> movieArray = null;

            if (params != null && params.length > 0) {
                String sortCriterium = params[0];
                URL movieRequestURL = NetworkUtils.buildURL(sortCriterium);
                movieArray = new ArrayList<Movie>();

                try {
                    String JSONMovieResponse = NetworkUtils.getResponseFromHttpURL(movieRequestURL);
                    movieArray = PopularMoviesJSONUtils.getMoviesFromJSON(MainActivity.this, JSONMovieResponse);

                } catch (Exception e) {  // try here to distinguish between exceptions
                    e.printStackTrace();
                }
            }
            return movieArray;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movieArray) {
            if (movieArray != null) {
                movieAdapter = new MovieAdapter(movieArray, MainActivity.this);
                recyclerView.setAdapter(movieAdapter);
            }
        }
    }

    @Override
    public void onMovieItemClick(Movie clickedMovie) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra("clicked_movie", clickedMovie);
        startActivity(intent);
    }
}