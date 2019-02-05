package com.example.android.schoolfinder.normalUsers.Fragments;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.example.android.schoolfinder.Activities.AuthenticationViewPagerActivity;
import com.example.android.schoolfinder.Constants.BundleConstants;
import com.example.android.schoolfinder.FirebaseHelper.Authentication;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.Models.Users;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.Utility.Validation;
import com.example.android.schoolfinder.databinding.FragmentSignUpBinding;
import com.example.android.schoolfinder.interfaces.AuthenticationCallbacks;
import com.example.android.schoolfinder.interfaces.AuthenticationViewPagerCallbacks;
import com.example.android.schoolfinder.normalUsers.Activities.AuthenticationViewPagerActivity;
import com.example.android.schoolfinder.normalUsers.HomeActivity;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment implements AuthenticationCallbacks {

    private Authentication auth = new Authentication(this);
    private FragmentSignUpBinding fragmentSignUpBinding;
    private AuthenticationViewPagerCallbacks authenticationViewPagerCallbacks;
    private AppCompatEditText mNameE, mContactE, mEmailE, mLocationE, mPasswordE,
            mConfirmPasswordE;
    private TextInputLayout mNameL, mContactL, mEmailL, mLocationL, mPasswordL,
            mConfirmPasswordL;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentSignUpBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false);
        initFields();
        fragmentSignUpBinding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authenticationViewPagerCallbacks.loginButtonClicked(false, null, null);
            }
        });

        fragmentSignUpBinding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authenticationViewPagerCallbacks.signUpButtonClicked(true, (Users) null, null);
                setAuthFieldErrorNull();
                signUpButtonClicked();
            }
        });
        return fragmentSignUpBinding.getRoot();
    }

    public void initAuthenticationCallbacks(AuthenticationViewPagerCallbacks authenticationViewPagerCallbacks) {

        this.authenticationViewPagerCallbacks = authenticationViewPagerCallbacks;
    }

    /**
     * This method initializes login fragment fields
     */
    private void initFields() {
        mNameE = fragmentSignUpBinding.signupName;
        mNameL = fragmentSignUpBinding.signupNameInputLayout;
        mContactE = fragmentSignUpBinding.signupContact;
        mContactL = fragmentSignUpBinding.signupContactInputLayout;
        mEmailE = fragmentSignUpBinding.signupEmail;
        mEmailL = fragmentSignUpBinding.signupEmailInputLayout;
        mLocationE = fragmentSignUpBinding.signupLocation;
        mLocationL = fragmentSignUpBinding.signupLocationInputLayout;
        mPasswordE = fragmentSignUpBinding.signUpPassword;
        mPasswordL = fragmentSignUpBinding.signUpPasswordInputLayout;
        mConfirmPasswordE = fragmentSignUpBinding.signUpConfirmPassword;
        mConfirmPasswordL = fragmentSignUpBinding.signUpConfirmPasswordInputLayout;
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
        mPasswordL.setError(null);
        mConfirmPasswordL.setError(null);
    }

    /**
     * This method shows progressbar and hides the button text
     */
    private void enableProgressbar() {
        fragmentSignUpBinding.signUpProgressbar.setVisibility(View.VISIBLE);
        fragmentSignUpBinding.signUpTextview.setVisibility(View.GONE);
    }

    /**
     * This method hides progressbar and shows the button text
     */
    private void disableProgressbar() {
        fragmentSignUpBinding.signUpProgressbar.setVisibility(View.GONE);
        fragmentSignUpBinding.signUpTextview.setVisibility(View.VISIBLE);
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

        if (getTextFromEditText(mPasswordE).length() < 6) {
            mPasswordL.setError(Validation.PASSWORD_LESS);
            errorCount++;
        }

        if (!getTextFromEditText(mConfirmPasswordE).equals(getTextFromEditText(mPasswordE))) {
            mPasswordL.setError(Validation.PASSWORD_NOT_EQUAL);
            errorCount++;
        }

        return errorCount;
    }


    private void signUpButtonClicked() {
        if (validateFields() == 0) {
            enableProgressbar();
            auth.createNewUser(getTextFromEditText(mEmailE), getTextFromEditText(mPasswordE));
        }
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
        return users;
    }

    @Override
    public void login(boolean loggedInSuccessful, FirebaseUser user) { }

    @Override
    public void signUp(boolean signedUpSuccessful, FirebaseUser user) {
        Users users = getUser();
        users.setId(user.getUid());
        if (signedUpSuccessful)
            auth.putNewUserInDb(users);
        else {
            disableProgressbar();
        }
    }

    @Override
    public void userInsertedToDatabase(Users users) {
        if(users != null) {
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(BundleConstants.USER_BUNDLE, users);
            startActivity(intent);
        }else{
            auth.deleteAccount((Users) null);
        }
    }

    @Override
    public void userInsertedToDatabase(School school) {

    }

    @Override
    public void loggedOut() {
        Intent intent = new Intent(getActivity(), AuthenticationViewPagerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
