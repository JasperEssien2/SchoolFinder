package com.example.android.schoolfinder.normalUsers.Adapters;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.schoolfinder.databinding.ActivitySchoolDetailBinding;
import com.example.android.schoolfinder.normalUsers.Fragments.ApplyFragment;
import com.example.android.schoolfinder.normalUsers.Fragments.SchoolDetailFragment;
import com.example.android.schoolfinder.normalUsers.Fragments.SchoolPhotosFragment;

import java.util.ArrayList;
import java.util.List;

public class SchoolDetailPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = SchoolDetailPagerAdapter.class.getSimpleName();
    private int NUM_OF_PAGE = 3;

    private List<String> pageTitles;
    private Activity mActivity;
    private Bundle mSchool;
    private ActivitySchoolDetailBinding mSchoolDetailBinding;

    public SchoolDetailPagerAdapter(FragmentManager fm, Activity activity, Bundle school) {
        super(fm);
        mActivity = activity;
        mSchool = school;
//        mSchoolDetailBinding = schoolDetailBinding;
        pageTitles = new ArrayList<>();
        pageTitles.add("Details");
        pageTitles.add("Photos");
        pageTitles.add("Apply");
    }


    @Override
    public Fragment getItem(int position) {


        switch (position) {
            case 0:
                SchoolDetailFragment schoolDetailFragment = SchoolDetailFragment.newInstance(mSchool);
                return schoolDetailFragment;
            case 1:
                SchoolPhotosFragment photosFragment = new SchoolPhotosFragment();
                return photosFragment;

            case 2:
                ApplyFragment applyFragment = new ApplyFragment();
                return applyFragment;
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
