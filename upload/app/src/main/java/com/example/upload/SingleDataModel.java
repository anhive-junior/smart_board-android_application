package com.example.upload;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class SingleDataModel implements Parcelable{
    String name;
    String filepath;
    String thumbpath;
    Bitmap thumb;

    public SingleDataModel(Parcel in){
        this.name = in.readString();
        this.filepath = in.readString();
        this.thumbpath = in.readString();
    }

    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(name);
        dest.writeString(filepath);
        dest.writeString(thumbpath);
    }

    public int describeContents(){
        return 0;
    }

    public static final Parcelable.Creator<SingleDataModel> CREATOR
            = new Parcelable.Creator<SingleDataModel>(){
        public SingleDataModel createFromParcel(Parcel in){
            return new SingleDataModel(in);
        }
        public SingleDataModel[] newArray(int size){
            return new SingleDataModel[size];
        }
    };

    public SingleDataModel(){

    }


}
