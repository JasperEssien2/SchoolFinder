package com.example.android.schoolfinder.normalUsers;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.schoolfinder.FirebaseHelper.Authentication;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.Models.Users;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.databinding.ActivityHome2Binding;
import com.example.android.schoolfinder.interfaces.AuthenticationCallbacks;
import com.example.android.schoolfinder.normalUsers.Activities.AuthenticationViewPagerActivity;
import com.example.android.schoolfinder.normalUsers.Utility.Navigation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity implements AuthenticationCallbacks {

    ActivityHome2Binding homeBinding;
    private static final String TAG = HomeActivity.class.getSimpleName();

    private Users mUser;
    private Authentication authentication;
    ActionBarDrawerToggle t;
    private Toolbar toolbar;
    private Navigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home2);
        bottomNavigation = new Navigation(this, homeBinding);
        bottomNavigation.setSelected(R.id.action_search_page);
        bottomNavigation.init();

//        setSupportActionBar(homeBinding.toolbar);
//        getSupportActionBar().setTitle("");
//        getSupportActionBar().setElevation(8.0f);
//        ViewCompat.setElevation(homeBinding.userDetailsView, 8.0f);
//        ViewCompat.setElevation(homeBinding.homeUserImage, 8.0f);

//        ActionBarDrawerToggle t = new ActionBarDrawerToggle(this, homeBinding.homeDrawerLayout, homeBinding.toolbar, R.string.Open, R.string.Close);
//
//        homeBinding.homeDrawerLayout.addDrawerListener(t);
//        t.syncState();
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
////        getSupportActionBar().setHomeButtonEnabled(true);

        homeBinding.homeNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.action_home:
                        Toast.makeText(HomeActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_top_schools:
                        Toast.makeText(HomeActivity.this, "Top Schools", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_chats:
                        Toast.makeText(HomeActivity.this, "Chats", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_following:
                        Toast.makeText(HomeActivity.this, "Following", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_applications:
                        Toast.makeText(HomeActivity.this, "Applications", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_subsription:
                        Toast.makeText(HomeActivity.this, "Subscription", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        return true;
                }


                return true;

            }
        });
        authentication = new Authentication(this);
        authentication.updateToken();
//        FlowTextView flowTextView;
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            authentication.getUserDetail(FirebaseAuth.getInstance().getCurrentUser().getUid(), false);

//        homeBinding.buttonSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                getSupportFragmentManager()
////                        .beginTransaction()
////                        .addToBackStack(null)
////                        .replace(homeBinding.activityHome.getId(), new SearchPageFragment())
////                        .commit();
//                Intent intent = new Intent(HomeActivity.this, SearchPageFragment.class);
//                startActivity(intent);
//            }
//        });
//        homeBinding.buttonNotification.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .addToBackStack(null)
//                        .replace(homeBinding.activityHome.getId(), new ActivitiesFragment())
//                        .commit();
//            }
//        });
//
//        homeBinding.buttonSettings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .addToBackStack(null)
//                        .replace(homeBinding.activityHome.getId(), new SettingsFragment())
//                        .commit();
//            }
//        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            authentication.getUserDetail(FirebaseAuth.getInstance().getCurrentUser().getUid(), false);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
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

    @Override
    public void accountUpdated(boolean isEmail, boolean isSuccessful, String newEmail) {

    }

    public void initToolbar(Toolbar toolbar) {

        this.toolbar = toolbar;
        if (toolbar == null) return;
        setSupportActionBar(toolbar);
        t = new ActionBarDrawerToggle(this, homeBinding.homeDrawerLayout, toolbar, R.string.Open, R.string.Close);

        homeBinding.homeDrawerLayout.addDrawerListener(t);
        t.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void setUpViewWithData() {
        if (mUser == null) return;
        if (mUser.getProfileImageUrl() != null && !mUser.getProfileImageUrl().isEmpty()) {
//            new PicassoImageLoader(this, mUser.getProfileImageUrl(), R.color.colorLightGrey,
//                    R.color.colorLightGrey, homeBinding.homeUserImage);
//            new PicassoImageLoader(this, mUser.getProfileImageUrl(), R.color.colorLightGrey,
//                    R.color.colorLightGrey, homeBinding.backgroundImage);
        }
//        homeBinding.name.setText(mUser.getName() != null ? mUser.getName() : "");
//        homeBinding.email.setText(mUser.getEmail() != null ? mUser.getEmail() : "");
//        homeBinding.location.setText(mUser.getLocation() != null ? mUser.getLocation() : "");
    }
}
