package org.bozin.igor.popularmovies;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.bozin.igor.popularmovies.data.MoviesContract;
import org.bozin.igor.popularmovies.databinding.ActivityMovieDetailsBinding;
import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MovieDetailsActivity extends AppCompatActivity implements TrailerAdapter.TrailerItemOnClickListener, android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {

    ActivityMovieDetailsBinding mBinding;
    private static final int DETAIL_LOADER_ID = 10;
    private String movieID;
    private RecyclerView RVTrailers;
    private RecyclerView RVReviews;
    private ArrayList<MovieTrailer> movieTrailers;
    private ArrayList<MovieReview> movieReviews;
    private Button btnAddFavorite;
    private String movieName;
    private String moviePosterLink;
    private String description;
    private String vote_average;
    private String release_date;
    private int dbID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);
        movieTrailers = new ArrayList<>();
        movieReviews = new ArrayList<>();
        ImageView iv_movie_poster = findViewById(R.id.iv_movie_image);
        RVTrailers = findViewById(R.id.rv_trailer_links);
        RVReviews = findViewById(R.id.rv_reviews);
        mBinding.tvTrailersLabel.setText(R.string.trailers);
        btnAddFavorite = findViewById(R.id.btn_favorite_movies);


        Intent intentWhichStartedActivity = getIntent();
        if (intentWhichStartedActivity.hasExtra("clicked_movie")) {
            Movie clickedMovie = (Movie) getIntent().getSerializableExtra("clicked_movie");

            movieName = clickedMovie.getMovieTitle();
            moviePosterLink = clickedMovie.getImageLink();
            description = clickedMovie.getOverview();
            vote_average = clickedMovie.getVote_average();
            release_date = clickedMovie.getReleaseDate();
            movieID = Integer.toString(clickedMovie.getId());
            dbID = clickedMovie.getDbID();

            Picasso.with(this).load(NetworkUtils.buildImageURL(moviePosterLink).toString()).into(iv_movie_poster);
            mBinding.tvMovieName.setText(movieName);
            mBinding.tvUserRating.setText(vote_average);
            mBinding.tvReleaseDate.setText(release_date);
            mBinding.tvDescription.setText(description);


            new FetchDetailsTask().execute(movieID);
            new FetchTrailersTask().execute(movieID);
            new FetchReviewsTask().execute(movieID);

        }

        getSupportLoaderManager().initLoader(DETAIL_LOADER_ID, null, MovieDetailsActivity.this).forceLoad();

        btnAddFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dbID == -1) {
                    ContentValues cv = new ContentValues();
                    cv.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_NAME, movieName);
                    cv.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_DESCRIPTION, description);
                    cv.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID, movieID);
                    cv.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_POSTER, moviePosterLink);
                    cv.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_RELEASE, release_date);
                    cv.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_VOTE, vote_average);

                    Uri uri = getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI, cv);

                    getSupportLoaderManager().restartLoader(DETAIL_LOADER_ID, null, MovieDetailsActivity.this);

                } else {
                    Uri uri = MoviesContract.MoviesEntry.CONTENT_URI;
                    uri = uri.buildUpon().appendPath(Integer.toString(dbID)).build();
                    getContentResolver().delete(uri, null, null);
                    dbID = -1;
                    btnAddFavorite.setBackgroundResource(R.drawable.ic_star_unselected_24dp);

                }


            }
        });

    }

    @Override
    public void onTrailerClick(MovieTrailer clickedTrailer) {
        URL trailerURL = NetworkUtils.buildYoutubeURL(clickedTrailer.getKey());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(trailerURL.toString()));
        startActivity(intent);
    }


    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            Cursor favoriteMovies = null;

            @Override
            protected void onStartLoading() {
                if (favoriteMovies != null) {
                    deliverResult(favoriteMovies);
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
                favoriteMovies = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        btnAddFavorite.setBackgroundResource(R.drawable.ic_star_unselected_24dp);
        for (int i = 0; i < data.getCount(); i++) {
            if (!data.moveToPosition(i)) {
                return;
            } else {
                String mID = data.getString(data.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID));
                if (mID.equals(movieID)) {
                    int id = data.getInt(data.getColumnIndex(MoviesContract.MoviesEntry._ID));
                    dbID = id;
                    btnAddFavorite.setBackgroundResource(R.drawable.ic_star_selected_24dp);
                    return;
                }
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(DETAIL_LOADER_ID, null, this);
    }

    public class FetchDetailsTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String JSONDetailsResponse = null;
            String runtime = null;
            if (strings != null && strings.length > 0) {
                String movieParam = strings[0];
                URL movieDetailsURL = NetworkUtils.buildDetailsURL(movieParam);

                try {
                    JSONDetailsResponse = NetworkUtils.getResponseFromHttpURL(movieDetailsURL);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                runtime = PopularMoviesJSONUtils.getMovieDetails(MovieDetailsActivity.this, JSONDetailsResponse);
            }
            return runtime;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mBinding.tvRuntime.setText(s);
        }
    }


    public class FetchTrailersTask extends AsyncTask<String, Void, ArrayList<MovieTrailer>> {

        @Override
        protected ArrayList<MovieTrailer> doInBackground(String... strings) {
            String JSONTrailersResponse = null;
            if (strings != null && strings.length > 0) {
                String trailerParam = strings[0];
                URL movieTrailersURL = NetworkUtils.buildTrailersURL(trailerParam);
                try {
                    JSONTrailersResponse = NetworkUtils.getResponseFromHttpURL(movieTrailersURL);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                movieTrailers = PopularMoviesJSONUtils.getMovieTrailers(MovieDetailsActivity.this, JSONTrailersResponse);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return movieTrailers;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieTrailer> movieTrailers) {
            super.onPostExecute(movieTrailers);
            TrailerAdapter trailerAdapter = new TrailerAdapter(movieTrailers, MovieDetailsActivity.this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MovieDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
            RVTrailers.setLayoutManager(layoutManager);
            RVTrailers.setAdapter(trailerAdapter);
            RVTrailers.setNestedScrollingEnabled(false);
        }
    }

    public class FetchReviewsTask extends AsyncTask<String, Void, ArrayList<MovieReview>> {

        @Override
        protected ArrayList<MovieReview> doInBackground(String... strings) {
            String JSONReviewsResponse = null;
            if (strings != null && strings.length > 0) {
                String trailerParam = strings[0];
                URL movieReviewsURL = NetworkUtils.buildReviewsURL(trailerParam);
                try {
                    JSONReviewsResponse = NetworkUtils.getResponseFromHttpURL(movieReviewsURL);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                movieReviews = PopularMoviesJSONUtils.getMovieReviews(MovieDetailsActivity.this, JSONReviewsResponse);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return movieReviews;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieReview> reviews) {
            super.onPostExecute(reviews);
            ReviewAdapter reviewAdapter = new ReviewAdapter(reviews);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MovieDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
            RVReviews.setLayoutManager(layoutManager);
            RVReviews.setAdapter(reviewAdapter);
            RVReviews.setNestedScrollingEnabled(false);
        }
    }
}
