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
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import java.util.ArrayList;

/**
 * Created by guo7711 on 10/15/2015.
 */
public class PosterFragment extends Fragment {

    private PosterAdapter posterAdapter;
    private View rootView;
    ArrayList<Movie> movies = new ArrayList<>();


    public PosterFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelableArrayList("movie", movies);
        super.onSaveInstanceState(outState);
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
        String order_by = prefs.getString(getString(R.string.pref_order_key), "popularity.desc");
        movieTask.execute(order_by);
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

            if (savedInstanceState != null) {
                movies = savedInstanceState.getParcelableArrayList("movie");

                // update UI
                posterAdapter.setMovies(movies);
                posterAdapter.notifyDataSetChanged();

                if (movies != null) {
                    GridView gridView = (GridView) rootView.findViewById(R.id.movie_grid);
                    gridView.setAdapter(posterAdapter);

                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View v,
                                                int position, long id) {
                            // Toast.makeText(getActivity(), "" + position,
                            //       Toast.LENGTH_SHORT).show();
                            String selectedMovieID = (movies.get(position)).id;
                            Intent intent = new Intent(getActivity(), DetailActivity.class);
                            intent.putExtra("movie", movies.get(position));
                            startActivity(intent);

                        }
                    });

                }
            } else {
                updateMovies(rootView, posterAdapter);
            }
        }


        return rootView;
    }
}
