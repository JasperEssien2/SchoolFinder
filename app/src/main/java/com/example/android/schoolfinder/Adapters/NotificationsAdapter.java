package com.example.android.schoolfinder.Adapters;

import android.app.Notification;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationsViewHolder> {
    List<Notification> notificationList;

    public NotificationsAdapter() {
        super();
    }

    @NonNull
    @Override
    public NotificationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return notificationList == null ? 0: notificationList.size();
    }

    public class NotificationsViewHolder extends RecyclerView.ViewHolder{
        public NotificationsViewHolder(View itemView) {
            super(itemView);
        }
    }
}
