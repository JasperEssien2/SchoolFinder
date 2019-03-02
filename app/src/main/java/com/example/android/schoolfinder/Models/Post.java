package com.example.android.schoolfinder.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.Map;

public class Post implements Parcelable {
    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
    private String uid;
    private Object timeStamp;
    private String author;
    private List<Image> imageList;
    private Image schoolLogo;
    private String body;
    private int starCount;
    private Map<String, Boolean> stars;
    private boolean isPromoted;

    public Post() {
    }

    protected Post(Parcel in) {
        uid = in.readString();
        timeStamp = in.readLong();
        author = in.readString();
        imageList = in.createTypedArrayList(Image.CREATOR);
        schoolLogo = in.readParcelable(Image.class.getClassLoader());
        body = in.readString();
        starCount = in.readInt();
        isPromoted = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeValue(timeStamp);
        dest.writeString(author);
        dest.writeTypedList(imageList);
        dest.writeParcelable(schoolLogo, flags);
        dest.writeString(body);
        dest.writeInt(starCount);
        dest.writeByte((byte) (isPromoted ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }

    public Image getSchoolLogo() {
        return schoolLogo;
    }

    public void setSchoolLogo(Image schoolLogo) {
        this.schoolLogo = schoolLogo;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getStarCount() {
        return starCount;
    }

    public void setStarCount(int starCount) {
        this.starCount = starCount;
    }

    public Map<String, Boolean> getStars() {
        return stars;
    }

    public void setStars(Map<String, Boolean> stars) {
        this.stars = stars;
    }

    public Boolean getPromoted() {
        return isPromoted;
    }

    public void setPromoted(Boolean promoted) {
        isPromoted = promoted;
    }

    @Override
    public String toString() {
        return "post Body - " + body + "\nPromoted: " + isPromoted;
    }
}
