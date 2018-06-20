package org.bozin.igor.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
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

    private ArrayList<Movie> mMovieList;
    private Context mContext;

    public MovieAdapter(ArrayList<Movie> movies, MovieItemClickListener onClickListener, Context context){
        mMovieList = movies;
        mOnClickListener = onClickListener;
        mContext = context;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String imageLink = mMovieList.get(position).getImageLink();
        Log.v("Image", imageLink);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = 0;
        int height = 0;
        int orientation = mContext.getResources().getConfiguration().orientation;
        if ( orientation == Configuration.ORIENTATION_PORTRAIT) {
            width = displaymetrics.widthPixels / 2;
            height = displaymetrics.heightPixels / 2;
        } else if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            width = displaymetrics.widthPixels / 3;
            height = displaymetrics.heightPixels ;

        }


        holder.moviePoster.getLayoutParams().width = width;
        holder.moviePoster.getLayoutParams().height = height;

        Picasso.with(holder.moviePoster.getContext()).load(NetworkUtils.buildImageURL(imageLink).toString()).into(holder.moviePoster);
    }


    public void swapData(ArrayList<Movie> movieList){
        mMovieList = movieList;
        this.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
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
            Movie clickedMovie = mMovieList.get(clickedPosition);
            mOnClickListener.onMovieItemClick(clickedMovie);
        }
    }
}
