package com.dekar.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by guo7711 on 1/7/2016.
 */
public class Trailer implements Parcelable {

    String name;
    String key;

    public Trailer(String name, String key){
        this.name = name;
        this.key = key;
    }

    protected Trailer (Parcel in) {
        name = in.readString();
        key = in.readString();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }


    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(key);
    }
}
