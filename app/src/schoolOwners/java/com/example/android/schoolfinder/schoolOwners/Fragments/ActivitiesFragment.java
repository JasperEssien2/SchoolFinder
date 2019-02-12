package com.example.android.schoolfinder.schoolOwners.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.schoolfinder.R;
//import com.example.android.schoolfinder.schoolOwners.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivitiesFragment extends Fragment {

    //TODO: This fragment contains things like the number of followers the school has, People impression
    //TODO: about the school by way of comments, numbers of likes the school has gotten etc
    //TODO: Instead of numbers of likes, use emojis for number of impression (normal, sad, happy emojis)
    //TODO: Just like udacity's way after each course to ask what student tink bout d course, Users can select


    public ActivitiesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activities, container, false);
    }

}
