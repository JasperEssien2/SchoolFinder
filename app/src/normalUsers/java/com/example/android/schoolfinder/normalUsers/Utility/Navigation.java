package com.example.android.schoolfinder.normalUsers.Utility;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.databinding.ActivityHome2Binding;
import com.example.android.schoolfinder.normalUsers.Fragments.ActivitiesFragment;
import com.example.android.schoolfinder.normalUsers.Fragments.SearchPageFragment;
import com.example.android.schoolfinder.normalUsers.Fragments.SettingsFragment;

//import com.example.android.schoolfinder.databinding.ActivitySettingsViewPagerMainBinding;

public class Navigation {
    private static final String TAG = Navigation.class.getSimpleName();

    // activity variables.

    private BottomNavigationView navigation;
    private Activity activity;
    private ActivityHome2Binding binding;
    private int selected;

    // constructor

    public Navigation(Activity a, ActivityHome2Binding binding) {
        activity = a;
        this.binding = binding;
        navigation = activity.findViewById(R.id.navigation);
    }

    public void init() {
        navigation.setSelectedItemId(selected);
        //disableShiftMode();
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_search_page:
//                        removeFragment();
                        selected = R.id.action_search_page;
                        ((AppCompatActivity) activity).getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.content, new SearchPageFragment(), "search_page")
                                .commit();

                        break;

                    case R.id.action_feed_page:
//                        removeFragment();
                        selected = R.id.action_feed_page;
                        ((AppCompatActivity) activity).getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.content, new ActivitiesFragment(), "owner_settings")
                                .commit();
                        break;

                    case R.id.action_profile_page:
//                        removeFragment();
                        selected = R.id.action_profile_page;
                        Log.e(TAG, "class frgament called --------------");
//                        bundle.putBoolean(BundleConstants.IS_CLASS_SEETTING, true);
                        ((AppCompatActivity) activity).getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.content, new SettingsFragment(), "profile page")
                                .commit();

                        break;

//                    case R.id.action_courses_setting:
//                        selected = R.id.action_courses_setting;
//                        bundle.putBoolean(BundleConstants.IS_CLASS_SEETTING, false);
//                        ((AppCompatActivity) activity).getSupportFragmentManager()
//                                .beginTransaction()
//                                .replace(R.id.school_settings, ClassCourseSettingsFragment.newInstance(bundle), "class_course_setting")
//                                .commit();
//                        showFab();
//                        showLogoHideOwnerPic();
//                        break;
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
        if (selected == R.id.action_search_page)
            ((AppCompatActivity) activity).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content, new SearchPageFragment(), "search_page")
                    .commit();
    }

    private void removeFragment() {
        Fragment currentFrag = ((AppCompatActivity) activity).getSupportFragmentManager()
                .findFragmentById(selected);

        ((AppCompatActivity) activity).getSupportFragmentManager()
                .beginTransaction()
                .remove(currentFrag)
                .commit();
    }
}

