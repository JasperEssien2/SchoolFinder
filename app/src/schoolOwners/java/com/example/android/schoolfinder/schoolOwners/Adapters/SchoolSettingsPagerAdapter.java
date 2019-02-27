package com.example.android.schoolfinder.schoolOwners.Adapters;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.example.android.schoolfinder.Constants.BundleConstants;
import com.example.android.schoolfinder.databinding.ActivitySettingsViewPagerBinding;
import com.example.android.schoolfinder.schoolOwners.Fragments.ClassCourseSettingsFragment;
import com.example.android.schoolfinder.schoolOwners.Fragments.OwnerSettingsFragment;
import com.example.android.schoolfinder.schoolOwners.Fragments.SchoolSettingsFragment;
import com.example.android.schoolfinder.schoolOwners.interfaces.SchoolSettingsViewPagerCallback;

import java.util.ArrayList;
import java.util.List;

public class SchoolSettingsPagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = SchoolSettingsPagerAdapter.class.getSimpleName();
    private int NUM_OF_PAGE = 4;

    private List<String> pageTitles;
    private Activity mActivity;
    private Bundle mSchool;
    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
    //    private SchoolSettingsViewPagerCallback mViewPagerCallback;
    private ActivitySettingsViewPagerBinding settingsViewPagerBinding;

    public SchoolSettingsPagerAdapter(FragmentManager fm, Activity activity, Bundle school,
                                      SchoolSettingsViewPagerCallback viewPagerCallback) {
        super(fm);
        mActivity = activity;
        mSchool = school;
//        mViewPagerCallback = viewPagerCallback;
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
//                mViewPagerCallback.showLogoHideOwnerPic();
                SchoolSettingsFragment schoolSettingsFragment = SchoolSettingsFragment.newInstance(mSchool);
                return schoolSettingsFragment;
            case 1:
//                mViewPagerCallback.hideLogoShowOwnerPic();
                OwnerSettingsFragment ownerSettingsFragment = OwnerSettingsFragment.newInstance(mSchool);
                return ownerSettingsFragment;

            case 2:
//                mViewPagerCallback.hideLogoShowOwnerPic();/
                mSchool.putBoolean(BundleConstants.IS_CLASS_SEETTING, true);
                ClassCourseSettingsFragment classSettingsFragment = ClassCourseSettingsFragment.newInstance(mSchool);
                return classSettingsFragment;

            case 3:
//                mViewPagerCallback.hideLogoShowOwnerPic();
                mSchool.putBoolean(BundleConstants.IS_CLASS_SEETTING, false);
                ClassCourseSettingsFragment courseSettingsFragment = ClassCourseSettingsFragment.newInstance(mSchool);
                return courseSettingsFragment;

        }


        return null;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
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
