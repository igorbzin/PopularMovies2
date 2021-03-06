package org.bozin.igor.popularmovies;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.bozin.igor.popularmovies.data.MoviesContract;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieItemClickListener, LoaderManager.LoaderCallbacks {


    private static final int LOADER_ID = 1;
    private RecyclerView recyclerView;
    private NestedScrollView nestedScrollView;
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
    private ArrayList<Movie> mMoviesList;
    public final static String RV_STATE_KEY = "recycler_list_state";
    public final static String MOVIE_LIST_KEY = "movie_list_state";
    Parcelable rvListState;



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

        if (savedInstanceState != null) {
            mMoviesList = savedInstanceState.getParcelableArrayList(MOVIE_LIST_KEY);
            movieAdapter = new MovieAdapter(mMoviesList, MainActivity.this, MainActivity.this);
            recyclerView.setAdapter(movieAdapter);
            recyclerView.getLayoutManager().onRestoreInstanceState(rvListState);
        } else {
            loadMovieData(sortByPopular);
        }


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
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        // Save list state
        rvListState = recyclerView.getLayoutManager().onSaveInstanceState();
        state.putParcelable(RV_STATE_KEY, rvListState);
        state.putParcelableArrayList(MOVIE_LIST_KEY, mMoviesList);
    }


    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        // Retrieve list state and list/item positions

        if (state != null) {
            rvListState = state.getParcelable(RV_STATE_KEY);
            mMoviesList = state.getParcelableArrayList(MOVIE_LIST_KEY);
        }
    }


    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader onCreateLoader(int i, final Bundle bundle) {
        Uri uri = MoviesContract.MoviesEntry.CONTENT_URI;
        android.support.v4.content.CursorLoader cursorLoader = new android.support.v4.content.CursorLoader(MainActivity.this, uri, null, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        ArrayList<Movie> favoriteMovies = new ArrayList<>();
        if (dropdown.getSelectedItemPosition() == 2) {
            Cursor c = (Cursor) data;
            for (int i = 0; i < c.getCount(); i++) {
                if (!c.moveToPosition(i)) {
                    return;
                } else {
                    String movieName = c.getString(c.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_NAME));
                    String moviePosterLink = c.getString(c.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_POSTER));
                    String movieDescription = c.getString(c.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_DESCRIPTION));
                    String movieRelease = c.getString(c.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_RELEASE));
                    String movieVote = c.getString(c.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_VOTE));
                    int movieId = Integer.parseInt(c.getString(c.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID)));
                    Movie movie = new Movie(movieName, moviePosterLink, movieDescription, movieVote, movieRelease, movieId);
                    int dbID = c.getInt(c.getColumnIndex(MoviesContract.MoviesEntry._ID));
                    movie.setDbID(dbID);
                    favoriteMovies.add(movie);
                }
            }
            mMoviesList = favoriteMovies;
            movieAdapter.swapData(mMoviesList);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }


    public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {
        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            ArrayList<Movie> movieArray = null;

            if (params != null && params.length > 0) {
                String sortCriterium = params[0];
                URL movieRequestURL = NetworkUtils.buildURL(sortCriterium);
                movieArray = new ArrayList<>();

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
                mMoviesList = movieArray;
                movieAdapter = new MovieAdapter(mMoviesList, MainActivity.this, MainActivity.this);
                recyclerView.setAdapter(movieAdapter);
                recyclerView.getLayoutManager().onRestoreInstanceState(rvListState);
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