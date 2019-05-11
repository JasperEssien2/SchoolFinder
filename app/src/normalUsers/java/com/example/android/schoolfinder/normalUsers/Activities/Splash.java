package com.example.android.schoolfinder.normalUsers.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.schoolfinder.R;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        try {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
        finish();
//                }
//            }).wait(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
