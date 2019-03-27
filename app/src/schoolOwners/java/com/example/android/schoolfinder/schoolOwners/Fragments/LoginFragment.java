package com.example.android.schoolfinder.schoolOwners.Fragments;


import android.content.Intent;
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
import com.example.android.schoolfinder.databinding.FragmentLoginBinding;
import com.example.android.schoolfinder.interfaces.AuthenticationCallbacks;
import com.example.android.schoolfinder.interfaces.AuthenticationViewPagerCallbacks;
import com.example.android.schoolfinder.schoolOwners.HomeActivity;
import com.google.firebase.auth.FirebaseUser;

//import com.example.android.schoolfinder.HomeActivity;
//import com.example.android.schoolfinder.schoolOwners.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements AuthenticationCallbacks, View.OnClickListener {

    private FragmentLoginBinding loginBinding;
    private AppCompatEditText mEmailE, mPasswordE;
    private TextInputLayout mEmailL, mPasswordL;

    private AuthenticationViewPagerCallbacks authenticationViewPagerCallbacks;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        initFields();
        loginBinding.loginButton.setOnClickListener(this);

        loginBinding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authenticationViewPagerCallbacks.signUpButtonClicked(false, (Users) null, null);
            }
        });
        return loginBinding.getRoot();
    }

    public void initAuthenticationCallbacks(AuthenticationViewPagerCallbacks authenticationViewPagerCallbacks) {

        this.authenticationViewPagerCallbacks = authenticationViewPagerCallbacks;
    }

    /**
     * This method initializes login fragment fields
     */
    private void initFields() {
        mEmailE = loginBinding.loginEmail;
        mEmailL = loginBinding.loginEmailInputLayout;
        mPasswordE = loginBinding.loginPassword;
        mPasswordL = loginBinding.loginPasswordInputLayout;
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
        mEmailL.setError(null);
        mPasswordL.setError(null);
    }

    /**
     * This method shows progressbar and hides the button text
     */
    private void enableProgressbar() {
        loginBinding.signInProgressbar.setVisibility(View.VISIBLE);
        loginBinding.loginTextview.setVisibility(View.GONE);
    }

    /**
     * This method hides progressbar and shows the button text
     */
    private void disableProgressbar() {
        loginBinding.signInProgressbar.setVisibility(View.GONE);
        loginBinding.loginTextview.setVisibility(View.VISIBLE);
    }

    /**
     * This method validates the fields
     *
     * @return the number of errors
     */
    private int validateFields() {
        int errorCount = 0;
        if (!Validation.validateEmail(getTextFromEditText(mEmailE))) {
            mEmailL.setError(Validation.EMAIL_NOT_VALID);
            errorCount++;
        }

        if (getTextFromEditText(mPasswordE).length() < 6) {
            mPasswordL.setError(Validation.PASSWORD_LESS);
            errorCount++;
        }

        return errorCount;
    }


    private void loginButtonClicked() {
        if (validateFields() == 0) {
            enableProgressbar();
            Authentication auth = new Authentication(this);
            auth.signIn(getTextFromEditText(mEmailE), getTextFromEditText(mPasswordE));
        }
//
    }

    @Override
    public void login(boolean loggedInSuccessful, FirebaseUser user) {
        if (!loggedInSuccessful)
            disableProgressbar();
        else {
            getActivity()
                    .startActivity(new Intent(getActivity(), HomeActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        }
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
    public void userGotten(School school) {

    }

    @Override
    public void userGotten(Users users) {

    }

    @Override
    public void accountUpdated(boolean isEmail, boolean isSuccessful) {

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.login_button:
                setAuthFieldErrorNull();
                loginButtonClicked();
                authenticationViewPagerCallbacks.loginButtonClicked(true, null, null);
                break;
            case R.id.sign_up_button:
                authenticationViewPagerCallbacks.signUpButtonClicked(false, (School) null, null);
                break;
        }
    }
}
