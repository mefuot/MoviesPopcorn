package com.pong.moviespopcorn;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pong.moviespopcorn.fragment.MovieDetailFragment;
import com.pong.moviespopcorn.fragment.MovieDiscoverFragment;

import org.json.JSONObject;




public class MainActivity extends AppCompatActivity {

    private MovieDiscoverFragment movieDiscoverFragment;

    private TextView toolbarTitleTextView;
    private ProgressBar progressBar;

    private String currentMovieType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){
            currentMovieType = MoviesPopcorn.POP_MOVIE_TITLE;

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.addToBackStack(null);
            movieDiscoverFragment = new MovieDiscoverFragment();
            ft.add(R.id.container, movieDiscoverFragment,"MovieDiscoverFragment");
            ft.commit();
        }else{
            movieDiscoverFragment = (MovieDiscoverFragment)getSupportFragmentManager()
                    .findFragmentByTag("MovieDiscoverFragment");
            currentMovieType = savedInstanceState.getString(MoviesPopcorn.MOVIE_TYPE_KEY);
        }

        setupToolBar();
    }

    private void setupToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitleTextView = (TextView) findViewById(R.id.toolbar_title);
        progressBar = (ProgressBar)findViewById(R.id.progress_api_bar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbarTitleTextView.setText(currentMovieType);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(movieDiscoverFragment != null) {
            if (id == R.id.sort_by_most_popular) {
                if(currentMovieType == MoviesPopcorn.POP_MOVIE_TITLE) return true;

                currentMovieType = MoviesPopcorn.POP_MOVIE_TITLE;
                toolbarTitleTextView.setText(currentMovieType);
                progressBar.setVisibility(View.VISIBLE);

                movieDiscoverFragment.callPopularMovie();

                return true;
            } else if (id == R.id.sort_by_hightest_rated) {
                if(currentMovieType == MoviesPopcorn.HIGHTEST_RATED_MOVIE_TITLE) return true;

                currentMovieType = MoviesPopcorn.HIGHTEST_RATED_MOVIE_TITLE;
                toolbarTitleTextView.setText(currentMovieType);
                progressBar.setVisibility(View.VISIBLE);

                movieDiscoverFragment.callHightestRatedMovie();

                return true;
            }
        }else return true;

        return super.onOptionsItemSelected(item);
    }

    public void showBackOnActionBar(){
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void hideBackOnActionBar(){
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    public void openMovieDetail(JSONObject jsonObject){
        try {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.addToBackStack(null);
            MovieDetailFragment fragment = new MovieDetailFragment().newInstance(jsonObject);
            ft.add(R.id.container,fragment);
            ft.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if( fm.getBackStackEntryCount() > 1){
            fm.popBackStack();
        }else{
            showPopupConfirmExit();
        }
    }

    private void showPopupConfirmExit(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Exit Application?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Cancel
            }
        });
        alert.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(MoviesPopcorn.MOVIE_TYPE_KEY,currentMovieType);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null){
            toolbarTitleTextView.setText(savedInstanceState.getString(MoviesPopcorn.MOVIE_TYPE_KEY));
        }
    }
}
