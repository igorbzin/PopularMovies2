package org.bozin.igor.popularmovies;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.content.res.Resources;
import android.database.Cursor;
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

import org.bozin.igor.popularmovies.data.MoviesContract;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {


    private static final int LOADER_ID = 1;
    private RecyclerView recyclerView;
    private GridLayoutManager mGridLayoutManager;
    private MovieAdapter movieAdapter;
    private String sortByPopular;
    private String sortByRating;
    private Spinner dropdown;
    private String random;
    private String popular;
    private String rating;
    private String favorite;
    public static Resources resources;
    public final static String LIST_STATE_KEY = "recycler_list_state";
    Parcelable listState;
    private static Bundle mBundleRecyclerViewState;


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
        favorite = getString(R.string.favorite);
        String[] items = new String[]{popular, rating, favorite};
        recyclerView = findViewById(R.id.rv_film_list);
        dropdown = findViewById(R.id.spinner_dropdown);
        ArrayAdapter<String> dropDownAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(dropDownAdapter);
        mGridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        recyclerView.setLayoutManager(mGridLayoutManager);
        recyclerView.setHasFixedSize(true);
        loadMovieData(sortByPopular);


        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        loadMovieData(sortByPopular);
                        break;
                    case 1:
                        loadMovieData(sortByRating);
                        break;
                    case 2:
                        getSupportLoaderManager().restartLoader(LOADER_ID, null, MainActivity.this);
                        break;
                    default:
                        loadMovieData(sortByPopular);
                        break;
                }

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


    @Override
    protected void onPause() {
        super.onPause();
        mBundleRecyclerViewState = new Bundle();
        onSaveInstanceState(mBundleRecyclerViewState);
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        // Save list state
        listState = mGridLayoutManager.onSaveInstanceState();
        state.putParcelable(LIST_STATE_KEY, listState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        // Retrieve list state and list/item positions
        if(state != null)
            listState = state.getParcelable(LIST_STATE_KEY);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (dropdown.getSelectedItemPosition() == 2) {
            getSupportLoaderManager().restartLoader(LOADER_ID, null, MainActivity.this);
        }

        if (listState != null) {
            mGridLayoutManager.onRestoreInstanceState(mBundleRecyclerViewState);
        }
    }


    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<Cursor> onCreateLoader(int i, final Bundle bundle) {

        Context context = getApplicationContext();
        return new AsyncTaskLoader<Cursor>(this) {

            Cursor mMovieData = null;

            @Override
            protected void onStartLoading() {
                if (mMovieData != null) {
                    deliverResult(mMovieData);
                } else {
                    forceLoad();
                }

            }

            @Override
            public Cursor loadInBackground() {


                try {
                    return getContentResolver().query(MoviesContract.MoviesEntry.CONTENT_URI, null, null, null, MoviesContract.MoviesEntry._ID);
                } catch (Exception e) {
                    Log.e("AsyncError", "Failed to load movie data");
                    e.printStackTrace();
                    return null;
                }
            }


            @Override
            public void deliverResult(Cursor data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        movieAdapter.swapData(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

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