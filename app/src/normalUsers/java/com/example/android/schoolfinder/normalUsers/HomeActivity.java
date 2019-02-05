package com.example.android.schoolfinder.normalUsers;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.databinding.ActivityHomeBinding;
import com.example.android.schoolfinder.normalUsers.Fragments.SearchFragment;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding homeBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
//        FlowTextView flowTextView;
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(homeBinding.activityHome.getId(), new SearchFragment())
                .commit();


    }

}
