package org.bozin.igor.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by igor on 26.03.18.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private ArrayList<MovieTrailer> mTrailers;
    private TrailerItemOnClickListener mOnClickListener;

    public interface TrailerItemOnClickListener{
        void onTrailerClick(MovieTrailer clickedTrailer);
    }

    public TrailerAdapter(ArrayList<MovieTrailer> movieTrailers, TrailerItemOnClickListener listener){
        mTrailers = movieTrailers;
        mOnClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_trailer, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String trailerName = mTrailers.get(position).getName();
        holder.tv_trailer_name.setText(trailerName);
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_trailer_name;

        public ViewHolder(View itemView){
            super(itemView);
            tv_trailer_name = (TextView) itemView.findViewById(R.id.tv_trailer_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            MovieTrailer trailer = mTrailers.get(clickedPosition);
            mOnClickListener.onTrailerClick(trailer);
        }
    }
}
