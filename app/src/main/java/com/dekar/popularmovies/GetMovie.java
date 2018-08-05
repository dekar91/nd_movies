package com.dekar.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class GetMovie extends AsyncTask<URL, Void, String> {
    @Override
    protected String doInBackground(URL... urls) {
        String result = this.getMovie();
        return result;
    }

    private URL buildUrl() {
        String urlString = "https://api.themoviedb.org/3/movie/550?api_key=d95e80a4df2194278367f995c9355a83";
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

    private String getMovie() {
        URL url = this.buildUrl();

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