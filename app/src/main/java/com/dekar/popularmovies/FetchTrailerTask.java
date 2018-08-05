package com.dekar.popularmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by guo7711 on 1/19/2016.
 */
public class FetchTrailerTask extends AsyncTask<DetailActivityFragment.TrailerTaskParams, Void, ArrayList<Trailer>> {
    private Context mContext;
    private View rootView;
    Movie selectedMovie = new Movie();
    TrailerAdapter trailerAdapter;

    public FetchTrailerTask(Context context, View rootView) {
        this.mContext = context;
        this.rootView = rootView;
    }

    private String getMovieDbKey()
    {
        return BuildConfig.MOVIE_DB_API_KEY;
    }

    @Override
    protected ArrayList<Trailer> doInBackground(DetailActivityFragment.TrailerTaskParams... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String responseJsonStr = null;

        String movieID = params[0].selectedMovieID;
        trailerAdapter = params[0].reviewTrailerAdapter;

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            //
            //http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=[YOUR API KEY]
            URL url = new URL("http://api.themoviedb.org/3/movie/"+movieID+"/videos?api_key=" + getMovieDbKey());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            responseJsonStr = buffer.toString();

            selectedMovie.trailers =  MovieDataParser.getTrailersByMovieID(movieID, responseJsonStr);


        } catch (IOException e) {
            Log.e("PosterFragment", "Error ", e);
            // If the code didn't successfully get the movie data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PosterFragment", "Error closing stream", e);
                }
            }
        }


        return selectedMovie.trailers;
    }

    @Override
    protected void onPostExecute(ArrayList<Trailer> result_trailers) {

        if (result_trailers != null) {
            trailerAdapter.setTrailers(result_trailers);
            trailerAdapter.notifyDataSetChanged();
            selectedMovie.trailers = result_trailers;
        }


        if (selectedMovie.trailers != null) {
            //ListView reviewListView = (ListView) rootView.findViewById(R.id.reviewList);
            //reviewListView.setAdapter(reviewAdapter);

            LinearLayout trailersLinearLayout = (LinearLayout) rootView.findViewById(R.id.trailersLinearLayout);
            for (int i = 0; i < selectedMovie.trailers.size(); i++) {
                trailersLinearLayout.addView(trailerAdapter.createTrailer(i));
            }

            super.onPostExecute(result_trailers);
        }


    }
}
