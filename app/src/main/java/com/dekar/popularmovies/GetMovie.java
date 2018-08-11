package com.dekar.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class GetMovie extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String ...orders) {
        return this.getMovie(orders[0]);
    }


    private String getMovieDbKey()
    {
        return BuildConfig.MOVIE_DB_API_KEY;
    }

    private URL buildUrl(String order) {
        
        String urlString = "https://api.themoviedb.org/3/movie/";

        switch (order)
        {
            case "vote_average":
                urlString += "top_rated";
                break;
            case "favorite":
                urlString += "top_rated";
                break;
            case "popularity.desc":
                default:
                    urlString += "top_rated";
                    break;
        }
        
       urlString += "?api_key=" + getMovieDbKey();
        Uri buildUri = Uri.parse(urlString);

        URL url = null;

        try {
            url = new URL(buildUri.toString());
        } catch (Exception e) {

        }

        return url;
    }

    @Override
    protected void onPostExecute(String s) {
        if (s != null && !s.equals("")) {
            Log.d("info", "onPostExecute: " + s);
        }
    }

    private String getMovie(String order) {

        URL url = this.buildUrl(order);

        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }

        } catch (Exception e) {
            Log.e("TASK", e.getMessage());
        }
        return null;
        }

}