package com.pong.moviespopcorn.fragment;


import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pong.moviespopcorn.MainActivity;
import com.pong.moviespopcorn.MoviesPopcorn;
import com.pong.moviespopcorn.R;
import com.pong.moviespopcorn.adapter.GridImageAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import util.AlertDialogManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDiscoverFragment extends Fragment {

    private GridView gridView;
    private GridImageAdapter adapter;
    private JSONArray moviesJsonArray;

    public MovieDiscoverFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discover_movie,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        gridView = (GridView) view.findViewById(R.id.grid_view);

        if(savedInstanceState == null){
            requestMovieAPI(MoviesPopcorn.MOST_POPULAR_URL);
        }
    }

    public void callPopularMovie(){
        requestMovieAPI(MoviesPopcorn.MOST_POPULAR_URL);
    }
    public void callHightestRatedMovie(){
        requestMovieAPI(MoviesPopcorn.HIGHTEST_RATED_URL);
    }

    private void requestMovieAPI(final String url) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        final ProgressBar progressBar = (ProgressBar) getActivity().findViewById(R.id.progress_api_bar);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        if(!response.isNull("results")){
                            try{
                                JSONArray jsonArray = response.getJSONArray("results");
                                setupView(jsonArray);

                            }catch (JSONException e){
                                AlertDialogManager.getInstance().alert(getActivity(), e.toString());
                            }
                        }else{
                            return ;
                        }
                    }
                }, new APIErrorListener(progressBar));

        queue.add(request);
    }

    private void setupView(JSONArray jsonArray){
        if(jsonArray == null) return;

        moviesJsonArray = jsonArray;

        int columnWidth = (int) (getScreenWidth() / 2);

        adapter = new GridImageAdapter(getActivity(), jsonArray, columnWidth);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivity) getActivity()).openMovieDetail(adapter.getItem(position));
            }
        });
    }

    private int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (java.lang.NoSuchMethodError ignore) { // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        columnWidth = point.x;
        return columnWidth;
    }

    class APIErrorListener implements Response.ErrorListener{
        private ProgressBar _progressBar;

        public APIErrorListener(){}

        public APIErrorListener(ProgressBar progressBar){
            this._progressBar = progressBar;
        }
        @Override
        public void onErrorResponse(VolleyError error) {
            if(_progressBar != null){
                _progressBar.setVisibility(View.GONE);
            }

            if(error instanceof NoConnectionError)
                AlertDialogManager.getInstance().alert(getActivity(), "No Internet Conection");
            else
                AlertDialogManager.getInstance().alert(getActivity(), error.toString());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(MoviesPopcorn.MOVIE_LIST_KEY,moviesJsonArray.toString());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null){
            try{
                JSONArray jsonArray = new JSONArray(savedInstanceState.getString(MoviesPopcorn.MOVIE_LIST_KEY));
                setupView(jsonArray);
            }catch (JSONException e){
                AlertDialogManager.getInstance().alert(getActivity(), e.toString());
            }
        }
    }


}
