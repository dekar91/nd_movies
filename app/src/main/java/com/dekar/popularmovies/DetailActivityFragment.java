package com.dekar.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.Set;


public class DetailActivityFragment extends Fragment {

    Movie selectedMovie = new Movie();
    ReviewAdapter reviewAdapter;
    TrailerAdapter trailerAdapter;
    SharedPreferences sp;
    View rootView;

    public DetailActivityFragment() {
    }

    public static class ReviewTaskParams{

        ReviewAdapter reviewAdapter;
        String selectedMovieID;

        ReviewTaskParams(ReviewAdapter adapter, String id){
            reviewAdapter = adapter;
            selectedMovieID = id;
        }
    }

    public static class TrailerTaskParams{
        TrailerAdapter reviewTrailerAdapter;
        String selectedMovieID;

        TrailerTaskParams(TrailerAdapter adapter, String id){
            reviewTrailerAdapter = adapter;
            selectedMovieID = id;
        }

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        sp = getContext().getSharedPreferences("pref_general", Context.MODE_PRIVATE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelable("movie", selectedMovie);
        super.onSaveInstanceState(outState);
    }

    public Boolean checkFavouriteByID(String MovieID){

        Set<String> retrive_set = sp.getStringSet("favourite", null);

        if (retrive_set != null) {
            String[] array = new String[retrive_set.size()];
            retrive_set.toArray(array);

            if (retrive_set.contains(MovieID)) {
                return true;
            } else {
                return false;
            }
        }
        else return false;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        reviewAdapter = new ReviewAdapter(getActivity(), selectedMovie.reviews);
        trailerAdapter = new TrailerAdapter(getActivity(), selectedMovie.trailers);

        DetailViewHolder detailViewHolder = new DetailViewHolder(rootView);

        if (savedInstanceState != null) {
            selectedMovie = savedInstanceState.getParcelable("movie");

            //update UI
            if (selectedMovie != null) {
                detailViewHolder.titleTextView.setText(selectedMovie.title);
                detailViewHolder.releasedateTextView.setText(selectedMovie.release_date);
                detailViewHolder.voteTextView.setText(String.valueOf(selectedMovie.vote_average + "/10"));
                detailViewHolder.overViewTextView.setText(selectedMovie.overview);
                Picasso.with(this.getContext()).load("http://image.tmdb.org/t/p/w185/"+ selectedMovie.posterURL).into(detailViewHolder.imageView);
            }

            //review
            reviewAdapter.setReviews(selectedMovie.reviews);
            reviewAdapter.notifyDataSetChanged();

            if (selectedMovie.reviews != null) {
                LinearLayout reviewsLinearLayout = (LinearLayout) rootView.findViewById(R.id.reviewsLinearLayout);
                for (int i = 0; i < selectedMovie.reviews.size(); i++) {
                    reviewsLinearLayout.addView(reviewAdapter.createReview(i));
                }
            }

            //trailer
            trailerAdapter.setTrailers(selectedMovie.trailers);
            trailerAdapter.notifyDataSetChanged();

            if (selectedMovie.trailers != null) {
                LinearLayout trailersLinearLayout = (LinearLayout) rootView.findViewById(R.id.trailersLinearLayout);
                for (int i = 0; i < selectedMovie.trailers.size(); i++) {
                    trailersLinearLayout.addView(trailerAdapter.createTrailer(i));
                }
            }
        }


        else{
            Intent intent = getActivity().getIntent();
            if (intent != null && intent.hasExtra("movie")) {

                selectedMovie = intent.getExtras().getParcelable("movie");

                String selectedMovieID = selectedMovie.id;

                if (selectedMovie != null) {
                    detailViewHolder.titleTextView.setText(selectedMovie.title);
                    detailViewHolder.releasedateTextView.setText(selectedMovie.release_date);
                    detailViewHolder.voteTextView.setText(String.valueOf(selectedMovie.vote_average + "/10"));
                    detailViewHolder.overViewTextView.setText(selectedMovie.overview);
                    Picasso.with(this.getContext()).load(selectedMovie.posterURL).into(detailViewHolder.imageView);
                }

                FetchReviewTask fetchReviewTask = new FetchReviewTask(getActivity(), rootView);
                fetchReviewTask.execute(new ReviewTaskParams(reviewAdapter, selectedMovieID));

                FetchTrailerTask fetchTrailerTask = new FetchTrailerTask(getActivity(), rootView);
                fetchTrailerTask.execute(new TrailerTaskParams(trailerAdapter, selectedMovieID));


            }
        }



        final Button button = (Button) rootView.findViewById(R.id.button);

        if (checkFavouriteByID(selectedMovie.id)) {
            // Log.e("onCreateView", "TRUE");
            button.setText("Unmark as favourite");
        }
        else
        {
            //Log.e("onCreateView", "FALSE");
            button.setText("Mark as favourite");
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkFavouriteByID(selectedMovie.id)) {

                    //SharedPreferences sp = getContext().getSharedPreferences("pref_general", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();

                    Set<String> set = new HashSet<String>();
                    set = sp.getStringSet("favourite", null);
                    set.remove(selectedMovie.id);
                    editor.putStringSet("favourite", set);
                    editor.commit();

                    Toast.makeText(getContext(), "Unmarked as favourite!", Toast.LENGTH_SHORT).show();
                    button.setText("Mark as favourite");

                } else {
                    //SharedPreferences sp = getContext().getSharedPreferences("pref_general", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();

                    Set<String> set = new HashSet<String>();
                    set = sp.getStringSet("favourite", null);
                    set.add(selectedMovie.id);
                    editor.putStringSet("favourite", set);
                    editor.commit();

                    Toast.makeText(getContext(), "Marked as favourite!", Toast.LENGTH_SHORT).show();
                    button.setText("Unmark as favourite");
                }

            }
        });



        return rootView;

    }
}
