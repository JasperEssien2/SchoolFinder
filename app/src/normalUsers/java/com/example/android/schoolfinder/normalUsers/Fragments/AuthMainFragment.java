package com.example.android.schoolfinder.normalUsers.Fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.schoolfinder.Models.Users;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.databinding.FragmentAuthMainBinding;
import com.example.android.schoolfinder.interfaces.AuthenticationViewPagerCallbacks;
//import com.example.android.schoolfinder.normalUsers.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AuthMainFragment extends Fragment {


    private AuthenticationViewPagerCallbacks authenticationViewPagerCallbacks;
    private FragmentAuthMainBinding fragmentAuthMainBinding;

    public AuthMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentAuthMainBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_auth_main, container, false);
        // Inflate the layout for this fragment
        fragmentAuthMainBinding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authenticationViewPagerCallbacks.loginButtonClicked(false, null, null);
            }
        });
        fragmentAuthMainBinding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authenticationViewPagerCallbacks.signUpButtonClicked(false, (Users) null, null);
            }
        });
        return fragmentAuthMainBinding.getRoot();
    }

    public void initAuthenticationCallbacks(AuthenticationViewPagerCallbacks authenticationViewPagerCallbacks) {

        this.authenticationViewPagerCallbacks = authenticationViewPagerCallbacks;
    }

}
