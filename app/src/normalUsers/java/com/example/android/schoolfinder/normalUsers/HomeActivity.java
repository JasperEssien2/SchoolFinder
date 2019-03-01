package com.example.android.schoolfinder.normalUsers;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.databinding.ActivityHomeBinding;
import com.example.android.schoolfinder.normalUsers.Activities.SearchActivity;
import com.example.android.schoolfinder.normalUsers.Fragments.ActivitiesFragment;
import com.example.android.schoolfinder.normalUsers.Fragments.SettingsFragment;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding homeBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);

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

}
