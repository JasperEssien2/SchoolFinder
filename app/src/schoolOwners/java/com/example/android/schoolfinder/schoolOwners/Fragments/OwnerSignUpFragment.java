package com.example.android.schoolfinder.schoolOwners.Fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.schoolfinder.FirebaseHelper.Authentication;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.Models.Users;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.Utility.Validation;
import com.example.android.schoolfinder.databinding.FragmentOwnersSignUpBinding;
import com.example.android.schoolfinder.interfaces.AuthenticationCallbacks;
import com.example.android.schoolfinder.interfaces.AuthenticationViewPagerCallbacks;
import com.google.firebase.auth.FirebaseUser;
//import com.example.android.schoolfinder.schoolOwners.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OwnerSignUpFragment extends Fragment implements View.OnClickListener, AuthenticationCallbacks {

    private FragmentOwnersSignUpBinding ownersSignUpBinding;
    private Authentication auth = new Authentication(this);
    private AuthenticationViewPagerCallbacks authenticationViewPagerCallbacks;
    private AppCompatEditText mNameE, mContactE, mEmailE, mLocationE, mBiographyE;
    private TextInputLayout mNameL, mContactL, mEmailL, mLocationL, mBiographyL;

    public OwnerSignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ownersSignUpBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_owners_sign_up, container, false);
        initFields();
        ownersSignUpBinding.loginButton.setOnClickListener(this);
        ownersSignUpBinding.nextButton.setOnClickListener(this);

        return ownersSignUpBinding.getRoot();
    }

    public void initAuthenticationCallbacks(AuthenticationViewPagerCallbacks authenticationViewPagerCallbacks) {

        this.authenticationViewPagerCallbacks = authenticationViewPagerCallbacks;
    }

    /**
     * This method initializes login fragment fields
     */
    private void initFields() {
        mNameE = ownersSignUpBinding.signupOwnerName;
        mNameL = ownersSignUpBinding.signupOwnerNameInputLayout;
        mContactE = ownersSignUpBinding.signupOwnerContact;
        mContactL = ownersSignUpBinding.signupOwnerContactInputLayout;
        mEmailE = ownersSignUpBinding.signupOwnerEmail;
        mEmailL = ownersSignUpBinding.signupOwnerEmailInputLayout;
        mLocationE = ownersSignUpBinding.signupOwnerLocation;
        mLocationL = ownersSignUpBinding.signupOwnerLocationInputLayout;
        mBiographyE = ownersSignUpBinding.signupOwnerBiography;
        mBiographyL = ownersSignUpBinding.signupOwnerBiographyInputLayout;
    }

    /**
     * Gets the text from the edittext
     *
     * @param editText required
     * @return String of text
     */
    private String getTextFromEditText(AppCompatEditText editText) {
        return editText.getText().toString().trim();
    }

    /**
     * Sets fields error null
     */
    private void setAuthFieldErrorNull() {
        mNameL.setError(null);
        mContactL.setError(null);
        mEmailL.setError(null);
        mLocationL.setError(null);
        mBiographyL.setError(null);
    }


    /**
     * This method validates the fields
     *
     * @return the number of errors
     */
    private int validateFields() {
        int errorCount = 0;
        if (!Validation.validateFields(getTextFromEditText(mNameE))) {
            mNameL.setError(Validation.EMPTY_FIELD);
            errorCount++;
        }

        if (!Validation.validateFields(getTextFromEditText(mContactE))) {
            mContactL.setError(Validation.EMPTY_FIELD);
            errorCount++;
        }

        if (!Validation.validateEmail(getTextFromEditText(mEmailE))) {
            mEmailL.setError(Validation.EMAIL_NOT_VALID);
            errorCount++;
        }

        if (!Validation.validateFields(getTextFromEditText(mLocationE))) {
            mLocationL.setError(Validation.EMPTY_FIELD);
            errorCount++;
        }

        if (!Validation.validateFields(getTextFromEditText(mBiographyE))) {
            mBiographyL.setError(Validation.EMPTY_FIELD);
            errorCount++;
        }


        return errorCount;
    }


    /**
     * Initializes a user object
     *
     * @return user
     */
    private Users getUser() {
        Users users = new Users();
        users.setName(getTextFromEditText(mNameE));
        users.setContact(getTextFromEditText(mContactE));
        users.setEmail(getTextFromEditText(mEmailE));
        users.setLocation(getTextFromEditText(mLocationE));
        users.setBiography(getTextFromEditText(mBiographyE));
        return users;
    }


    @Override
    public void login(boolean loggedInSuccessful, FirebaseUser user) {
    }

    @Override
    public void signUp(boolean signedUpSuccessful, FirebaseUser user) {
    }

    @Override
    public void userInsertedToDatabase(Users users) {
    }

    @Override
    public void userInsertedToDatabase(School school) {
    }

    @Override
    public void loggedOut() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_button:
                setAuthFieldErrorNull();
                if (validateFields() == 0) {
                    authenticationViewPagerCallbacks.nextButtonCLicked(getUser());
                }
                break;
            case R.id.login_button:
                authenticationViewPagerCallbacks.loginButtonClicked(false, null, null);
                break;
        }
    }
}
