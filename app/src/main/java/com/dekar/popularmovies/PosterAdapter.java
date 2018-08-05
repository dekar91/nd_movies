package com.dekar.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by guo7711 on 10/15/2015.
 */
public class PosterAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Movie> movies;
    private LayoutInflater inflater;


    public void setMovies(ArrayList<Movie> result)
    {
        movies = result;
    }

    public PosterAdapter(Context c, ArrayList<Movie> list) {
        mContext = c;
        movies = list;
        inflater = LayoutInflater.from(mContext);
    }

    public int getCount() {
        return movies.size();
    }

    public Object getItem(int position) {
        return movies.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        PosterViewHolder holder;
        holder = new PosterViewHolder();
        //ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            holder.imageView = new ImageView(mContext);
            holder.imageView.setLayoutParams(new GridView.LayoutParams(540, 810));
            holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.imageView.setPadding(0, 0, 0, 0);

            /*
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(540, 810));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);*/
        } else {
            holder.imageView = (ImageView) convertView;
        }

        Picasso.with(mContext).load(movies.get(position).posterURL).into(holder.imageView);
        //Picasso.with(mContext).load(movies.get(position).posterURL).into(imageView);
        //Log.e("PosterAdapter",String.valueOf(movies.size()));
        return holder.imageView;


    }



}
