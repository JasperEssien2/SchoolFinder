package com.example.android.schoolfinder.schoolOwners.Fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.schoolfinder.Constants.BundleConstants;
import com.example.android.schoolfinder.FirebaseHelper.Authentication;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.Models.Users;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.Utility.AppLocationService;
import com.example.android.schoolfinder.Utility.GeocoderHandler;
import com.example.android.schoolfinder.Utility.LocationAddress;
import com.example.android.schoolfinder.Utility.Validation;
import com.example.android.schoolfinder.databinding.FragmentSchoolSignUpFieldsBinding;
import com.example.android.schoolfinder.interfaces.AuthenticationCallbacks;
import com.example.android.schoolfinder.interfaces.AuthenticationViewPagerCallbacks;
import com.example.android.schoolfinder.interfaces.GetLocationCallback;
import com.example.android.schoolfinder.schoolOwners.Activities.AuthenticationViewPagerActivity;
import com.example.android.schoolfinder.schoolOwners.HomeActivity;
import com.google.firebase.auth.FirebaseUser;

//import com.example.android.schoolfinder.Activities.AuthenticationViewPagerActivity;
//import com.example.android.schoolfinder.HomeActivity;
//import com.example.android.schoolfinder.schoolOwners.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SchoolSignUpFragment extends Fragment implements AuthenticationCallbacks, View.OnClickListener {

    private static final String TAG = SchoolSignUpFragment.class.getSimpleName();
    FragmentSchoolSignUpFieldsBinding schoolSignUpFieldsBinding;
    private AppLocationService locationService;
    private Authentication auth = new Authentication(this);
    private AuthenticationViewPagerCallbacks authenticationViewPagerCallbacks;
    private AppCompatEditText mSchoolNameE, mSchoolContactE, mSchoolEmailE, mSchoolLocationE, mSchoolBiographyE,
            mPasswordE, mConfirmPasswordE;
    private TextInputLayout mSchoolNameL, mSchoolContactL, mSchoolEmailL, mSchoolLocationL, mSchoolBiographyL,
            mPasswordL, mConfirmPasswordL;
    private Users mSchoolOwnerDetails;
    private double latitude, longitude;
    private String addressFromLocation;

    public SchoolSignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        schoolSignUpFieldsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_school_sign_up_fields,
                container, false);
        initFields();
        if (getActivity() != null)
            locationService = new AppLocationService(getActivity());
        schoolSignUpFieldsBinding
                .loginButton.setOnClickListener(this);
        schoolSignUpFieldsBinding
                .signupButton.setOnClickListener(this);
        schoolSignUpFieldsBinding
                .signupPrevious.setOnClickListener(this);
        mSchoolLocationE.setFocusable(false);
        mSchoolLocationE.setOnClickListener(this);
        return schoolSignUpFieldsBinding.getRoot();
    }

    /**
     * This method initializes the authentication callback
     *
     * @param authenticationViewPagerCallbacks view pager callback
     */
    public void initAuthenticationCallbacks(AuthenticationViewPagerCallbacks authenticationViewPagerCallbacks) {

        this.authenticationViewPagerCallbacks = authenticationViewPagerCallbacks;
    }

    private void onLocationEdittextClicked() {
        if (locationService != null) {
            final Location location = locationService
                    .getLocation(LocationManager.GPS_PROVIDER);
            Log.e(TAG, "onLocationEdittextClicked() called ---  --- ");
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                final LocationAddress locationAddress = new LocationAddress();
                locationAddress.getAddressFromLocation(latitude, longitude,
                        getActivity(), getActivity(), new GeocoderHandler(new GetLocationCallback() {
                            @Override
                            public void setAddress(String address) {
                                Log.e(TAG, "Addreess oooh --- " + address);
                                addressFromLocation = address;
                            }
                        }));
            }else {
                showSettingsAlert();
            }
        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                getActivity());
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        getActivity().startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    /**
     * This method initializes all fields
     */
    private void initFields() {
        mSchoolNameE = schoolSignUpFieldsBinding.signupSchoolName;
        mSchoolNameL = schoolSignUpFieldsBinding.signupSchoolNameInputLayout;
        mSchoolContactE = schoolSignUpFieldsBinding.signupSchoolContact;
        mSchoolContactL = schoolSignUpFieldsBinding.signupSchoolContactInputLayout;
        mSchoolEmailE = schoolSignUpFieldsBinding.signupSchoolEmail;
        mSchoolEmailL = schoolSignUpFieldsBinding.signupSchoolEmailInputLayout;
        mSchoolLocationE = schoolSignUpFieldsBinding.signupSchoolLocation;
        mSchoolLocationL = schoolSignUpFieldsBinding.signupSchoolLocationInputLayout;
        mSchoolBiographyE = schoolSignUpFieldsBinding.signupSchoolBiography;
        mSchoolBiographyL = schoolSignUpFieldsBinding.signupSchoolBiographyInputLayout;
        mPasswordE = schoolSignUpFieldsBinding.signupPassword;
        mPasswordL = schoolSignUpFieldsBinding.signupPasswordInputLayout;
        mConfirmPasswordE = schoolSignUpFieldsBinding.signUpConfirmPassword;
        mConfirmPasswordL = schoolSignUpFieldsBinding.signUpConfirmPasswordInputLayout;
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
        mSchoolNameL.setError(null);
        mSchoolContactL.setError(null);
        mSchoolEmailL.setError(null);
        mSchoolLocationL.setError(null);
        mSchoolBiographyL.setError(null);
        mPasswordL.setError(null);
        mConfirmPasswordL.setError(null);
    }

    /**
     * This method shows progressbar and hides the button text
     */
    private void enableProgressbar() {
        schoolSignUpFieldsBinding.signUpProgressbar.setVisibility(View.VISIBLE);
        schoolSignUpFieldsBinding.signUpTextview.setVisibility(View.GONE);
    }

    /**
     * This method hides progressbar and shows the button text
     */
    private void disableProgressbar() {
        schoolSignUpFieldsBinding.signUpProgressbar.setVisibility(View.GONE);
        schoolSignUpFieldsBinding.signUpTextview.setVisibility(View.VISIBLE);
    }


    /**
     * This method validates the fields
     *
     * @return the number of errors
     */
    private int validateFields() {
        int errorCount = 0;
        if (!Validation.validateFields(getTextFromEditText(mSchoolNameE))) {
            mSchoolNameL.setError(Validation.EMPTY_FIELD);
            errorCount++;
        }

        if (!Validation.validateFields(getTextFromEditText(mSchoolContactE))) {
            mSchoolContactL.setError(Validation.EMPTY_FIELD);
            errorCount++;
        }

        if (!Validation.validateEmail(getTextFromEditText(mSchoolEmailE))) {
            mSchoolEmailL.setError(Validation.EMAIL_NOT_VALID);
            errorCount++;
        }

        if (!Validation.validateFields(getTextFromEditText(mSchoolLocationE))) {
            mSchoolLocationL.setError(Validation.EMPTY_FIELD);
            errorCount++;
        }

        if (!Validation.validateFields(getTextFromEditText(mSchoolBiographyE))) {
            mSchoolBiographyL.setError(Validation.EMPTY_FIELD);
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

    /**
     * method to run actions when sign up button is clicked
     */
    private void signUpButtonClicked() {
        if (validateFields() == 0) {
            enableProgressbar();
            auth.createNewUser(getTextFromEditText(mSchoolEmailE), getTextFromEditText(mPasswordE));

        } else {
            disableProgressbar();
        }
    }

    /**
     * This is used to set the school owners detail from the other page
     *
     * @param schoolOwnerDetails an instance of user, initializes the school owner details
     */
    public void setSchoolOwnerDetails(Users schoolOwnerDetails) {

        mSchoolOwnerDetails = schoolOwnerDetails;
    }


    /**
     * Initializes a user object
     *
     * @return user
     */
    private School getSchool() {
        if (null != mSchoolOwnerDetails) {
            School school = new School();
            school.setSchoolOwnerDetails(mSchoolOwnerDetails);
            school.setSchoolName(getTextFromEditText(mSchoolNameE));
            school.setSchoolContact(getTextFromEditText(mSchoolContactE));
            school.setSchoolEmail(getTextFromEditText(mSchoolEmailE));
            school.setSchoolLocation(getTextFromEditText(mSchoolLocationE));
            school.setSchoolBiography(getTextFromEditText(mSchoolBiographyE));
            Log.e(TAG, "School owner detail null");
            return school;
        }
        return null;
    }

    @Override
    public void login(boolean loggedInSuccessful, FirebaseUser user) {

    }

    @Override
    public void signUp(boolean signedUpSuccessful, FirebaseUser user) {
        Log.e(TAG, "SignedUp method called: successful? " + signedUpSuccessful);
        School school = getSchool();
        if (school != null) {
            school.setId(user.getUid());
            if (signedUpSuccessful)
                auth.putNewUserInDb(school);
            else {
                disableProgressbar();
            }
            Log.e(TAG, "school is null ooooh ");
        }
    }

    @Override
    public void userInsertedToDatabase(Users users) {
    }

    @Override
    public void userInsertedToDatabase(School school) {
        if (school != null) {
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(BundleConstants.USER_BUNDLE, school);
            startActivity(intent);
        } else {
            auth.deleteAccount((Users) null);
        }
    }

    @Override
    public void loggedOut() {
        Intent intent = new Intent(getActivity(), AuthenticationViewPagerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signup_button:
                setAuthFieldErrorNull();
                if (validateFields() == 0) {
                    //TODO: Open a dialog box to get image of proof certificate
                    signUpButtonClicked();
                }
                break;
            case R.id.login_button:
                authenticationViewPagerCallbacks.loginButtonClicked(false, null, null);
                break;
            case R.id.signup_previous:
                authenticationViewPagerCallbacks.previousButtonCLicked();
                break;
            case R.id.signup_school_location:
                onLocationEdittextClicked();
                break;
        }
    }

}
