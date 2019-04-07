package com.example.android.schoolfinder.notifications;

import com.google.gson.annotations.SerializedName;

public class RequestNotification {

    @SerializedName("to") //  "to" changed to token
    private String token;

    @SerializedName("content_available")
    private boolean contentAvailable;

    @SerializedName("priority")
    private String priority;

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

    public boolean isContentAvailable() {
        return contentAvailable;
    }

    public void setContentAvailable(boolean contentAvailable) {
        this.contentAvailable = contentAvailable;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
