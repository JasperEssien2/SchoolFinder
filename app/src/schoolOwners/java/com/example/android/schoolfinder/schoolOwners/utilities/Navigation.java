package com.example.android.schoolfinder.schoolOwners.utilities;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.android.schoolfinder.Constants.BundleConstants;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.databinding.ActivitySettingsViewPagerMainBinding;
import com.example.android.schoolfinder.schoolOwners.Fragments.ClassCourseSettingsFragment;
import com.example.android.schoolfinder.schoolOwners.Fragments.OwnerSettingsFragment;
import com.example.android.schoolfinder.schoolOwners.Fragments.SchoolPhotosFragment;
import com.example.android.schoolfinder.schoolOwners.Fragments.SchoolSettingsFragment;

public class Navigation {
    private static final String TAG = Navigation.class.getSimpleName();

    // activity variables.

    private BottomNavigationView navigation;
    private Activity activity;
    private Bundle bundle;
    private FloatingActionButton addCoursesClassesFab;
    private ActivitySettingsViewPagerMainBinding settingsViewPagerBinding;
    private int selected;

    // constructor

    public Navigation(Activity a, Bundle bundle, FloatingActionButton addCoursesClassesFab, ActivitySettingsViewPagerMainBinding settingsViewPagerBinding) {
        activity = a;
        this.bundle = bundle;
        this.addCoursesClassesFab = addCoursesClassesFab;
        this.settingsViewPagerBinding = settingsViewPagerBinding;
        navigation = activity.findViewById(R.id.navigation);
    }

    public void init() {
        if (bundle == null) return;
        navigation.setSelectedItemId(selected);
        //disableShiftMode();
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_school_settings:
                        selected = R.id.action_school_settings;
                        ((AppCompatActivity) activity).getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.school_settings, SchoolSettingsFragment.newInstance(bundle), "school_settings")
                                .commit();
                        hideFab();
                        showLogoHideOwnerPic();
                        break;

                    case R.id.action_owner_settings:
                        selected = R.id.action_owner_settings;
                        ((AppCompatActivity) activity).getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.school_settings, OwnerSettingsFragment.newInstance(bundle), "owner_settings")
                                .commit();
                        hideFab();
                        hideLogoShowOwnerPic();
                        break;

                    case R.id.action_classes_settings:
                        selected = R.id.action_classes_settings;
                        Log.e(TAG, "class frgament called --------------");
                        bundle.putBoolean(BundleConstants.IS_CLASS_SEETTING, true);
                        ((AppCompatActivity) activity).getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.school_settings, ClassCourseSettingsFragment.newInstance(bundle), "class_course_setting")
                                .commit();
                        showFab();
                        showLogoHideOwnerPic();
                        break;

                    case R.id.action_courses_setting:
                        selected = R.id.action_courses_setting;
                        bundle.putBoolean(BundleConstants.IS_CLASS_SEETTING, false);
                        ((AppCompatActivity) activity).getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.school_settings, ClassCourseSettingsFragment.newInstance(bundle), "class_course_setting")
                                .commit();
                        showFab();
                        showLogoHideOwnerPic();
                        break;

                    case R.id.action_gallery_setting:
                        selected = R.id.action_gallery_setting;
                        ((AppCompatActivity) activity).getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.school_settings, new SchoolPhotosFragment(), "gallery_setting")
                                .commit();
                        showFab();
                        showLogoHideOwnerPic();
                        break;
                }
                return true;
            }
        });
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int i) {
        selected = i;
        if (selected == R.id.action_school_settings) {
//            TODO: uncomment this oh, i comment this just to avoid the map crashing on the emulator
            ((AppCompatActivity) activity).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.school_settings, SchoolSettingsFragment.newInstance(bundle))
                    .commit();
        }
    }

    public void hideLogoShowOwnerPic() {
        settingsViewPagerBinding.frameLayout.setVisibility(View.VISIBLE);
        settingsViewPagerBinding.schoolAddress.setVisibility(View.GONE);
        settingsViewPagerBinding.schoolName.setVisibility(View.GONE);
        settingsViewPagerBinding.schoolSettingsLogoImgview.setVisibility(View.GONE);
        settingsViewPagerBinding.facebook.setVisibility(View.GONE);
        settingsViewPagerBinding.twitter.setVisibility(View.GONE);
        settingsViewPagerBinding.mail.setVisibility(View.GONE);
//        settingsViewPagerBinding.schoolSettingsEditName.setVisibility(View.GONE);
        Log.e(TAG, "hideLogoShowOwnerPic() _______________________");
    }

    public void showLogoHideOwnerPic() {
        settingsViewPagerBinding.frameLayout.setVisibility(View.GONE);
        settingsViewPagerBinding.schoolAddress.setVisibility(View.VISIBLE);
        settingsViewPagerBinding.schoolName.setVisibility(View.VISIBLE);
        settingsViewPagerBinding.schoolSettingsLogoImgview.setVisibility(View.VISIBLE);
        settingsViewPagerBinding.facebook.setVisibility(View.VISIBLE);
        settingsViewPagerBinding.twitter.setVisibility(View.VISIBLE);
        settingsViewPagerBinding.mail.setVisibility(View.VISIBLE);
//        settingsViewPagerBinding.schoolSettingsEditName.setVisibility(View.VISIBLE);
        Log.e(TAG, "showLogoHideOwnerPic() _______________________");
    }

    private void hideFab() {
        addCoursesClassesFab.setVisibility(View.GONE);
        settingsViewPagerBinding.schoolStats.setVisibility(View.VISIBLE);
    }

    private void showFab() {
        addCoursesClassesFab.setVisibility(View.VISIBLE);
        settingsViewPagerBinding.schoolStats.setVisibility(View.GONE);
    }
}

