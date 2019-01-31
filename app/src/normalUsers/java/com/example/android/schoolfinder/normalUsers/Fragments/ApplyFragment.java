package com.example.android.schoolfinder.normalUsers.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.schoolfinder.R;
//import com.example.android.schoolfinder.normalUsers.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ApplyFragment extends Fragment {


    public ApplyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_apply, container, false);
    }

}
