package com.example.android.schoolfinder.interfaces;

import android.support.annotation.Nullable;

import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.Models.Users;

public interface AuthenticationViewPagerCallbacks {

    /**
     * This call back method is used to control the AuthenticationViewPagerActivity viewpager signUp page
     *
     * @param signUp   if true, code to create an account will be executed, else it will just move
     *                 to signUp screen
     * @param users    the object of the user to store in db
     * @param password contains the password to use sign up
     */
    void signUpButtonClicked(boolean signUp, @Nullable Users users, @Nullable String password);

    /**
     * This call back method is used to control the AuthenticationViewPagerActivity viewpager signUp page
     *
     * @param signUp if true, code to create an account will be executed, else it will just move
     *               to signUp screen
     */
    void signUpButtonClicked(boolean signUp, @Nullable School users, @Nullable String password);

    /**
     * This call back method is used to control the AuthenticationViewPagerActivity viewpager login page
     *
     * @param login if true, code to login will be executed, else it will just move
     *              to signUp screen
     */
    void loginButtonClicked(boolean login, @Nullable String email, @Nullable String password);

    /**
     * Used in the school owners flavour authentication page to know when previous page is selected
     */
    void previousButtonCLicked();

    /**
     * Used in the school owners flavour authentication page to know when next page is selected
     */
    void nextButtonCLicked(Users schoolOwnerDetail);
}
