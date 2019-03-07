package com.example.android.schoolfinder.schoolOwners.Adapters;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.example.android.schoolfinder.Constants.BundleConstants;
import com.example.android.schoolfinder.databinding.ActivitySettingsViewPagerBinding;
import com.example.android.schoolfinder.schoolOwners.Fragments.ClassCourseSettingsFragment;
import com.example.android.schoolfinder.schoolOwners.Fragments.OwnerSettingsFragment;
import com.example.android.schoolfinder.schoolOwners.Fragments.SchoolPhotosFragment;
import com.example.android.schoolfinder.schoolOwners.Fragments.SchoolSettingsFragment;
import com.example.android.schoolfinder.schoolOwners.interfaces.SchoolSettingsViewPagerCallback;

import java.util.ArrayList;
import java.util.List;

public class SchoolSettingsPagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = SchoolSettingsPagerAdapter.class.getSimpleName();
    private int NUM_OF_PAGE = 5;

    private List<String> pageTitles;
    private Activity mActivity;
    private Bundle mSchool;
    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
    //    private SchoolSettingsViewPagerCallback mViewPagerCallback;
    private ActivitySettingsViewPagerBinding settingsViewPagerBinding;

    private int tabSelectedPos;

    public SchoolSettingsPagerAdapter(FragmentManager fm, Activity activity, Bundle school,
                                      SchoolSettingsViewPagerCallback viewPagerCallback) {
        super(fm);
        mActivity = activity;
        mSchool = school;
//        mViewPagerCallback = viewPagerCallback;
        pageTitles = new ArrayList<>();
        pageTitles.add("School");
        pageTitles.add("Owner");
        pageTitles.add("Classes");
        pageTitles.add("Courses");
        pageTitles.add("Gallery");
    }


    public void tabSelected(int pos) {

        this.tabSelectedPos = pos;
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
                Log.e(TAG, "class frgament called --------------");
                mSchool.putBoolean(BundleConstants.IS_CLASS_SEETTING, true);
                ClassCourseSettingsFragment classSettingsFragment = ClassCourseSettingsFragment.newInstance(mSchool);
                return classSettingsFragment;

            case 3:
                Log.e(TAG, "tabSelectedPos ----------- " + tabSelectedPos);
                mSchool.putBoolean(BundleConstants.IS_CLASS_SEETTING, false);
                ClassCourseSettingsFragment courseSettingsFragment = ClassCourseSettingsFragment.newInstance(mSchool);
//                    if (classSettingsPageCallback != null)
//                        classSettingsPageCallback.updateCourseClassSettingsPage(3);
                return courseSettingsFragment;
//                //To prevent the next fragment overriding this.. when tabselected, we use this tabSelectedPos
//                //int to verify which is meant fow which, if atall wat i said made sense
//                if (tabSelectedPos == 2) {
//                    Log.e(TAG, "class frgament called --------------");
//                    tabSelectedPos = 0;
//                    mSchool.putBoolean(BundleConstants.IS_CLASS_SEETTING, true);
//                    ClassCourseSettingsFragment classSettingsFragment2 = ClassCourseSettingsFragment.newInstance(mSchool);
////                    if (classSettingsPageCallback != null)
////                        classSettingsPageCallback.updateCourseClassSettingsPage(2);
//                    return classSettingsFragment2;
//                }
//                if (tabSelectedPos == 3) {
//                    Log.e(TAG, "course frgament called --------------");
//                    mSchool.putBoolean(BundleConstants.IS_CLASS_SEETTING, false);
//                    ClassCourseSettingsFragment courseSettingsFragment = new  ClassCourseSettingsFragment();
////                    if (classSettingsPageCallback != null)
////                        classSettingsPageCallback.updateCourseClassSettingsPage(3);
//                    return courseSettingsFragment;
//                }
            case 4:
                return new SchoolPhotosFragment();
        }


        return null;
    }

//    public interface UpdateCourseClassSettingsPage {
//        void updateCourseClassSettingsPage(int tabPosition);
//    }

//    private UpdateCourseClassSettingsPage classSettingsPageCallback;

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
//        if(position == 2 || position == 3)
//        classSettingsPageCallback = (ClassCourseSettingsFragment) registeredFragments.get(position);
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
