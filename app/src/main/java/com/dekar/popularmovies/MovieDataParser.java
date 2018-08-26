package com.dekar.popularmovies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieDataParser {


    public static Movie getMovieByID(String ID, String movieJsonStr)
    {
        Movie m = new Movie(ID);
        try {
            JSONObject onemovie = new JSONObject(movieJsonStr);
            String posterpath = onemovie.getString("poster_path");
            m.posterURL = "http://image.tmdb.org/t/p/w185/" + posterpath;
            String releasedate = onemovie.getString("release_date");
            m.release_date = releasedate;
            String title = onemovie.getString("title");
            m.title = title;
            double vote = onemovie.getDouble("vote_average");
            m.vote_average = vote;
            String overview = onemovie.getString("overview");
            m.overview = overview;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return m;
    }

    public static ArrayList<Trailer> getTrailersByMovieID (String MovieID, String responseJsonStr){

        ArrayList<Trailer> trailers = new ArrayList<Trailer>();

        try {
            JSONArray trailersArray = new JSONObject(responseJsonStr).getJSONArray("results");

            for (int i = 0; i < trailersArray.length(); i++){
                JSONObject onetrailercontent = trailersArray.getJSONObject(i);
                String onetrailername = onetrailercontent.getString("name");
                String onetrailerkey = onetrailercontent.getString("key");
                trailers.add(new Trailer(onetrailername, onetrailerkey));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return trailers;
    }

    public static ArrayList<String> getReviewsByMovieID(String MovieID, String responseJsonStr){

        ArrayList<String> reviews = new ArrayList<>();

        //only get results in page 1
        try {
            JSONArray pageOneResults = new JSONObject(responseJsonStr).getJSONArray("results");

            for (int i = 0; i < pageOneResults.length(); i++){
                JSONObject onereviewcontent = pageOneResults.getJSONObject(i);
                String onecontent = onereviewcontent.getString("content");
                reviews.add(onecontent);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reviews;

    }

    public static String[] getPosterPaths(String movieJsonStr)throws JSONException {
        JSONArray movies = new JSONObject(movieJsonStr).getJSONArray("results");
        ArrayList<String> posters = new ArrayList<String>();

        for (int i = 0; i < movies.length(); i++){
            JSONObject onemovie = movies.getJSONObject(i);
            String posterpath = onemovie.getString("poster_path");
            posters.add(posterpath);
        }

        String[] posterArr = new String[posters.size()];
               posterArr =  posters.toArray(posterArr);
        return posterArr;
    }

    public static ArrayList<Movie> getMovies(String movieJsonStr){

        ArrayList<Movie> movieArray = new ArrayList<Movie>();

        try {

            JSONArray movies = new JSONObject(movieJsonStr).getJSONArray("results");

            for (int i = 0; i < movies.length(); i++) {
                JSONObject onemovie = null;
                onemovie = movies.getJSONObject(i);
                String posterpath = "http://image.tmdb.org/t/p/w185/" + onemovie.getString("poster_path");
                String id = onemovie.getString("id");
                String overview = onemovie.getString("overview");
                String title = onemovie.getString("title");
                String date = onemovie.getString("release_date");
                Double vote = onemovie.getDouble("vote_average");


                Movie movieObj = new Movie(id);
                movieObj.posterURL = posterpath;
                movieObj.overview = overview;
                movieObj.title = title;
                movieObj.release_date = date;
                movieObj.vote_average = vote;


                movieArray.add(movieObj);

            }


        }

        catch (JSONException e) {
            e.printStackTrace();
        }

        return movieArray;
    }

}
