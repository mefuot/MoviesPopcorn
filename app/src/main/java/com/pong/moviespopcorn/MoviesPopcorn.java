package com.pong.moviespopcorn;

import android.app.Application;

/**
 * Created by pong on 10/7/15 AD.
 */
public class MoviesPopcorn extends Application {
    public String appName = "MoviesPopcorn";

    public static final String BASE_API_URL = "http://api.themoviedb.org/3/discover/movie?";
    public static final String API_KEY = "7119e96352d0e13416b5d6e54d448642";

    public static final String MOST_POPULAR_URL = BASE_API_URL +"sort_by=popularity.desc&api_key="+API_KEY;
    public static final String HIGHTEST_RATED_URL = BASE_API_URL +"sort_by=vote_average.desc&api_key="+API_KEY;

    public static final String MOVIE_LIST_KEY = "movie_list";
    public static final String MOVIE_TYPE_KEY = "movie_type_key";

    public static final String POP_MOVIE_TITLE = "Pop Movies";
    public static final String HIGHTEST_RATED_MOVIE_TITLE = "Hightest Rated Movies";
}
