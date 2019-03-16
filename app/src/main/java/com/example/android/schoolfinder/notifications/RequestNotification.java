package com.example.android.schoolfinder.notifications;

import com.google.gson.annotations.SerializedName;

public class RequestNotification {

    @SerializedName("to") //  "to" changed to token
    private String token;

    @SerializedName("data")
    private Config sendNotificationModel;

    public Config getSendNotificationModel() {
        return sendNotificationModel;
    }

    public void setSendNotificationModel(Config sendNotificationModel) {
        this.sendNotificationModel = sendNotificationModel;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
