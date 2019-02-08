package com.example.android.schoolfinder;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class App extends Application {

    public App() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
