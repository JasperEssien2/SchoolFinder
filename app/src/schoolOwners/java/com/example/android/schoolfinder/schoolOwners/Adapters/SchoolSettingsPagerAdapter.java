package com.example.android.schoolfinder.schoolOwners.Adapters;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.schoolfinder.databinding.ActivitySettingsViewPagerBinding;
import com.example.android.schoolfinder.schoolOwners.Fragments.ClassCourseSettingsFragment;
import com.example.android.schoolfinder.schoolOwners.Fragments.OwnerSettingsFragment;
import com.example.android.schoolfinder.schoolOwners.Fragments.SchoolSettingsFragment;

import java.util.ArrayList;
import java.util.List;

public class SchoolSettingsPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = SchoolSettingsPagerAdapter.class.getSimpleName();
    private int NUM_OF_PAGE = 4;

    private List<String> pageTitles;
    private Activity mActivity;
    private Bundle mSchool;
    private ActivitySettingsViewPagerBinding settingsViewPagerBinding;

    public SchoolSettingsPagerAdapter(FragmentManager fm, Activity activity, Bundle school) {
        super(fm);
        mActivity = activity;
        mSchool = school;
        pageTitles = new ArrayList<>();
        pageTitles.add("School Settings");
        pageTitles.add("Owner Settings");
        pageTitles.add("Classes Settings");
        pageTitles.add("Courses Settings");
    }


    @Override
    public Fragment getItem(int position) {


        switch (position) {
            case 0:
                SchoolSettingsFragment schoolSettingsFragment = SchoolSettingsFragment.newInstance(mSchool);
                return schoolSettingsFragment;
            case 1:
                OwnerSettingsFragment ownerSettingsFragment = OwnerSettingsFragment.newInstance(mSchool);
                return ownerSettingsFragment;

            case 2:
                ClassCourseSettingsFragment classSettingsFragment = new ClassCourseSettingsFragment();
                return classSettingsFragment;

            case 3:
                ClassCourseSettingsFragment courseSettingsFragment = new ClassCourseSettingsFragment();
                return courseSettingsFragment;

        }


        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitles.get(position);
    }

    @Override
    public int getCount() {
        return NUM_OF_PAGE;
    }

}
