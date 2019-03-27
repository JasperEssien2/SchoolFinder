package com.example.android.schoolfinder.schoolOwners.Fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.countryregioncitypicker.Models.Country;
import com.example.android.countryregioncitypicker.Models.GeoNamesViewModels;
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
import com.example.android.schoolfinder.schoolOwners.DialogFragments.CategoryDialogFragment;
import com.example.android.schoolfinder.schoolOwners.HomeActivity;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

//import com.example.android.schoolfinder.Activities.AuthenticationViewPagerActivity;
//import com.example.android.schoolfinder.HomeActivity;
//import com.example.android.schoolfinder.schoolOwners.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SchoolSignUpFragment extends Fragment implements AuthenticationCallbacks, View.OnClickListener {

    private static final String TAG = SchoolSignUpFragment.class.getSimpleName();
    private static final int ALL_PERMISSIONS_RESULT = 1011;
    final Criteria criteria = new Criteria();
    FragmentSchoolSignUpFieldsBinding schoolSignUpFieldsBinding;
    private AppLocationService locationService;
    private Authentication auth = new Authentication(this);
    private String mCountry, mState_region, mCity;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private Location mLocation;
    private AuthenticationViewPagerCallbacks authenticationViewPagerCallbacks;
    private AppCompatEditText mSchoolNameE, mSchoolContactE, mSchoolEmailE, mSchoolLocationE, mSchoolBiographyE,
            mPasswordE, mConfirmPasswordE;
    private TextInputLayout mSchoolNameL, mSchoolContactL, mSchoolEmailL, mSchoolLocationL, mSchoolBiographyL,
            mPasswordL, mConfirmPasswordL;
    private Users mSchoolOwnerDetails;
    private double latitude, longitude;
    private String addressFromLocation;
    private GeoNamesViewModels.CountriesViewModel countriesViewModel;
    private String mAddress = "";
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mLocation = location;
            LocationAddress.getAddressFromLocation(mLocation.getLatitude(), mLocation.getLongitude(),
                    getActivity(), getActivity(), new GeocoderHandler(new GetLocationCallback() {
                        @Override
                        public void setAddress(String address, String country, String stateRegion, String city) {
                            if (address == null) {
                                Log.e(TAG, "Address === null ooh");
                                countriesViewModel = new ViewModelProvider.NewInstanceFactory()
                                        .create(GeoNamesViewModels.CountriesViewModel.class);
                                countriesViewModel.getCountry(mLocation.getLatitude(), mLocation.getLongitude())
                                        .observe(SchoolSignUpFragment.this, new Observer<Country>() {
                                            @Override
                                            public void onChanged(@Nullable Country country) {
//                                                if (country != null) {
                                                    mCountry = country.getCountryName();
//                                                }
//                                                assert country != null;
                                                mState_region = country.getAdminName1();
                                                mAddress = mState_region + ", " + mCountry;
                                                Log.e(TAG, "Address ---- " + mAddress);
                                                schoolSignUpFieldsBinding.signupSchoolLocation.setText(mAddress);
                                            }
                                        });
                            } else {
                                Log.e(TAG, "Address NOT null ooh");
                                mCountry = country;
                                mState_region = stateRegion;
                                mCity = city;
                                mAddress = address;
                                schoolSignUpFieldsBinding.signupSchoolLocation.setText(mAddress);
                            }
                            Log.e(TAG, "Adresss oooooh -----------------------------" + mAddress);
                        }
                    }));
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };
    private LocationManager locationManager;
    private List<String> categories;

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
        schoolSignUpFieldsBinding.signupSchoolCategory.setFocusable(false);
        schoolSignUpFieldsBinding.signupSchoolCategory.setOnClickListener(this);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionsToRequest = permissionsToRequest(permissions);

        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);

        // Now create a location manager
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
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

    /**
     * Action to take when the location edittext field is clicked
     */
    private void onLocationEdittextClicked() {

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), permissionsToRequest.toArray(new String[0]), ALL_PERMISSIONS_RESULT);
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestSingleUpdate(criteria, locationListener, null);

//        if (locationService != null) {
//            final Location location = locationService
//                    .getLocation(LocationManager.GPS_PROVIDER);
//            Log.e(TAG, "onLocationEdittextClicked() called ---  --- ");
//            if (location != null) {
//                latitude = location.getLatitude();
//                longitude = location.getLongitude();
//                final LocationAddress locationAddress = new LocationAddress();
//                LocationAddress.getAddressFromLocation(latitude, longitude,
//                        getActivity(), getActivity(), new GeocoderHandler(new GetLocationCallback() {
//                            @Override
//                            public void setAddress(String address) {
//                                Log.e(TAG, "Addreess oooh --- " + address);
//                                addressFromLocation = address;
//                                mSchoolLocationE.setText(address);
//                            }
//                        }));
//            }else {
//                showSettingsAlert();
//            }
//        }
    }

    /**
     * Action to take when the category edittext field is clicked
     */
    private void onCategoryEdittextClicked() {
        CategoryDialogFragment categoryDialogFragment = new CategoryDialogFragment();
        categoryDialogFragment.initSchoolSignUpFragment(this);
        categoryDialogFragment.show(getActivity().getSupportFragmentManager(), null);
    }

    /**
     * This is called in the CategoryDialogFragment to set the categories chosen by the user
     * @param categories of the school
     */
    public void setSchoolCategories(List<String> categories) {
        this.categories = categories;
        if (categories != null && !categories.isEmpty())
            schoolSignUpFieldsBinding.signupSchoolCategory.setText(categories.toString().substring(1, categories.toString().length() - 1));
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

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    locationManager.requestSingleUpdate(criteria, locationListener, null);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
        }
    }

    /**
     * This method adds permission to request from the device
     * @param wantedPermissions
     * @return list of permission required
     */
    private ArrayList<String> permissionsToRequest(ArrayList<String> wantedPermissions) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wantedPermissions) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    /**
     * For android 6.0 up, this method checks to see if the app has permission from the device to use
     * location
     * @param permission required
     * @return
     */
    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
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

        if (!Validation.validateFields(getTextFromEditText(schoolSignUpFieldsBinding.signupSchoolCategory))) {
            schoolSignUpFieldsBinding.signupSchoolCategoryInputLayout.setError(Validation.EMPTY_FIELD);
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
            school.setCountry(mCountry);
            school.setState_region(mState_region);
            school.setSchoolCategory(categories);
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
        if (user == null) {
            Toast.makeText(getActivity(), "Sign up was't, succesful please try again",
                    Toast.LENGTH_SHORT).show();
            return;
        }
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
            case R.id.signup_school_category:
                onCategoryEdittextClicked();
                break;
        }
    }

}
