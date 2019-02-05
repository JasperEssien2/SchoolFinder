package com.example.android.schoolfinder.schoolOwners.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.schoolfinder.interfaces.AuthenticationViewPagerCallbacks;
import com.example.android.schoolfinder.schoolOwners.Fragments.AuthMainFragment;
import com.example.android.schoolfinder.schoolOwners.Fragments.LoginFragment;
import com.example.android.schoolfinder.schoolOwners.Fragments.OwnerSignUpFragment;
import com.example.android.schoolfinder.schoolOwners.Fragments.SchoolSignUpFragment;

public class AuthViewPagerAdapter extends FragmentPagerAdapter {
    private final AuthenticationViewPagerCallbacks authenticationViewPagerCallbacks;
    private int numOfPages = 4;

    public AuthViewPagerAdapter(FragmentManager fm, AuthenticationViewPagerCallbacks authenticationViewPagerCallbacks) {
        super(fm);
        this.authenticationViewPagerCallbacks = authenticationViewPagerCallbacks;

    }


    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                AuthMainFragment authMainFragment = new AuthMainFragment();
                authMainFragment.initAuthenticationCallbacks(authenticationViewPagerCallbacks);

                return authMainFragment;
            case 1:
                LoginFragment loginFragment = new LoginFragment();
                loginFragment.initAuthenticationCallbacks(authenticationViewPagerCallbacks);
                return loginFragment;

            case 2:
                OwnerSignUpFragment ownerSignUpFragment = new OwnerSignUpFragment();
                ownerSignUpFragment.initAuthenticationCallbacks(authenticationViewPagerCallbacks);
                return ownerSignUpFragment;
            case 3:
                SchoolSignUpFragment schoolSignUpFragment = new SchoolSignUpFragment();
                schoolSignUpFragment.initAuthenticationCallbacks(authenticationViewPagerCallbacks);
                return schoolSignUpFragment;
        }

        return null;
    }

    @Override
    public int getCount() {
        return numOfPages;
    }
}
