package org.bozin.igor.popularmovies;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by igorb on 08.05.2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {


    private ArrayList<Movie> movieList = new ArrayList<Movie>();

    public MovieAdapter(ArrayList<Movie> movies){
        movieList = movies;
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
        Picasso.with(holder.moviePoster.getContext()).load("http://image.tmdb.org/t/p/w185/"+imageLink).into(holder.moviePoster);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView moviePoster;

        public ViewHolder (View itemView){
            super(itemView);
            moviePoster = (ImageView)itemView.findViewById(R.id.iv_movie_poster);
        }



    }
}
