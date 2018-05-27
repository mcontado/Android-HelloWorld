package com.example.myfirstapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class MatchesModel implements Parcelable  {
    public String imageUrl;
    public boolean liked;
    public String name;
    public String uid;
    public String lat;
    public String longitude;

    public MatchesModel() {
     //Default constructor required
    }

    public MatchesModel(Parcel in) {
        imageUrl = in.readString();
        liked = in.readByte() != 0;
        name = in.readString();
        lat = in.readString();
        longitude = in.readString();
    }

    public static final Creator<MatchesModel> CREATOR = new Creator<MatchesModel>() {
        @Override
        public MatchesModel createFromParcel(Parcel source) {
            return new MatchesModel(source);
        }

        @Override
        public MatchesModel[] newArray(int size) {
            return new MatchesModel[size];
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
        dest.writeString(lat);
        dest.writeString(longitude);
    }
}
