package org.bozin.igor.popularmovies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.bozin.igor.popularmovies.databinding.ActivityMovieDetailsBinding;
import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MovieDetailsActivity extends AppCompatActivity implements TrailerAdapter.TrailerItemOnClickListener {

    ActivityMovieDetailsBinding mBinding;
    private String movieID;
    private RecyclerView RVTrailers;
    private RecyclerView RVReviews;
    private ArrayList<MovieTrailer> movieTrailers;
    private ArrayList<MovieReview> movieReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);
        movieTrailers = new ArrayList<>();
        movieReviews = new ArrayList<>();
        ImageView iv_movie_poster = (ImageView) findViewById(R.id.iv_movie_image);
        RVTrailers = (RecyclerView) findViewById(R.id.rv_trailer_links);
        RVReviews = (RecyclerView) findViewById(R.id.rv_reviews);
        mBinding.tvTrailersLabel.setText(R.string.trailers);

        Intent intentWhichStartedActivity = getIntent();
        if (intentWhichStartedActivity.hasExtra("clicked_movie")) {
            Movie clickedMovie = (Movie) getIntent().getSerializableExtra("clicked_movie");

            String movieName = clickedMovie.getMovieTitle();
            String moviePosterLink = clickedMovie.getImageLink();
            String description = clickedMovie.getOverview();
            String vote_average = clickedMovie.getVote_average();
            String release_date = clickedMovie.getReleaseDate();
            movieID = Integer.toString(clickedMovie.getId());


            Picasso.with(this).load(NetworkUtils.buildImageURL(moviePosterLink).toString()).into(iv_movie_poster);
            mBinding.tvMovieName.setText(movieName);
            mBinding.tvUserRating.setText(vote_average);
            mBinding.tvReleaseDate.setText(release_date);
            mBinding.tvDescription.setText(description);


            new FetchDetailsTask().execute(movieID);
            new FetchTrailersTask().execute(movieID);
            new FetchReviewsTask().execute(movieID);

        }

    }

    @Override
    public void onTrailerClick(MovieTrailer clickedTrailer) {
        URL trailerURL = NetworkUtils.buildYoutubeURL(clickedTrailer.getKey());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(trailerURL.toString()));
        startActivity(intent);
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
