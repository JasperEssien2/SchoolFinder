package com.example.android.schoolfinder.notifications;

import com.google.gson.annotations.SerializedName;

public class Config {
    @SerializedName("content")
    public String content = "";
    @SerializedName("title")
    public String title = "";
    @SerializedName("imageUrl")
    public String imageUrl = "";
//    public static String gameUrl = "";


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
