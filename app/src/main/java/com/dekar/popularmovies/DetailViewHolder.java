package com.dekar.popularmovies;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by guo7711 on 1/19/2016.
 */
public class DetailViewHolder {

    private View rootView;
    TextView titleTextView;
    TextView releasedateTextView;
    TextView voteTextView;
    TextView overViewTextView;
    ImageView imageView;
    TextView trailerTextView;
    TextView reviewTextView;


    DetailViewHolder(View root){
        this.rootView = root;
        titleTextView = ((TextView) rootView.findViewById(R.id.titleText));
        releasedateTextView = ((TextView) rootView.findViewById(R.id.releasedateText));
        voteTextView = ((TextView) rootView.findViewById(R.id.voteText));
        overViewTextView = ((TextView) rootView.findViewById(R.id.overViewText));
        imageView = ((ImageView) rootView.findViewById(R.id.imageView));

    }

    DetailViewHolder(){
        
    }


}
