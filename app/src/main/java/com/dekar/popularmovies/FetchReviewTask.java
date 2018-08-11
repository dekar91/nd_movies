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

public class FetchReviewTask extends AsyncTask<DetailActivityFragment.ReviewTaskParams, Void, ArrayList<String>> {
    private Context mContext;
    private View rootView;
    Movie selectedMovie = new Movie();
    ReviewAdapter reviewAdapter;


    public FetchReviewTask(Context context, View rootView) {
        this.mContext = context;
        this.rootView = rootView;
    }

    private String getMovieDbKey()
    {
        return BuildConfig.MOVIE_DB_API_KEY;
    }


    @Override
    protected ArrayList<String> doInBackground(DetailActivityFragment.ReviewTaskParams... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String responseJsonStr = null;

        String movieID = params[0].selectedMovieID;

        reviewAdapter = params[0].reviewAdapter;

        try {

            URL url = new URL("http://api.themoviedb.org/3/movie/"+movieID+"/reviews?api_key=" + getMovieDbKey());

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
                buffer.append(line);
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            responseJsonStr = buffer.toString();

            selectedMovie.reviews =  MovieDataParser.getReviewsByMovieID(movieID, responseJsonStr);


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


        return selectedMovie.reviews;
    }

    @Override
    protected void onPostExecute(ArrayList<String> result_reviews) {

        if (result_reviews != null) {
            reviewAdapter.setReviews(result_reviews);
            reviewAdapter.notifyDataSetChanged();
            selectedMovie.reviews = result_reviews;
        }


        if (selectedMovie.reviews != null) {

            LinearLayout reviewsLinearLayout = (LinearLayout) rootView.findViewById(R.id.reviewsLinearLayout);
            for (int i = 0; i < selectedMovie.reviews.size(); i++) {
                reviewsLinearLayout.addView(reviewAdapter.createReview(i));
            }

            super.onPostExecute(result_reviews);
        }
    }
}
