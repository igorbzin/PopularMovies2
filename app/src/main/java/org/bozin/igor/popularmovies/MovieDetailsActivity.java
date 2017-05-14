package org.bozin.igor.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    private Movie clickedMovie;
    private String movieName;
    private String moviePosterLink;
    private String description;
    private String vote_average;
    private String release_date;
    private ImageView iv_movie_poster;
    private TextView tv_movie_name;
    private TextView tv_user_vote;
    private TextView tv_release_date;
    private TextView tv_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        iv_movie_poster = (ImageView) findViewById(R.id.iv_movie_image);
        tv_movie_name = (TextView)findViewById(R.id.tv_movie_name);
        tv_user_vote =(TextView)findViewById(R.id.tv_user_rating);
        tv_release_date = (TextView)findViewById(R.id.tv_release_date);
        tv_description =(TextView)findViewById(R.id.tv_description);

        Intent intentWhichStartedActivity = getIntent();
        if(intentWhichStartedActivity.hasExtra("clicked_movie")){
            clickedMovie = (Movie) getIntent().getSerializableExtra("clicked_movie");

            movieName = clickedMovie.getMovieTitle();
            moviePosterLink = clickedMovie.getImageLink();
            description = clickedMovie.getOverview();
            vote_average = clickedMovie.getVote_average();
            release_date = clickedMovie.getReleaseDate();



            Picasso.with(this).load(NetworkUtils.buildImageURL(moviePosterLink).toString()).into(iv_movie_poster);
            tv_movie_name.setText(movieName);
            tv_user_vote.setText(vote_average);
            tv_release_date.setText(release_date);
            tv_description.setText(description);
        }

    }
}
