package com.dekar.popularmovies;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dekar.popularmovies.provider.MoviesContract;
import com.dekar.popularmovies.provider.MoviesProvider;
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

        ContentResolver resolver = getContext().getContentResolver();

        Cursor cursor = resolver.query(MoviesContract.Movies.CONTENT_URI,null,MoviesContract.MoviesColumns.MOVIE_ID  + " = " + MovieID,null, null);
        return  null != cursor &&  cursor.getCount() > 0;

    }

    public void changeColor(FloatingActionButton button, boolean enabled)
    {
        if(enabled)
        {
            button.setImageResource(R.drawable.ic_favorite);
        } else {
            button.setImageResource(R.drawable.favourite_filled);
        }

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
                getActivity().setTitle(selectedMovie.title);
                detailViewHolder.titleTextView.setText(selectedMovie.title);
                detailViewHolder.releasedateTextView.setText(selectedMovie.release_date);
                detailViewHolder.voteTextView.setText(String.valueOf(selectedMovie.vote_average + "/10"));
                detailViewHolder.overViewTextView.setText(selectedMovie.overview);
                Picasso.with(this.getContext()).load(selectedMovie.posterURL).into(detailViewHolder.imageView);
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
                    getActivity().setTitle(selectedMovie.title);

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



        final FloatingActionButton button = rootView.findViewById(R.id.button);

        changeColor(button, checkFavouriteByID(selectedMovie.id));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContentResolver resolver = getContext().getContentResolver();

                boolean changeColor = checkFavouriteByID(selectedMovie.id);

                if (changeColor) {


                    resolver.delete(MoviesContract.Movies.CONTENT_URI, MoviesContract.MoviesColumns.MOVIE_ID + " =  " + selectedMovie.id,  null);

                   Toast.makeText(getContext(), "Unmarked as favourite!", Toast.LENGTH_SHORT).show();

                } else {
                    ContentValues values = new ContentValues();
                    values.put(MoviesContract.Movies.MOVIE_ID, selectedMovie.id);
                    values.put(MoviesContract.Movies.MOVIE_TITLE, selectedMovie.title);
                    values.put(MoviesContract.Movies.MOVIE_OVERVIEW, selectedMovie.overview);
                    values.put(MoviesContract.Movies.MOVIE_VOTE_AVERAGE, selectedMovie.vote_average);
                    values.put(MoviesContract.Movies.MOVIE_RELEASE_DATE, selectedMovie.release_date);
                    values.put(MoviesContract.Movies.MOVIE_POSTER_PATH, selectedMovie.posterURL);

                    Uri cursor = resolver.insert(MoviesContract.Movies.CONTENT_URI, values);


                    Toast.makeText(getContext(), "Marked as favourite!", Toast.LENGTH_SHORT).show();
//                    button.setText("Unmark as favourite");
                }

                String s  = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(
                        getString(R.string.pref_order_key),
                        getString(R.string.pref_order_popularity)
                );

                if(s.equals(getString(R.string.pref_order_favourite)))
                {
                    new GetMovie().execute(s);
//                    new PosterFragment().updateMovies();
                }

                changeColor(button, checkFavouriteByID(selectedMovie.id));



            }
        });



        return rootView;

    }
}
