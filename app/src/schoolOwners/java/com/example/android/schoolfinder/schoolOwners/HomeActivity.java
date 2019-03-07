package com.example.android.schoolfinder.schoolOwners;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.android.schoolfinder.Constants.BundleConstants;
import com.example.android.schoolfinder.FirebaseHelper.Authentication;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.Models.Users;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.databinding.ActivityHomeBinding;
import com.example.android.schoolfinder.interfaces.AuthenticationCallbacks;
import com.example.android.schoolfinder.schoolOwners.Activities.AuthenticationViewPagerActivity;
import com.example.android.schoolfinder.schoolOwners.Activities.SettingsViewPagerActivity;
import com.example.android.schoolfinder.schoolOwners.Fragments.ActivitiesFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity implements AuthenticationCallbacks {

    private static final String TAG = HomeActivity.class.getSimpleName();
    ActivityHomeBinding homeBinding;
    private School mSchool;
    private Authentication authentication;

    public HomeActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        setSupportActionBar(homeBinding.toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setElevation(8.0f);

        authentication = new Authentication(this);
//        FlowTextView flowTextView;
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            authentication.getUserDetail(FirebaseAuth.getInstance().getCurrentUser().getUid(), true);
        homeBinding.buttonSettings
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(HomeActivity.this,
                                SettingsViewPagerActivity.class);
                        intent.putExtra(BundleConstants.SCHOOL_BUNDLE, mSchool);
                        startActivity(intent);
                    }
                });
        homeBinding.buttonActivities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.activity_home, new ActivitiesFragment())
                        .commit();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                authentication.logout();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void login(boolean loggedInSuccessful, FirebaseUser user) {

    }

    @Override
    public void signUp(boolean signedUpSuccessful, FirebaseUser user) {

    }

    @Override
    public void userInsertedToDatabase(Users users) {

    }

    @Override
    public void userInsertedToDatabase(School school) {

    }

    @Override
    public void loggedOut() {
        Intent intent = new Intent(HomeActivity.this, AuthenticationViewPagerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void userGotten(School school) {
        mSchool = school;
        Log.e(TAG, "userGotten from database");
    }

    @Override
    public void userGotten(Users users) {

    }
}
