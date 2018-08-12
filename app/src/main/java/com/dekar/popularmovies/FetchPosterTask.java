package com.dekar.popularmovies;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.dekar.popularmovies.provider.MoviesContract;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FetchPosterTask extends AsyncTask<String, Void, ArrayList<Movie>> {

    private Context mContext;
    private View rootView;
    private PosterAdapter posterAdapter;

    ArrayList<Movie> movies = new ArrayList<>();


    public FetchPosterTask(Context context, View rootView, PosterAdapter adapter){
        this.mContext=context;
        this.rootView=rootView;
        this.posterAdapter = adapter;
    }

    private String getMovieDbKey()
    {
        return BuildConfig.MOVIE_DB_API_KEY;
    }



    protected Movie fetchPoster(String movieId)
    {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String responseJsonStr = null;

        try {

            URL url = new URL("http://api.themoviedb.org/3/movie/"+movieId+"?api_key=" +getMovieDbKey());

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

            //Log.e("Doinbackground", responseJsonStr);

            return MovieDataParser.getMovieByID(movieId, responseJsonStr);


        } catch (IOException e) {
            Log.e("PosterFragment", "Error " + e.getMessage());
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


    }

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String responseJsonStr = null;
        String order_by = params[0];


// THere is a template for further  so it's broken at the moment.
        if (order_by.equals("favourite")) {

            ContentResolver resolver = mContext.getContentResolver();

            Cursor cursor = resolver.query(MoviesContract.Movies.CONTENT_URI,null,null,null, null);
            int columnId = cursor.getColumnIndex(MoviesContract.MoviesColumns.MOVIE_ID);

            if(null != cursor && cursor.getCount() > 0)
            {
                while (cursor.moveToNext())
                {
                    movies.add(fetchPoster( String.valueOf(cursor.getInt(columnId))));
                }

                cursor.close();

            } else {
                // Unable to start Toast from here
//                Toast toast = Toast.makeText(mContext, "No favorite movies selected", Toast.LENGTH_SHORT);
//                toast.show();
            }

        }
        else {
            try {


                URL url = new URL("http://api.themoviedb.org/3/movie/" + order_by + "?api_key=" + getMovieDbKey());

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
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                responseJsonStr = buffer.toString();


                movies = MovieDataParser.getMovies(responseJsonStr);

            } catch (IOException e) {
                Log.e("PosterFragment", "Error " + e.getMessage());
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
        }
        return movies;
    }



    @Override
    protected void onPostExecute(ArrayList<Movie> result) {

        if (result != null) {
            posterAdapter.setMovies(result);
            posterAdapter.notifyDataSetChanged();
            movies = result;
        }


        if (movies != null)
        {
            GridView gridView = (GridView) rootView.findViewById(R.id.movie_grid);
            gridView.setAdapter(posterAdapter);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    String selectedMovieID = (movies.get(position)).id;
                    Activity activity = (Activity) mContext;

                    // removeAllViews() is not supported in AdapterView
                    //        at android.widget.AdapterView.removeAllViews(AdapterView.java:545)
//                    TransitionManager.go(
//                            Scene.getSceneForLayout(
//                                    (ViewGroup) activity.findViewById(R.id.movie_grid),
//                                    R.layout.activity_details,
//                                    activity
//
//                            )
//                    );
                    Intent intent = new Intent(activity, DetailActivity.class);
                    intent.putExtra("movie", movies.get(position));
                    activity.startActivity(intent);

                }
            });

        }


        super.onPostExecute(result);
    }


}
