package com.example.android.schoolfinder.normalUsers;

import android.app.Application;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.example.android.schoolfinder.Constants.BundleConstants;
import com.example.android.schoolfinder.Constants.FirebaseConstants;
import com.example.android.schoolfinder.Models.Users;
import com.example.android.schoolfinder.normalUsers.Activities.AuthenticationViewPagerActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.fabric.sdk.android.Fabric;

public class App extends Application {

    private static final String TAG = App.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference dbRef =
                    FirebaseDatabase
                            .getInstance()
                            .getReference(FirebaseConstants.NORMAL_USERS_NODE)
                            .child(currentUser.getUid())
                            .child(FirebaseConstants.USER_DETAIL_NODE);
            Log.e(TAG, dbRef.toString());

            dbRef
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.e(TAG, "onDataChange called");
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra(BundleConstants.USER_BUNDLE, dataSnapshot.getValue(Users.class));
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

        } else {
            Log.e(TAG, "Current user is null");
            Intent intent = new Intent(getApplicationContext(), AuthenticationViewPagerActivity.class);
            startActivity(intent);
        }
    }
}
