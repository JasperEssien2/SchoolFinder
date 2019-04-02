package com.example.android.schoolfinder.normalUsers.Fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
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
import com.example.android.schoolfinder.FirebaseHelper.MediaStorage;
import com.example.android.schoolfinder.Models.Certificate;
import com.example.android.schoolfinder.Models.Image;
import com.example.android.schoolfinder.Models.Post;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.Models.Users;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.Utility.GeocoderHandler;
import com.example.android.schoolfinder.Utility.LocationAddress;
import com.example.android.schoolfinder.Utility.Validation;
import com.example.android.schoolfinder.databinding.FragmentSignUpBinding;
import com.example.android.schoolfinder.interfaces.AuthenticationCallbacks;
import com.example.android.schoolfinder.interfaces.AuthenticationViewPagerCallbacks;
import com.example.android.schoolfinder.interfaces.GetLocationCallback;
import com.example.android.schoolfinder.interfaces.MediaStorageCallback;
import com.example.android.schoolfinder.normalUsers.Activities.AuthenticationViewPagerActivity;
import com.example.android.schoolfinder.normalUsers.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

//import com.example.android.schoolfinder.Activities.AuthenticationViewPagerActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment implements AuthenticationCallbacks, MediaStorageCallback {

    private static final String TAG = SignUpFragment.class.getSimpleName();
    private static final int SELECT_PHOTO = 358;
    // integer for permissions results request
    private static final int ALL_PERMISSIONS_RESULT = 1011;
    final Criteria criteria = new Criteria();
    final Looper looper = null;
    private Authentication auth = new Authentication(this);
    private MediaStorage storage = new MediaStorage(this);
    private String mCountry, mState_region, mCity;
    // lists for permissions
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private FragmentSignUpBinding fragmentSignUpBinding;
    private AuthenticationViewPagerCallbacks authenticationViewPagerCallbacks;
    private AppCompatEditText mNameE, mContactE, mEmailE, mLocationE, mPasswordE,
            mConfirmPasswordE;
    private TextInputLayout mNameL, mContactL, mEmailL, mLocationL, mPasswordL,
            mConfirmPasswordL;
    private Location mLocation;
    private Uri imageUri;
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
                                countriesViewModel = new ViewModelProvider.NewInstanceFactory()
                                        .create(GeoNamesViewModels.CountriesViewModel.class);
                                countriesViewModel.getCountry(mLocation.getLatitude(), mLocation.getLongitude())
                                        .observe(SignUpFragment.this, new Observer<Country>() {
                                            @Override
                                            public void onChanged(@Nullable Country country) {
                                                mCountry = country.getCountryName();
                                                mState_region = country.getAdminName1();
                                                mAddress = mState_region + ", " + mCountry;
                                            }
                                        });
                            } else {
                                mCountry = country;
                                mState_region = stateRegion;
                                mCity = city;
                                mAddress = address;
                            }
                            fragmentSignUpBinding
                                    .signupLocation.setText(mAddress);
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
//    private LocationAddress locationAddress = new LocationAddress();

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentSignUpBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false);
        initFields();
        // Now first make a criteria with your requirements
        // this is done to save the battery life of the device
        // there are various other other criteria you can search for..

        // we add permissions we need to request location of the users
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

        fragmentSignUpBinding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authenticationViewPagerCallbacks.loginButtonClicked(false, null, null);
            }
        });

        fragmentSignUpBinding.signupLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                locationManager.requestSingleUpdate(criteria, locationListener, looper);
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

        fragmentSignUpBinding.ownerSettingsImagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePicker();
            }
        });
        return fragmentSignUpBinding.getRoot();
    }

    /**
     * This method is to initializez the viewpager callback, that controls
     * the page that should be showing depending on which button the user clicks
     *
     * @param authenticationViewPagerCallbacks the callback
     */
    public void initAuthenticationCallbacks(AuthenticationViewPagerCallbacks authenticationViewPagerCallbacks) {

        this.authenticationViewPagerCallbacks = authenticationViewPagerCallbacks;
    }

    /**
     * This method is called to open up the image image picker intent
     */
    private void imagePicker() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    imageUri = data.getData();
                    if (imageUri != null) {
                        Picasso
                                .get()
                                .load(imageUri)
                                .into(fragmentSignUpBinding.ownerSettingsImage);
                    } else
                        Toast.makeText(getActivity(), "Error getting image", Toast.LENGTH_SHORT).show();

                }
                break;
        }
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
                    locationManager.requestSingleUpdate(criteria, locationListener, looper);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
        }
    }

    private ArrayList<String> permissionsToRequest(ArrayList<String> wantedPermissions) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wantedPermissions) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
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
        mLocationE.setFocusable(false);
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
            mConfirmPasswordL.setError(Validation.PASSWORD_NOT_EQUAL);
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
        users.setLatitude(mLocation.getLatitude());
        users.setLongitude(mLocation.getLongitude());
        users.setState_region(mState_region);
        users.setCountry(mCountry);
        return users;
    }

    @Override
    public void login(boolean loggedInSuccessful, FirebaseUser user) {
    }

    @Override
    public void signUp(boolean signedUpSuccessful, FirebaseUser user) {
        if (user == null) {
            Toast.makeText(getActivity(), "Sign up wasn't succesful pls try again",
                    Toast.LENGTH_SHORT).show();
            disableProgressbar();
            return;
        }

        if (signedUpSuccessful)
            if (imageUri != null)
                storage.addProfileImage(false, imageUri);
            else {
                Users users = getUser();
                users.setId(user.getUid());
                auth.putNewUserInDb(users);
//                } else Toast.makeText(getActivity(), "Current user null", Toast.LENGTH_SHORT).show();
                //TODO
                disableProgressbar();
            }
    }

    @Override
    public void userInsertedToDatabase(Users users) {
        if (users != null) {
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(BundleConstants.USER_BUNDLE, users);
            startActivity(intent);
        } else {
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

    @Override
    public void userGotten(School school) {

    }

    @Override
    public void userGotten(Users users) {

    }

    @Override
    public void accountUpdated(boolean isEmail, boolean isSuccessful, String newEmail) {

    }

    @Override
    public void profileImageStored(String imageUrl, boolean isSuccesful) {
        Users users = getUser();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            users.setId(FirebaseAuth.getInstance().getCurrentUser().getUid());
            auth.putNewUserInDb(users);
        } else Toast.makeText(getActivity(), "Current user null", Toast.LENGTH_SHORT).show();
        //TODO
    }

    @Override
    public void certificateImageStored(Certificate certificate, String imageUrl, boolean isCert) {

    }

    @Override
    public void profileImageDeleted(boolean isSuccessful) {

    }

    @Override
    public void logoStored(String imageUrl) {

    }

    @Override
    public void schoolImageAdded(String imageUrl, String tag) {

    }

    @Override
    public void schoolImageAdded(List<Image> images, boolean isSuccessful) {

    }

    @Override
    public void postImageAdded(Post post, boolean isSuccessful) {

    }
}
