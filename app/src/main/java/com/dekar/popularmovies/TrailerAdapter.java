package com.dekar.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TrailerAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Trailer> trailers;
    private LayoutInflater inflater;

    public void setTrailers (ArrayList<Trailer> result){
        trailers = result;
    }

    @Override
    public int getCount() {
        return trailers.size();
    }

    @Override
    public Object getItem(int position) {
        return trailers.get(position);
    }

    public TrailerAdapter(Context c, ArrayList<Trailer> list){
        mContext = c;
        trailers = list;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View createTrailer(final int position)
    {
        DetailViewHolder holder = new DetailViewHolder();
        holder.trailerTextView = new TextView(mContext);
        holder.trailerTextView.setClickable(true);
        holder.trailerTextView.setPadding(0, 0, 0, 0);
        holder.trailerTextView.setTextSize(16);

        holder.trailerTextView.setText(trailers.get(position).name);
        holder.trailerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + trailers.get(position).key));
                if (mContext!=null) {
                    mContext.startActivity(Intent.createChooser(intent, "Choose an App"));
                }
                else Log.e("createTrailer", "mContext is null");
            }
        });

        return holder.trailerTextView;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            textView = new TextView(mContext);
            textView.setPadding(0, 0, 0, 0);
        } else {
            textView = (TextView) convertView;
        }

        return textView;

    }
}
