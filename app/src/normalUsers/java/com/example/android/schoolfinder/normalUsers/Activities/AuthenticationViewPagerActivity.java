package com.example.android.schoolfinder.normalUsers.Activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

//import com.example.android.schoolfinder.Adapters.AuthViewPagerAdapter;
import com.example.android.schoolfinder.Constants.BundleConstants;
import com.example.android.schoolfinder.Constants.FirebaseConstants;
//import com.example.android.schoolfinder.HomeActivity;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.Models.Users;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.databinding.ActivityAuthenticationBinding;
import com.example.android.schoolfinder.interfaces.AuthenticationViewPagerCallbacks;
import com.example.android.schoolfinder.normalUsers.Adapters.AuthViewPagerAdapter;
import com.example.android.schoolfinder.normalUsers.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AuthenticationViewPagerActivity extends AppCompatActivity implements AuthenticationViewPagerCallbacks {
    private static final String TAG = AuthenticationViewPagerActivity.class.getSimpleName();
    private ActivityAuthenticationBinding authenticationBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authenticationBinding = DataBindingUtil.setContentView(this, R.layout.activity_authentication);
        authenticationBinding
                .authViewPager
                .setAdapter(new AuthViewPagerAdapter(getSupportFragmentManager(), this));
        Log.e(TAG, "onCreate() called oooh");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference dbRef =
            FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child(FirebaseConstants.NORMAL_USERS_NODE)
                    .child(currentUser.getUid())
                    .child(FirebaseConstants.USER_DETAIL_NODE);
            Log.e(TAG, dbRef.toString());

                    dbRef
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.e(TAG, "onDataChange called");
                            Intent intent = new Intent(AuthenticationViewPagerActivity.this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra(BundleConstants.USER_BUNDLE, dataSnapshot.getValue(Users.class));
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

        }else{
            Log.e(TAG, "Current user is null");
        }
    }

    @Override
    public void signUpButtonClicked(boolean signUp, @Nullable Users users, @Nullable String password) {
        if (!signUp)
            authenticationBinding.authViewPager.setCurrentItem(2, true);
    }

    @Override
    public void signUpButtonClicked(boolean signUp, @Nullable School users, @Nullable String password) {
        if (!signUp)
            authenticationBinding.authViewPager.setCurrentItem(2, true);
    }

    @Override
    public void loginButtonClicked(boolean login, @Nullable String email, @Nullable String password) {
        if (!login)
            authenticationBinding.authViewPager.setCurrentItem(1, true);
    }

    @Override
    public void previousButtonCLicked() {
        authenticationBinding.authViewPager.setCurrentItem(2);
    }

    @Override
    public void nextButtonCLicked(Users users) {

    }

}
