package com.skilenza.movieai.oauth2.response;

/**
 * Created by Neeraj on 19/03/17.
 */
public class Movie {
    private String movieId;
    private String title;
    private String genres;
    private long year;
    private String url;
    private String rating;
    private String imdb;


    public Movie(String movieId,String title,  String genres, long year,String url, String rating,String imdb) {
        this.movieId = movieId;

        this.title = title;
        this.genres=genres;
        this.year=year;
        this.url= url;
        this.rating=rating;
        this.imdb=imdb;
    }

    public Movie() {

    }

    public String getMovieId() {
        return movieId;
    }
    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getGenres() {
        return genres;
    }
    public void setGenres(String genres) {
        this.genres = genres;
    }
    public long getYear() {
        return year;
    }
    public void setYear(long year) {
        this.year = year;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getRating() {
        return rating;
    }
    public void setRating(String rating) {
        this.rating = rating;
    }
    public String getImdb() {
        return imdb;
    }
    public void setImdb(String imdb) {
        this.imdb = imdb;
    }
}
