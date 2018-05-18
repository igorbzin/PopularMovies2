package org.bozin.igor.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by igor on 27.03.18.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private ArrayList<MovieReview> mReviews;

    public ReviewAdapter(ArrayList<MovieReview> reviews){
        mReviews = reviews;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_review, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MovieReview review = mReviews.get(position);
        holder.tv_author.setText(review.getAuthor());
        holder.tv_review.setText(review.getReview());
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{

        private TextView tv_review;
        private TextView tv_author;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_review = (TextView) itemView.findViewById(R.id.tv_movie_review);
            tv_author = (TextView) itemView.findViewById(R.id.tv_review_author);
        }
    }
}
