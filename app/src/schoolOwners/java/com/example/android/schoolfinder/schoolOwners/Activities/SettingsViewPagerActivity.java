package com.example.android.schoolfinder.schoolOwners.Activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.android.schoolfinder.Constants.BundleConstants;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.Models.Users;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.databinding.ActivitySettingsViewPagerBinding;
import com.example.android.schoolfinder.interfaces.AuthenticationCallbacks;
import com.example.android.schoolfinder.schoolOwners.Adapters.SchoolSettingsPagerAdapter;
import com.google.firebase.auth.FirebaseUser;

public class SettingsViewPagerActivity extends AppCompatActivity implements AuthenticationCallbacks {

    private ActivitySettingsViewPagerBinding settingsViewPagerBinding;
    private School school;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsViewPagerBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings_view_pager);
        setSupportActionBar(settingsViewPagerBinding.toolbar);

        school = getSchool();
        settingsViewPagerBinding.settingsTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                settingsViewPagerBinding.settingsViewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        settingsViewPagerBinding.settingsTabs.setupWithViewPager(settingsViewPagerBinding.settingsViewpager);
        settingsViewPagerBinding.settingsViewpager.setAdapter(new SchoolSettingsPagerAdapter(
                getSupportFragmentManager(), this, getIntent().getExtras()));
    }

    private School getSchool() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(BundleConstants.SCHOOL_BUNDLE)) {
                return bundle.getParcelable(BundleConstants.SCHOOL_BUNDLE);
            }
        }
        return null;
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
        if (null != school) {
            this.school = school;
            Toast.makeText(this, "Changes Made.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void loggedOut() {

    }

    @Override
    public void userGotten(School school) {

    }

    @Override
    public void userGotten(Users users) {

    }
}
