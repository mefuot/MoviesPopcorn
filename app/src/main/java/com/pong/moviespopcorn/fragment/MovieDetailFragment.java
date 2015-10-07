package com.pong.moviespopcorn.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pong.moviespopcorn.MainActivity;
import com.pong.moviespopcorn.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import util.AlertDialogManager;


/**
 * Created by pong on 10/4/15 AD.
 */
public class MovieDetailFragment extends Fragment {
    private static final String KEY_JSON = "key_json";
    private JSONObject _jsonObject;

    public static MovieDetailFragment newInstance(JSONObject jsonObject){
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_JSON,jsonObject.toString());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if(bundle != null) {
            try {
                _jsonObject = new JSONObject(bundle.getString(KEY_JSON));
            }catch (JSONException e){
                AlertDialogManager.getInstance().alert(getActivity(), e.toString());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        ((MainActivity)getActivity()).showBackOnActionBar();
        setupMovieData(view);

        super.onViewCreated(view, savedInstanceState);
    }

    private void setupMovieData(View view) {

        TextView titleTextView = (TextView) view.findViewById(R.id.text_original_title);
        TextView releaseDateTextView = (TextView) view.findViewById(R.id.text_release_date);
        TextView voteTextView = (TextView) view.findViewById(R.id.text_vote_average);
        TextView overviewTextView = (TextView) view.findViewById(R.id.text_overview);

        ImageView posterImageView = (ImageView) view.findViewById(R.id.img_poster);

        try {
            titleTextView.setText(_jsonObject.getString("original_title"));
            voteTextView.setText(_jsonObject.getString("vote_average")+"/10");
            overviewTextView.setText(_jsonObject.getString("overview"));

            if(!_jsonObject.isNull("release_date")){
                String date = _jsonObject.getString("release_date");
                String[] str = date.split("-");
                releaseDateTextView.setText(str[0]);
            }else{
                releaseDateTextView.setText("Unknow");
            }

            if(!_jsonObject.isNull("poster_path")){
                String imgUrl = "http://image.tmdb.org/t/p/w185/"+_jsonObject.getString("poster_path");
                Picasso.with(getActivity()).load(imgUrl).into(posterImageView);
            }else{
                Picasso.with(getActivity()).load(R.mipmap.no_image).into(posterImageView);
            }

        }catch (JSONException e){
            AlertDialogManager.getInstance().alert(getActivity(), e.toString());
        }
    }

    @Override
    public void onDetach() {
        ((MainActivity)getActivity()).hideBackOnActionBar();
        super.onDetach();
    }
}
