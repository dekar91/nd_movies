package com.dekar.popularmovies;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;
import java.util.ArrayList;


public class PosterFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    private PosterAdapter posterAdapter;
    private View rootView;
    public static int scrollPosition;
    ArrayList<Movie> movies = new ArrayList<>();


    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this.getActivity()).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if(s.equals(getString(R.string.pref_order_key)))
        {
            updateMovies(rootView, posterAdapter);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        PreferenceManager.getDefaultSharedPreferences(this.getActivity()).registerOnSharedPreferenceChangeListener(this);

        if(scrollPosition > 0){
            GridView t  = (GridView) getActivity().findViewById(R.id.movie_grid);
            t.smoothScrollToPosition(scrollPosition);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelableArrayList("movies", movies);
        super.onSaveInstanceState(outState);

//        GridView t  = (GridView) getActivity().findViewById(R.id.movie_grid);
//        scrollPosition = t.getFirstVisiblePosition();
    }

    @Override
    public void onPause() {
        super.onPause();

//        GridView t  = (GridView) getActivity().findViewById(R.id.movie_grid);
//        scrollPosition = t.getFirstVisiblePosition();

    }

    @Override
    public void onResume() {
        super.onResume();
        updateMovies(rootView, posterAdapter);

        if(scrollPosition > 0){
            GridView t  = (GridView) getActivity().findViewById(R.id.movie_grid);
            t.smoothScrollToPosition(scrollPosition);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
           Intent intent = new Intent(getActivity(), SettingsActivity.class);startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateMovies(View rootView, PosterAdapter posterAdapter){
        FetchPosterTask movieTask = new FetchPosterTask(getActivity(), rootView, posterAdapter);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String order_by = prefs.getString(getString(R.string.pref_order_key), getString(R.string.pref_order_popularity));
        movieTask.execute(order_by);

        if(scrollPosition > 0){
            GridView t  = (GridView) getActivity().findViewById(R.id.movie_grid);
            t.smoothScrollToPosition(scrollPosition);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        posterAdapter = new PosterAdapter(getActivity(), movies);
        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ConnectivityManager cm =
                (ConnectivityManager)(getActivity().getSystemService(Context.CONNECTIVITY_SERVICE));

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();


        if (!isConnected) {
            Toast toast = Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
                updateMovies(rootView, posterAdapter);
        }

        if(scrollPosition > 0){
            GridView t  = (GridView) getActivity().findViewById(R.id.movie_grid);
            t.smoothScrollToPosition(scrollPosition);
        }

        return rootView;
    }
}
