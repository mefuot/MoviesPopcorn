package com.pong.moviespopcorn.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.pong.moviespopcorn.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import util.AlertDialogManager;

/**
 * Created by pong on 9/30/15 AD.
 */
public class GridImageAdapter extends BaseAdapter {

    private Activity _activity;
    private JSONArray _jsonArray;
    private int _imageWidth;
    private int _imageHeight;

    private LayoutInflater inflater;

    public GridImageAdapter(Activity activity, JSONArray jsonArray, int imageWidth) {
        this._activity = activity;
        this._jsonArray = jsonArray;
        this._imageWidth = imageWidth;
        _imageHeight = (imageWidth*278)/185;

        inflater = LayoutInflater.from(_activity);
    }

    @Override
    public int getCount() {
        return this._jsonArray.length();
    }

    @Override
    public JSONObject getItem(int position) {
        try {
            return this._jsonArray.getJSONObject(position);
        }catch (JSONException e){
            AlertDialogManager.getInstance().alert(_activity, e.toString());
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.view_poster,null);

            holder.imageView = (ImageView) convertView.findViewById(R.id.image_poster);
            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progress_bar);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        setupViewHolderData(holder,position);

        return convertView;
    }

    private void setupViewHolderData(final ViewHolder holder,int position){

        holder.imageView.setLayoutParams(new RelativeLayout.LayoutParams(_imageWidth, _imageHeight));
        holder.progressBar.setVisibility(View.VISIBLE);

        try {
            String imgUrl = "http://image.tmdb.org/t/p/w185/"+getItem(position).getString("poster_path");

            if(!getItem(position).isNull("poster_path")){
                Picasso.with(_activity).load(imgUrl).resize(_imageWidth, _imageHeight)
                        .into(holder.imageView, new Callback() {
                            @Override
                            public void onSuccess() {
                                holder.progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {
                                holder.progressBar.setVisibility(View.GONE);
                                Picasso.with(_activity).load(R.mipmap.no_image).into(holder.imageView);
                            }
                        });
            }else{
                holder.progressBar.setVisibility(View.GONE);
                Picasso.with(_activity).load(R.mipmap.no_image).into(holder.imageView);
            }

        }catch (JSONException e){
            AlertDialogManager.getInstance().alert(_activity, e.toString());
        }
    }

    private static class ViewHolder{
        public ImageView imageView;
        public ProgressBar progressBar;
    }
}
