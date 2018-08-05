package com.dekar.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Movie implements Parcelable {

    String posterURL;
    String id;
    String title;
    String release_date;
    double vote_average;
    String overview;

    ArrayList<String> reviews;
    ArrayList<Trailer> trailers;

    public Movie()
    {
        this.id = "default";
        reviews = new ArrayList<>();
        trailers = new ArrayList<>();
    }

    public Movie(String id){
        this.id = id;
        reviews = new ArrayList<>();
        trailers = new ArrayList<>();
    }

    protected Movie(Parcel in) {
        posterURL = in.readString();
        id = in.readString();
        title = in.readString();
        release_date = in.readString();
        vote_average = in.readDouble();
        overview = in.readString();
        reviews = in.createStringArrayList();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(posterURL);
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(release_date);
        dest.writeDouble(vote_average);
        dest.writeString(overview);

        if (reviews != null) {
            String[] reviewArray = new String[reviews.size()];
            reviewArray = reviews.toArray(reviewArray);
            dest.writeStringArray(reviewArray);
        }

        if (trailers != null) {
            Trailer[] trailerArray = new Trailer[trailers.size()];
            trailerArray = trailers.toArray(trailerArray);
            dest.writeParcelableArray(trailerArray, flags);
        }


    }
}
