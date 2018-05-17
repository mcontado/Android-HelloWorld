package com.example.myfirstapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Matches implements Parcelable  {
    public String imageUrl;
    public boolean liked;
    public String name;
    public String uid;

    public Matches() {
     //Default constructor required
    }

    public Matches(Parcel in) {
        imageUrl = in.readString();
        liked = in.readByte() != 0;
        name = in.readString();
    }

    public static final Creator<Matches> CREATOR = new Creator<Matches>() {
        @Override
        public Matches createFromParcel(Parcel source) {
            return new Matches(source);
        }

        @Override
        public Matches[] newArray(int size) {
            return new Matches[size];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUrl);
        dest.writeByte((byte) (liked ? 1 : 0));
        dest.writeString(name);
    }
}
