package com.skilenza.movieai;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.skilenza.movieai.oauth2.client.ApiService;
import com.skilenza.movieai.oauth2.request.ApiRequest;
import com.skilenza.movieai.oauth2.response.ApiResponse;
import com.skilenza.movieai.oauth2.response.Movie;
import com.skilenza.movieai.oauth2.response.SignUpResponse;
import com.skilenza.movieai.oauth2.service.IApiService;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.skilenza.movieai.oauth2.constant.OauthConstant.AUTHENTICATION_SERVER_URL;

/**
 * Created by dominicneeraj on 08/08/17.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private String token;

    private static com.squareup.okhttp.Response response;
    static final String MAIN_URL = "https://movie-recommend.herokuapp.com/recommend/api/movie/";
    static final String RATING_URL = "https://movie-recommend.herokuapp.com/recommend/api/rating/";
    ProgressBar progressBar;
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private List<Movie> movieList;
    private ProgressDialog pDialog;
    private String genrelist;
    private Button getmovie;
    private Button mymovie;
    private Map<String, String> mp;
    private String jsrating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        movieList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new MovieAdapter(MainActivity.this, movieList);
        mp = new HashMap<String, String>();
        recyclerView.setAdapter(adapter);

        SharedPreferences prefs = this.getSharedPreferences(
                android.support.v7.appcompat.BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);

        token = prefs.getString("oauth2.accesstoken", "");
        Log.e(TAG, "True");

        new GetDataTask().execute();

        try {
            Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        getmovie = (Button) findViewById(R.id.btnnewMovie);
//        mymovie = (Button) findViewById(R.id.myMovie);
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.action_item1:
                                if (movieList.get(0).getRating() == null | movieList.get(1).getRating() == null | movieList.get(2).getRating() == null | movieList.get(3).getRating() == null | movieList.get(4).getRating() == null) {
                                    Toast.makeText(getApplicationContext(), "Please Rate movies first", Toast.LENGTH_LONG).show();
                                } else {

                                    for (Movie data : movieList) {


                                        data.getMovieId();
                                        data.getRating();
                                        mp.put(String.valueOf(data.getMovieId()), data.getRating());


                                        Log.e(TAG, "" + data.getMovieId() + data.getRating());
                                    }
                                    Gson gson = new Gson();
                                    jsrating = gson.toJson(mp);
                                    Log.e(TAG, "" + jsrating);
                                    new GetRatingTask().execute(jsrating);
                                    recyclerView.invalidate();

                                }
                                break;
                            case R.id.action_item2:
                                new GetMyMovieTask().execute();
                                break;
                            case R.id.action_item3:
                                new GetDataTask().execute();
                                break;
                            case R.id.action_item4:
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                        }

                        return true;
                    }
                });


//
//
//        getmovie.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Button b = (Button) v;
//                String text = b.getText().toString();
//                Log.e(TAG, text);
//                if (text.equalsIgnoreCase("get new movies")) {
//
//                    if (movieList.get(0).getRating() == null | movieList.get(1).getRating() == null | movieList.get(2).getRating() == null | movieList.get(3).getRating() == null | movieList.get(4).getRating() == null) {
//                        Toast.makeText(getApplicationContext(), "Please Rate movies first", Toast.LENGTH_LONG).show();
//                    } else {
//
//                        for (Movie data : movieList) {
//
//
//                            data.getMovieId();
//                            data.getRating();
//                            mp.put(String.valueOf(data.getMovieId()), data.getRating());
//
//
//                            Log.e(TAG, "" + data.getMovieId() + data.getRating());
//                        }
//                        Gson gson = new Gson();
//                        jsrating = gson.toJson(mp);
//                        Log.e(TAG, "" + jsrating);
//                        new GetRatingTask().execute(jsrating);
//                        recyclerView.invalidate();
//
//                    }
//
//
//                }
//
//                else {
//                    new GetDataTask().execute();
//
//
//                }
//
//
//            }
//        });
//
//
//
//        mymovie.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new GetMyMovieTask().execute();
//
//
//            }
//        });
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
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
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    class GetMyMovieTask extends AsyncTask<Void, Void, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /**
             * Progress Dialog for User Interaction
             */
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setTitle("Please wait...");
            dialog.setMessage("Loading");
            dialog.show();
        }

        @Nullable
        @Override
        protected String doInBackground(Void... params) {

            try {
                OkHttpClient client = new OkHttpClient();


                Request request = new Request.Builder()
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + token)
                        .url(RATING_URL)
                        .build();
                response = client.newCall(request).execute();

                return response.body().string();
            } catch (@NonNull IOException e) {
                Log.e(TAG, "" + e.getLocalizedMessage());
            }


            return null;
        }

        @Override
        protected void onPostExecute(String Response) {
            super.onPostExecute(Response);
            dialog.dismiss();
            movieList.clear();

            Log.e(TAG, "Response");
            JSONArray jsonarray = null;
            try {
                jsonarray = new JSONArray(Response);
                Log.e(TAG, String.valueOf(jsonarray.getJSONObject(0)));
            } catch (JSONException e) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

            for (int i = 0; i < jsonarray.length(); i++) {
                Movie movie = new Movie();
                JSONObject json = null;
                genrelist = "";
                try {
                    json = jsonarray.getJSONObject(i);
                    movie.setMovieId(json.getString("movieId"));
                    movie.setTitle(json.getString("title"));
                    String generes = json.getString("genres");
                    movie.setRating(json.getString("rating"));

                    JSONArray genresjson = new JSONArray(generes);

                    if (genresjson.length() >= 1) {
                        genrelist = genresjson.getString(0);
                    }
                    for (int j = 1; j < genresjson.length(); j++) {

                        genrelist += ", " + genresjson.getString(j);


                    }
                    Log.e(TAG, String.valueOf(genresjson.getString(0)));
                    movie.setGenres(genrelist);
                    movie.setYear(Long.parseLong(json.getString("year")));
                    movie.setUrl(json.getString("url"));
                    movie.setImdb(json.getString("imdb"));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                movieList.add(movie);
            }


            adapter.notifyDataSetChanged();


        }
    }
    class GetDataTask extends AsyncTask<Void, Void, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /**
             * Progress Dialog for User Interaction
             */
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setTitle("Please wait...");
            dialog.setMessage("Loading");
            dialog.show();
        }

        @Nullable
        @Override
        protected String doInBackground(Void... params) {

            try {
                OkHttpClient client = new OkHttpClient();


                Request request = new Request.Builder()
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + token)
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
        protected void onPostExecute(String Response) {
            super.onPostExecute(Response);
            dialog.dismiss();
            movieList.clear();


            JSONArray jsonarray = null;
            if(Response!=null){
                Log.e(TAG, Response);
                try {
                    jsonarray = new JSONArray(Response);
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Connection Problem", Toast.LENGTH_LONG).show();
                }

            }
            else
                Toast.makeText(getApplicationContext(), "Connection Problem", Toast.LENGTH_LONG).show();

try{
            for (int i = 0; i < jsonarray.length(); i++) {
                Movie movie = new Movie();
                JSONObject json = null;
                genrelist = "";
                try {
                    json = jsonarray.getJSONObject(i);
                    movie.setMovieId(json.getString("movieId"));
                    movie.setTitle(json.getString("title"));
                    String generes = json.getString("genres");
                    JSONArray genresjson = new JSONArray(generes);

                    if (genresjson.length() >= 1) {
                        genrelist = genresjson.getString(0);
                    }
                    for (int j = 1; j < genresjson.length(); j++) {

                        genrelist += ", " + genresjson.getString(j);


                    }
                    Log.e(TAG, String.valueOf(genresjson.getString(0)));
                    movie.setGenres(genrelist);
                    movie.setYear(Long.parseLong(json.getString("year")));
                    movie.setUrl(json.getString("url"));
                    movie.setImdb(json.getString("imdb"));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                movieList.add(movie);
            }}
catch(NullPointerException e){


    Toast.makeText(getApplicationContext(), "Connection Problem", Toast.LENGTH_LONG).show();
}

            adapter.notifyDataSetChanged();


        }
    }

    class GetRatingTask extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /**
             * Progress Dialog for User Interaction
             */
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setTitle("Please wait...");
            dialog.setMessage("Loading");
            dialog.show();
        }

        @Nullable
        @Override
        protected String doInBackground(String... params) {
            String da = params[0];
            try {
                OkHttpClient client = new OkHttpClient();
                MediaType JSON
                        = MediaType.parse("application/json; charset=utf-8");


                RequestBody body = RequestBody.create(JSON, da);
                Request request = new Request.Builder()
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + token)
                        .url(RATING_URL)
                        .post(body)
                        .build();
                response = client.newCall(request).execute();

                return response.body().string();
            } catch (@NonNull IOException e) {
                Log.e(TAG, "" + e.getLocalizedMessage());
            }


            return null;
        }


        @Override
        protected void onPostExecute(String Response) {
            super.onPostExecute(Response);
            dialog.dismiss();
            movieList.clear();

            Log.e(TAG, "Response");
            JSONArray jsonarray = null;
            try {
                jsonarray = new JSONArray(Response);
                Log.e(TAG, String.valueOf(jsonarray.getJSONObject(0)));
            } catch (JSONException e) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

            for (int i = 0; i < jsonarray.length(); i++) {
                Movie movie = new Movie();
                JSONObject json = null;
                genrelist = "";
                try {
                    json = jsonarray.getJSONObject(i);
                    movie.setMovieId(json.getString("movieId"));
                    movie.setTitle(json.getString("title"));
                    String generes = json.getString("genres");
                    movie.setRating(json.getString("rating"));

                    JSONArray genresjson = new JSONArray(generes);

                    if (genresjson.length() >= 1) {
                        genrelist = genresjson.getString(0);
                    }
                    for (int j = 1; j < genresjson.length(); j++) {

                        genrelist += ", " + genresjson.getString(j);


                    }
                    Log.e(TAG, String.valueOf(genresjson.getString(0)));
                    movie.setGenres(genrelist);
                    movie.setYear(Long.parseLong(json.getString("year")));
                    movie.setUrl(json.getString("url"));
                    movie.setImdb(json.getString("imdb"));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                movieList.add(movie);
            }


            adapter.notifyDataSetChanged();


        }
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

//            if (includeEdge) {
//                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
//                outRect.right = (column ) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)
//
//                if (position < spanCount) { // top edge
//                    outRect.top = spacing;
//                }
//                outRect.bottom = spacing; // item bottom
//            } else {
            outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
            outRect.right = spacing - (column) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)

//            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        switch (item.getItemId()) {

            case R.id.action_logout:
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);


    }
}

