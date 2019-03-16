package com.example.android.schoolfinder.normalUsers;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.android.schoolfinder.FirebaseHelper.Authentication;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.Models.Users;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.Utility.PicassoImageLoader;
import com.example.android.schoolfinder.databinding.ActivityHomeBinding;
import com.example.android.schoolfinder.interfaces.AuthenticationCallbacks;
import com.example.android.schoolfinder.normalUsers.Activities.AuthenticationViewPagerActivity;
import com.example.android.schoolfinder.normalUsers.Activities.SearchActivity;
import com.example.android.schoolfinder.normalUsers.Fragments.ActivitiesFragment;
import com.example.android.schoolfinder.normalUsers.Fragments.SettingsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity implements AuthenticationCallbacks {

    ActivityHomeBinding homeBinding;
    private static final String TAG = HomeActivity.class.getSimpleName();

    private Users mUser;
    private Authentication authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        setSupportActionBar(homeBinding.toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setElevation(8.0f);
        ViewCompat.setElevation(homeBinding.userDetailsView, 8.0f);
        ViewCompat.setElevation(homeBinding.homeUserImage, 8.0f);

        authentication = new Authentication(this);
        authentication.updateToken();
//        FlowTextView flowTextView;
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            authentication.getUserDetail(FirebaseAuth.getInstance().getCurrentUser().getUid(), false);

        homeBinding.buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .addToBackStack(null)
//                        .replace(homeBinding.activityHome.getId(), new SearchActivity())
//                        .commit();
                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        homeBinding.buttonNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(homeBinding.activityHome.getId(), new ActivitiesFragment())
                        .commit();
            }
        });

        homeBinding.buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(homeBinding.activityHome.getId(), new SettingsFragment())
                        .commit();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        authentication.getUserDetail(FirebaseAuth.getInstance().getCurrentUser().getUid(), false);
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

    }

    @Override
    public void userGotten(Users users) {
        mUser = users;
        setUpViewWithData();
        Log.e(TAG, "userGotten from database");
    }

    private void setUpViewWithData() {
        if (mUser == null) return;
        if (mUser.getProfileImageUrl() != null && !mUser.getProfileImageUrl().isEmpty()) {
            new PicassoImageLoader(this, mUser.getProfileImageUrl(), R.color.colorLightGrey,
                    R.color.colorLightGrey, homeBinding.homeUserImage);
            new PicassoImageLoader(this, mUser.getProfileImageUrl(), R.color.colorLightGrey,
                    R.color.colorLightGrey, homeBinding.backgroundImage);
        }
        homeBinding.name.setText(mUser.getName() != null ? mUser.getName() : "");
        homeBinding.email.setText(mUser.getEmail() != null ? mUser.getEmail() : "");
        homeBinding.location.setText(mUser.getLocation() != null ? mUser.getLocation() : "");
    }
}
