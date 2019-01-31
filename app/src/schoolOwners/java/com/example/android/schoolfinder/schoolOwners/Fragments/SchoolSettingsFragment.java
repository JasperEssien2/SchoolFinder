package com.example.android.schoolfinder.schoolOwners.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.schoolfinder.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SchoolSettingsFragment extends Fragment {


    public SchoolSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_school_settings, container, false);
    }

}
