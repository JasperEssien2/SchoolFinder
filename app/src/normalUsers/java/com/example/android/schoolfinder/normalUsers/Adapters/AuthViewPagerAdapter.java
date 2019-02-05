package com.example.android.schoolfinder.normalUsers.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.schoolfinder.BuildConfig;
import com.example.android.schoolfinder.interfaces.AuthenticationViewPagerCallbacks;
import com.example.android.schoolfinder.normalUsers.Fragments.AuthMainFragment;
import com.example.android.schoolfinder.normalUsers.Fragments.LoginFragment;
import com.example.android.schoolfinder.normalUsers.Fragments.SignUpFragment;

public class AuthViewPagerAdapter extends FragmentPagerAdapter {
    private final AuthenticationViewPagerCallbacks authenticationViewPagerCallbacks;
    private int numOfPages =3;

    public AuthViewPagerAdapter(FragmentManager fm, AuthenticationViewPagerCallbacks authenticationViewPagerCallbacks) {
        super(fm);
        this.authenticationViewPagerCallbacks = authenticationViewPagerCallbacks;
        if(BuildConfig.BUILD_VARIANT.equals("normalUser")){
            numOfPages = 3;
        }else if(BuildConfig.BUILD_VARIANT.equals("schoolOwner")){
            numOfPages = 4;
        }
    }


    @Override
    public Fragment getItem(int position) {

        if(BuildConfig.BUILD_VARIANT.equals("normalUser")){
            switch (position){
                case 0:
                    AuthMainFragment authMainFragment = new AuthMainFragment();
                    authMainFragment.initAuthenticationCallbacks(authenticationViewPagerCallbacks);

                    return authMainFragment;
                case 1:
                    LoginFragment loginFragment = new LoginFragment();
                    loginFragment.initAuthenticationCallbacks(authenticationViewPagerCallbacks);
                    return loginFragment;

                case 2:
                    SignUpFragment signUpFragment = new SignUpFragment();
                    signUpFragment.initAuthenticationCallbacks(authenticationViewPagerCallbacks);
                    return signUpFragment;
            }
        }else if(BuildConfig.BUILD_VARIANT.equals("schoolOwner")){
//            switch (position){
//                case 0:
//                    return new AuthMainFragment();
//                case 1:
//                    return new LoginFragment();
//
//                case 2:
//                    return new SignUpFragment();
//            }
        }

        return null;
    }

    @Override
    public int getCount() {
        return numOfPages;
    }
}
