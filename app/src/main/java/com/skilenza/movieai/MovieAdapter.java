package com.skilenza.movieai;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.skilenza.movieai.oauth2.response.Movie;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Neeraj on 19/03/17.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {
    private String TAG = MovieAdapter.class.getSimpleName();
    private Context mContext;
    private List<Movie> movieList;
   public HashMap<String, String> ratingquery = new HashMap<String, String>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, genres,year;
        public ImageView thumbnail;
        public RatingBar ratingBar;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            genres= (TextView) view.findViewById(R.id.genres);
            year = (TextView) view.findViewById(R.id.year);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            ratingBar= (RatingBar) view.findViewById(R.id.card_view_rating_bar);
        }
    }


    public MovieAdapter(Context mContext, List<Movie> movieList) {
        this.mContext = mContext;
        this.movieList = movieList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
         final Movie movie = movieList.get(position);
        holder.title.setText(movie.getTitle());
        holder.genres.setText(movie.getGenres());
        holder.year.setText( String.valueOf(movie.getYear()));

        // loading movie cover using Glide library
        Glide.with(mContext).load(movie.getUrl()).into(holder.thumbnail);

if(movie.getRating()==null){
    holder.ratingBar.setRating(0);
 holder.ratingBar.setIsIndicator(false);

    holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

        @Override
        public void onRatingChanged(RatingBar arg0, float rateValue, boolean arg2) {
            // TODO Auto-generated method stub
            Log.e("Rating", "your selected value is :"+rateValue);




            movie.setRating(String.valueOf(rateValue));


        }
    });






}


else{
    Log.e("Rating is not null", movie.getRating());
    holder.ratingBar.setRating(Float.parseFloat(movie.getRating()));
    holder.ratingBar.setIsIndicator(true);

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(mContext, MovieDetailActivity.class);
                Log.e(TAG, "True"+movie.getUrl());
                intent.putExtra("title", movie.getTitle());
                intent.putExtra("genres", movie.getGenres());
                intent.putExtra("year", String.valueOf(movie.getYear()));
                intent.putExtra("url", movie.getUrl());
                intent.putExtra("rating", movie.getRating());
                intent.putExtra("imdb", movie.getImdb());
                mContext.startActivity(intent);






            }
        });
    }}

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_movie, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Rate", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
}
