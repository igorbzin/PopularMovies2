package org.bozin.igor.popularmovies;

import android.database.Cursor;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.bozin.igor.popularmovies.data.MoviesContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by igorb on 08.05.2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>  {

    private MovieItemClickListener mOnClickListener;

    public interface MovieItemClickListener{
        void onMovieItemClick(Movie clickedMovie);
    }

    private ArrayList<Movie> movieList = new ArrayList<Movie>();

    public MovieAdapter(ArrayList<Movie> movies, MovieItemClickListener onClickListener){
        movieList = movies;
        mOnClickListener = onClickListener;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String imageLink = movieList.get(position).getImageLink();
        Log.v("Image", imageLink);
        Picasso.with(holder.moviePoster.getContext()).load(NetworkUtils.buildImageURL(imageLink).toString()).into(holder.moviePoster);
    }


    public void swapData(Cursor c){
        ArrayList<Movie> favoriteMovies = new ArrayList<>();
        for(int i = 0 ; i< c.getCount(); i++){
            if (!c.moveToPosition(i)){
                return;
            } else {
                String movieName = c.getString(c.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_NAME));
                String moviePosterLink = c.getString(c.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_POSTER));
                String movieDescription = c.getString(c.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_DESCRIPTION));
                String movieRelease = c.getString(c.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_RELEASE));
                String movieVote = c.getString(c.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_VOTE));
                int movieId = Integer.parseInt(c.getString(c.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID)));
                Movie movie = new Movie(movieName, moviePosterLink, movieDescription, movieVote, movieRelease, movieId );
                int dbID = c.getInt(c.getColumnIndex(MoviesContract.MoviesEntry._ID));
                movie.setDbID(dbID);
                favoriteMovies.add(movie);
            }
        }

        movieList = favoriteMovies;
        this.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView moviePoster;

        public ViewHolder (View itemView){
            super(itemView);
            moviePoster = (ImageView)itemView.findViewById(R.id.iv_movie_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            Movie clickedMovie = movieList.get(clickedPosition);
            mOnClickListener.onMovieItemClick(clickedMovie);
        }
    }
}
