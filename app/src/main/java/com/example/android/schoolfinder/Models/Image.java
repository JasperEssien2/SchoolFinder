package com.example.android.schoolfinder.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Image implements Parcelable {
    private String id;
    private String imageUrl;
    private String imageTag;

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    public Image() {

    }

    public Image(String id, String imageUrl, String imageTag) {

        this.id = id;
        this.imageUrl = imageUrl;
        this.imageTag = imageTag;
    }

    protected Image(Parcel in) {
        id = in.readString();
        imageUrl = in.readString();
        imageTag = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageTag() {
        return imageTag;
    }

    public void setImageTag(String imageTag) {
        this.imageTag = imageTag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(imageUrl);
        parcel.writeString(imageTag);
    }

    @Override
    public String toString() {
        return "image id --- " + id + ", imageUrl -- " + imageUrl + ", imageTag -- " + imageTag + "\n";
    }
}
