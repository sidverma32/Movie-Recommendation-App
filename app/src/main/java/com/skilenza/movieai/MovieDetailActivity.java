package com.skilenza.movieai;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.skilenza.movieai.oauth2.response.Movie;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Neeraj on 3/19/2017.
 */

public class MovieDetailActivity extends AppCompatActivity {
    private static Response response;
    static final String image_url="http://image.tmdb.org/t/p/w500/";
    private String imdb;
    private String MAIN_URL ;
    MediaPlayer mediaplayer;
    private String TAG = MovieDetailActivity.class.getSimpleName();
    private String movie;
    private String url;
    private String artists;
    private String cover;
    private Integer position;
    private ImageView imageView;
    private TextView movie_title;
    private TextView movie_overview;
    private boolean flag;
    private ProgressDialog dialog;



    private String movie1;
    private String url1;
    private String artists1;
    private String cover1;
    private Movie moviei;
    private Integer n;
    private String title;
private RatingBar ratingBar;
    private String genres;
    private String year;
    private String rating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_layout);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initCollapsingToolbar();

        Bundle extras = getIntent().getExtras();
        if (extras!= null) {

            title = extras.getString("title");

             url1 = extras.getString("url");
            Log.e(TAG, "True"+url);

            genres = extras.getString("genres");
imdb=extras.getString("imdb");
            year=extras.getString("year");
            rating=extras.getString("rating");
        }
imageView=(ImageView)findViewById(R.id.imageView);
        movie_title=(TextView)findViewById(R.id.movieTitle);
        movie_overview=(TextView)findViewById(R.id.overview);
        ratingBar=(RatingBar)findViewById(R.id.rating_bar);
        ratingBar.setRating(Float.parseFloat(rating));
       MAIN_URL= "https://api.themoviedb.org/3/movie/"+imdb+"?api_key=b831a3c80a3bb0635ae1afd7a22c0e8c";
        new GetDataTask().execute();







    }
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(title);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    class GetDataTask extends AsyncTask<Void, Void, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /**
             * Progress Dialog for User Interaction
             */
            dialog = new ProgressDialog(MovieDetailActivity.this);
            dialog.setTitle("Please wait...");
            dialog.setMessage("Loading");
            dialog.show();
        }

        @Nullable
        @Override
        protected String doInBackground(Void... params) {
            Log.e(TAG, "url" +MAIN_URL );
            try {
                OkHttpClient client = new OkHttpClient();




                Request request = new Request.Builder()
                        .url(MAIN_URL)
                        .build();
                response = client.newCall(request).execute();

                return response.body().string();
            } catch (@NonNull IOException e) {
                Log.e(TAG, "" + e.getLocalizedMessage());
            }


            return null;
        }

        @Override
        public void onPostExecute(String Response) {
            super.onPostExecute(Response);
            dialog.dismiss();
            JSONObject jsonobject = null;
            try {
                jsonobject = new JSONObject(Response);
                Log.e(TAG, String.valueOf(jsonobject));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                Log.e(TAG,image_url+jsonobject.getString("backdrop_path").substring(1));
                Glide.with(getApplicationContext()).load(image_url+jsonobject.getString("backdrop_path").substring(1)).into((ImageView) findViewById(R.id.imageView));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                movie_title.setText(jsonobject.getString("original_title"));
                movie_overview.setText(jsonobject.getString("overview"));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();

        if (i == android.R.id.home) {

            finish();
            return true;

        }

        else {
            return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}

